package ticketgol.compras.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import ticketgol.compras.Exception.ComprasNotFoundException;
import ticketgol.compras.Model.Compras;
import ticketgol.compras.Repository.ComprasRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComprasServiceTest {

    @Mock
    private ComprasRepository repositorio;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ComprasService comprasService;

    @BeforeEach
    void setUp() {
        // Mockeo inicial del WebClientBuilder para evitar NullPointerException al instanciar el servicio
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
        comprasService = new ComprasService(repositorio, webClientBuilder);
    }

    @Test
    void testObtenerTodos_DeberiaRetornarListaDeCompras() {
        // Arrange (Preparar)
        Compras compra1 = new Compras();
        compra1.setId(1L);
        Compras compra2 = new Compras();
        compra2.setId(2L);

        when(repositorio.findAll()).thenReturn(Arrays.asList(compra1, compra2));

        // Act (Ejecutar)
        List<Compras> resultado = comprasService.obtenerTodos();

        // Assert (Verificar)
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(repositorio, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_CuandoCompraExiste_DeberiaRetornarCompra() {
        // Arrange
        Long compraId = 1L;
        Compras compraMock = new Compras();
        compraMock.setId(compraId);
        compraMock.setUsuarioId("usuario_test");

        when(repositorio.findById(compraId)).thenReturn(Optional.of(compraMock));

        // Act
        Compras resultado = comprasService.obtenerPorId(compraId);

        // Assert
        assertNotNull(resultado);
        assertEquals(compraId, resultado.getId());
        assertEquals("usuario_test", resultado.getUsuarioId());
        verify(repositorio, times(1)).findById(compraId);
    }

    @Test
    void testObtenerPorId_CuandoCompraNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        Long compraId = 99L;
        when(repositorio.findById(compraId)).thenReturn(Optional.empty());

        // Act & Assert (Verifica que tire la excepción personalizada que arreglamos antes)
        assertThrows(ComprasNotFoundException.class, () -> {
            comprasService.obtenerPorId(compraId);
        });

        verify(repositorio, times(1)).findById(compraId);
    }

    @Test
    void testEliminar_CuandoCompraExiste_DeberiaEliminarCorrectamente() {
        // Arrange
        Long compraId = 1L;
        when(repositorio.existsById(compraId)).thenReturn(true);
        doNothing().when(repositorio).deleteById(compraId);

        // Act
        assertDoesNotThrow(() -> comprasService.eliminar(compraId));

        // Assert
        verify(repositorio, times(1)).existsById(compraId);
        verify(repositorio, times(1)).deleteById(compraId);
    }

    @Test
    void testEliminar_CuandoCompraNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        Long compraId = 99L;
        when(repositorio.existsById(compraId)).thenReturn(false);

        // Act & Assert
        assertThrows(ComprasNotFoundException.class, () -> {
            comprasService.eliminar(compraId);
        });

        verify(repositorio, times(1)).existsById(compraId);
        verify(repositorio, never()).deleteById(anyLong());
    }
}