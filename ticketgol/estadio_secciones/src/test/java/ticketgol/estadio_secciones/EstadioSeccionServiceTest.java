package ticketgol.estadio_secciones;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ticketgol.estadio_secciones.dto.EstadioDTO;
import ticketgol.estadio_secciones.exception.EstadioNotFoundException;
import ticketgol.estadio_secciones.exception.EstadioSeccionNotFoundException;
import ticketgol.estadio_secciones.model.EstadioSeccion;
import ticketgol.estadio_secciones.repository.EstadioSeccionRepository;
import ticketgol.estadio_secciones.service.EstadioSeccionService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@ActiveProfiles("test")
public class EstadioSeccionServiceTest {

    @InjectMocks
    private EstadioSeccionService estadioSeccionService;

    @Mock
    private EstadioSeccionRepository seccionRepository;

    @Mock
    private RestTemplate restTemplate;

    private EstadioSeccion seccionMock;
    private EstadioDTO estadioDtoMock;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(estadioSeccionService, "restTemplate", restTemplate);

        seccionMock = new EstadioSeccion();
        seccionMock.setId(1L);
        seccionMock.setEstadioId(10L);
        seccionMock.setNombre("Tribuna Norte");
        seccionMock.setCantidadAsientos(5000);

        estadioDtoMock = new EstadioDTO(10L, "Estadio Nacional", "Santiago", 45000, "Av. Grecia 2001");
    }

    @Test
    public void buscarTodasLasSecciones() {
        Mockito.when(seccionRepository.findAll()).thenReturn(Arrays.asList(seccionMock));

        List<EstadioSeccion> resultado = estadioSeccionService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void buscarPorIdExitoso() {
        Mockito.when(seccionRepository.findById(1L)).thenReturn(Optional.of(seccionMock));

        EstadioSeccion resultado = estadioSeccionService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void buscarPorIdNoEncontrado() {
        Mockito.when(seccionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EstadioSeccionNotFoundException.class, () -> {
            estadioSeccionService.findById(99L);
        });
    }

    @Test
    public void buscarPorEstadioIdExitoso() {
        Mockito.when(seccionRepository.findByEstadioId(10L)).thenReturn(Arrays.asList(seccionMock));

        List<EstadioSeccion> resultado = estadioSeccionService.findByEstadioId(10L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getEstadioId());
    }

    @Test
    public void guardarSeccionExitoso() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(EstadioDTO.class))).thenReturn(estadioDtoMock);
        Mockito.when(seccionRepository.save(any(EstadioSeccion.class))).thenReturn(seccionMock);

        EstadioSeccion resultado = estadioSeccionService.save(seccionMock);

        assertNotNull(resultado);
        assertEquals("Tribuna Norte", resultado.getNombre());
    }

    @Test
    public void guardarSeccionEstadioNoExiste() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(EstadioDTO.class)))
                .thenThrow(HttpClientErrorException.NotFound.create(null, null, null, null, null));

        assertThrows(EstadioNotFoundException.class, () -> {
            estadioSeccionService.save(seccionMock);
        });
    }

    @Test
    public void actualizarSeccionExitoso() {
        Mockito.when(seccionRepository.findById(1L)).thenReturn(Optional.of(seccionMock));
        Mockito.when(restTemplate.getForObject(anyString(), eq(EstadioDTO.class))).thenReturn(estadioDtoMock);
        Mockito.when(seccionRepository.save(any(EstadioSeccion.class))).thenReturn(seccionMock);

        EstadioSeccion resultado = estadioSeccionService.update(1L, seccionMock);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void eliminarSeccionExitoso() {
        Mockito.when(seccionRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(seccionRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            estadioSeccionService.delete(1L);
        });
    }

    @Test
    public void eliminarSeccionNoEncontrada() {
        Mockito.when(seccionRepository.existsById(99L)).thenReturn(false);

        assertThrows(EstadioSeccionNotFoundException.class, () -> {
            estadioSeccionService.delete(99L);
        });
    }
}