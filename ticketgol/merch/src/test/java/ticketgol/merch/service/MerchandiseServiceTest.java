package ticketgol.merch.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ticketgol.merch.dto.MerchandiseDTO;
import ticketgol.merch.exception.MerchandiseNotFoundException;
import ticketgol.merch.model.Merchandise;
import ticketgol.merch.repository.MerchandiseRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchandiseServiceTest {

    @Mock
    private MerchandiseRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MerchandiseService merchandiseService;

    // Datos base reutilizables en los tests
    private Merchandise crearArticulo(Long id, Long clubId) {
        Merchandise m = new Merchandise();
        m.setId(id);
        m.setNombre("Camiseta Titular");
        m.setDescripcion("Camiseta oficial temporada 2025");
        m.setPrecio(new BigDecimal("25990"));
        m.setStock(50);
        m.setImagenUrl("https://imagen.com/camiseta.jpg");
        m.setClubId(clubId);
        return m;
    }

    private MerchandiseDTO crearDTO(Long id, Long clubId) {
        return new MerchandiseDTO(
                id,
                "Camiseta Titular",
                "Camiseta oficial temporada 2025",
                new BigDecimal("25990"),
                50,
                "https://imagen.com/camiseta.jpg",
                clubId
        );
    }

    // -------------------------------------------------------
    // obtenerTodosLosArticulos()
    // -------------------------------------------------------

    @Test
    @DisplayName("Debe retornar una lista de artículos cuando existen registros en la BD")
    void shouldReturnArticleListWhenArticlesExist() {

        // GIVEN
        Merchandise m1 = crearArticulo(1L, 1L);
        Merchandise m2 = crearArticulo(2L, 2L);
        when(repository.findAll()).thenReturn(Arrays.asList(m1, m2));

        // WHEN
        List<MerchandiseDTO> resultado = merchandiseService.obtenerTodosLosArticulos();

        // THEN
        assertNotNull(resultado, "La lista no debería ser nula");
        assertEquals(2, resultado.size(), "Deberían retornarse exactamente 2 artículos");
        assertEquals("Camiseta Titular", resultado.get(0).getNombre());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando no hay artículos en la BD")
    void shouldReturnEmptyListWhenNoArticlesExist() {

        // GIVEN
        when(repository.findAll()).thenReturn(List.of());

        // WHEN
        List<MerchandiseDTO> resultado = merchandiseService.obtenerTodosLosArticulos();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");
        verify(repository, times(1)).findAll();
    }

    // -------------------------------------------------------
    // buscarPorId()
    // -------------------------------------------------------

    @Test
    @DisplayName("Debe retornar un MerchandiseDTO cuando el ID existe en la BD")
    void shouldReturnMerchandiseDTOWhenIdExists() {

        // GIVEN
        Merchandise articulo = crearArticulo(1L, 1L);
        when(repository.findById(1L)).thenReturn(Optional.of(articulo));

        // WHEN
        MerchandiseDTO resultado = merchandiseService.buscarPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Camiseta Titular", resultado.getNombre());
        assertEquals(new BigDecimal("25990"), resultado.getPrecio());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar MerchandiseNotFoundException cuando el ID no existe")
    void shouldThrowExceptionWhenArticleNotFound() {

        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(MerchandiseNotFoundException.class,
                () -> merchandiseService.buscarPorId(99L));
        verify(repository, times(1)).findById(99L);
    }

    // -------------------------------------------------------
    // guardarArticulo()
    // -------------------------------------------------------

    @Test
    @DisplayName("Debe guardar un artículo cuando el club asociado existe en el microservicio")
    void shouldSaveArticleWhenClubExists() {

        // GIVEN
        MerchandiseDTO dto = crearDTO(null, 1L);
        Merchandise articuloGuardado = crearArticulo(1L, 1L);

        // Simulamos que el microservicio de clubes responde OK
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenReturn(ResponseEntity.ok().build());
        when(repository.save(any(Merchandise.class))).thenReturn(articuloGuardado);

        // WHEN
        MerchandiseDTO resultado = merchandiseService.guardarArticulo(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getClubId());
        // Solo se valida 1 club (a diferencia de eventos que valida 2)
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Object.class));
        verify(repository, times(1)).save(any(Merchandise.class));
    }

    @Test
    @DisplayName("Debe lanzar MerchandiseNotFoundException cuando el club no existe en el microservicio")
    void shouldThrowWhenClubNotFound() {

        // GIVEN
        MerchandiseDTO dto = crearDTO(null, 99L);
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        // WHEN & THEN
        assertThrows(MerchandiseNotFoundException.class,
                () -> merchandiseService.guardarArticulo(dto));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando el microservicio de clubes no está disponible")
    void shouldThrowRuntimeExceptionWhenClubServiceIsDown() {

        // GIVEN
        MerchandiseDTO dto = crearDTO(null, 1L);
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenThrow(new ResourceAccessException("Servicio caído"));

        // WHEN & THEN
        assertThrows(RuntimeException.class,
                () -> merchandiseService.guardarArticulo(dto));
        verify(repository, never()).save(any());
    }

    // -------------------------------------------------------
    // actualizarArticulo()
    // -------------------------------------------------------

    @Test
    @DisplayName("Debe actualizar el artículo correctamente cuando el ID existe y el club no cambia")
    void shouldUpdateArticleWhenIdExistsAndClubUnchanged() {

        // GIVEN - el club no cambia, no se vuelve a validar contra el microservicio
        Merchandise existente = crearArticulo(1L, 1L);
        MerchandiseDTO dto = crearDTO(1L, 1L);
        dto.setNombre("Camiseta Alternativa");
        dto.setPrecio(new BigDecimal("19990"));

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Merchandise.class))).thenReturn(existente);

        // WHEN
        MerchandiseDTO resultado = merchandiseService.actualizarArticulo(1L, dto);

        // THEN
        assertNotNull(resultado);
        // El microservicio de clubes NO debería haberse consultado porque el clubId no cambió
        verify(restTemplate, never()).getForEntity(anyString(), eq(Object.class));
        verify(repository, times(1)).save(any(Merchandise.class));
    }

    @Test
    @DisplayName("Debe validar el nuevo club cuando el clubId cambia al actualizar")
    void shouldValidateNewClubWhenClubIdChanges() {

        // GIVEN - el club cambia de 1 a 2, debe validarse
        Merchandise existente = crearArticulo(1L, 1L);
        MerchandiseDTO dto = crearDTO(1L, 2L); // clubId distinto

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenReturn(ResponseEntity.ok().build());
        when(repository.save(any(Merchandise.class))).thenReturn(existente);

        // WHEN
        merchandiseService.actualizarArticulo(1L, dto);

        // THEN
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Object.class));
        verify(repository, times(1)).save(any(Merchandise.class));
    }

    @Test
    @DisplayName("Debe lanzar MerchandiseNotFoundException al actualizar un ID que no existe")
    void shouldThrowWhenUpdatingNonExistentArticle() {

        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(MerchandiseNotFoundException.class,
                () -> merchandiseService.actualizarArticulo(99L, crearDTO(99L, 1L)));
        verify(repository, never()).save(any());
    }

    // -------------------------------------------------------
    // eliminarArticulo()
    // -------------------------------------------------------

    @Test
    @DisplayName("Debe eliminar el artículo cuando el ID existe")
    void shouldDeleteArticleWhenIdExists() {

        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN
        assertDoesNotThrow(() -> merchandiseService.eliminarArticulo(1L));

        // THEN
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar MerchandiseNotFoundException al intentar eliminar un ID que no existe")
    void shouldThrowWhenDeletingNonExistentArticle() {

        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN & THEN
        assertThrows(MerchandiseNotFoundException.class,
                () -> merchandiseService.eliminarArticulo(99L));
        verify(repository, never()).deleteById(any());
    }
}