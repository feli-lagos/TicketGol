package ticketgol.compras;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import ticketgol.compras.Exception.ComprasNotFoundException;
import ticketgol.compras.Model.Compras;
import ticketgol.compras.Repository.ComprasRepository;
import ticketgol.compras.Service.ComprasService;

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

    private ComprasService comprasService;


    @BeforeEach
    void setUp() {

        when(webClientBuilder.build()).thenReturn(webClient);

        comprasService = new ComprasService(
                repositorio,
                webClientBuilder
        );
    }


    @Test
    void testObtenerTodos_DeberiaRetornarListaDeCompras() {

        Compras compra1 = new Compras();
        compra1.setId(1L);

        Compras compra2 = new Compras();
        compra2.setId(2L);


        when(repositorio.findAll())
                .thenReturn(Arrays.asList(compra1, compra2));


        List<Compras> resultado = comprasService.obtenerTodos();


        System.out.println("TEST obtenerTodos: Compras encontradas = " + resultado.size());


        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(repositorio).findAll();
    }


    @Test
    void testObtenerPorId_CuandoCompraExiste_DeberiaRetornarCompra() {

        Long compraId = 1L;


        Compras compraMock = new Compras();
        compraMock.setId(compraId);
        compraMock.setUsuarioId("usuario_test");


        when(repositorio.findById(compraId))
                .thenReturn(Optional.of(compraMock));


        Compras resultado = comprasService.obtenerPorId(compraId);


        System.out.println(
                "TEST obtenerPorId: Compra encontrada ID = "
                        + resultado.getId()
        );


        assertNotNull(resultado);
        assertEquals(compraId, resultado.getId());
        assertEquals("usuario_test", resultado.getUsuarioId());


        verify(repositorio).findById(compraId);
    }


    @Test
    void testObtenerPorId_CuandoCompraNoExiste_DeberiaLanzarExcepcion() {

        Long compraId = 99L;


        when(repositorio.findById(compraId))
                .thenReturn(Optional.empty());


        ComprasNotFoundException exception = assertThrows(
                ComprasNotFoundException.class,
                () -> comprasService.obtenerPorId(compraId)
        );


        System.out.println(
                "ERROR esperado obtenerPorId: "
                        + exception.getMessage()
        );


        verify(repositorio).findById(compraId);
    }


    @Test
    void testEliminar_CuandoCompraExiste_DeberiaEliminarCorrectamente() {

        Long compraId = 1L;


        when(repositorio.existsById(compraId))
                .thenReturn(true);


        doNothing()
                .when(repositorio)
                .deleteById(compraId);


        assertDoesNotThrow(() ->
                comprasService.eliminar(compraId)
        );


        System.out.println(
                "TEST eliminar: Compra eliminada correctamente ID = "
                        + compraId
        );


        verify(repositorio).existsById(compraId);
        verify(repositorio).deleteById(compraId);
    }


    @Test
    void testEliminar_CuandoCompraNoExiste_DeberiaLanzarExcepcion() {

        Long compraId = 99L;


        when(repositorio.existsById(compraId))
                .thenReturn(false);


        ComprasNotFoundException exception = assertThrows(
                ComprasNotFoundException.class,
                () -> comprasService.eliminar(compraId)
        );


        System.out.println(
                "ERROR esperado eliminar: "
                        + exception.getMessage()
        );


        verify(repositorio).existsById(compraId);


        verify(repositorio, never())
                .deleteById(anyLong());
    }
}