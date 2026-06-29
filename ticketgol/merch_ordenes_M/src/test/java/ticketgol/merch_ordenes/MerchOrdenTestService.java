package ticketgol.merch_ordenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ticketgol.merch_ordenes.model.MerchOrden;
import ticketgol.merch_ordenes.repository.MerchRepository;
import ticketgol.merch_ordenes.service.MerchOrdenService;
import ticketgol.merch_ordenes.webClient.MerchClient;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MerchOrdenTestService {


    @Mock
    private MerchRepository repository;


    @Mock
    private MerchClient merchandiseClient;


    @InjectMocks
    private MerchOrdenService merchOrdenService;



    private MerchOrden orden;



    @BeforeEach
    void setUp(){

        orden = new MerchOrden();

        orden.setId(1L);
        orden.setCantidad(2);
        orden.setPrecioUnitario(5000);
        orden.setPrecioTotal(10000);
        orden.setMerchandiseId(101L);
        orden.setUserId(55L);
        orden.setStatus(true);
    }



    @Test
    @DisplayName("Debe retornar todas las órdenes")
    void shouldReturnAllOrdenes(){


        when(repository.findAll())
                .thenReturn(Arrays.asList(orden));


        List<MerchOrden> resultado =
                merchOrdenService.findAllOrdenes();



        System.out.println("TEST FIND ALL");
        System.out.println("Órdenes encontradas: " + resultado.size());


        assertNotNull(resultado);

        assertEquals(1, resultado.size());


        verify(repository)
                .findAll();

    }





    @Test
    @DisplayName("Debe retornar una orden por ID")
    void shouldReturnOrdenById(){


        when(repository.findById(1L))
                .thenReturn(Optional.of(orden));



        MerchOrden resultado =
                merchOrdenService.getOrdenById(1L);



        System.out.println("TEST FIND BY ID");

        System.out.println(
                "Orden encontrada ID: "
                        + resultado.getId()
        );



        assertNotNull(resultado);

        assertEquals(1L, resultado.getId());



        verify(repository)
                .findById(1L);

    }





    @Test
    @DisplayName("Debe mostrar mensaje cuando la orden no existe")
    void shouldThrowExceptionWhenOrdenNotFound(){


        when(repository.findById(999L))
                .thenReturn(Optional.empty());



        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> merchOrdenService.getOrdenById(999L)
                );



        System.out.println("ERROR CONTROLADO:");

        System.out.println(exception.getMessage());



        verify(repository)
                .findById(999L);

    }





    @Test
    @DisplayName("Debe crear una orden correctamente")
    void shouldCreateOrden(){


        Map<String,Object> producto = new HashMap<>();

        producto.put("id",101L);



        when(merchandiseClient.getMerchandise(101L))
                .thenReturn(producto);



        when(repository.save(any(MerchOrden.class)))
                .thenReturn(orden);



        MerchOrden resultado =
                merchOrdenService.crearOrden(orden);



        System.out.println("TEST CREAR ORDEN");

        System.out.println(
                "Orden creada ID: " + resultado.getId()
        );

        System.out.println(
                "Precio total calculado: "
                        + resultado.getPrecioTotal()
        );



        assertNotNull(resultado);

        assertEquals(10000, resultado.getPrecioTotal());



        verify(merchandiseClient)
                .getMerchandise(101L);


        verify(repository)
                .save(orden);

    }





    @Test
    @DisplayName("No debe crear orden si merchandise no existe")
    void shouldNotCreateWhenMerchNotFound(){


        when(merchandiseClient.getMerchandise(101L))
                .thenReturn(Collections.emptyMap());



        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> merchOrdenService.crearOrden(orden)
                );



        System.out.println("ERROR CONTROLADO:");

        System.out.println(exception.getMessage());



        verify(repository, never())
                .save(any());

    }





    @Test
    @DisplayName("Debe actualizar una orden existente")
    void shouldUpdateOrden(){


        MerchOrden nueva = new MerchOrden();


        nueva.setCantidad(5);

        nueva.setPrecioUnitario(3000);



        when(repository.findById(1L))
                .thenReturn(Optional.of(orden));



        when(repository.save(any(MerchOrden.class)))
                .thenReturn(orden);



        MerchOrden resultado =
                merchOrdenService.updateOrden(1L,nueva);



        System.out.println("TEST UPDATE");

        System.out.println(
                "Nuevo total: " + resultado.getPrecioTotal()
        );



        assertNotNull(resultado);



        verify(repository)
                .save(orden);

    }





    @Test
    @DisplayName("Debe eliminar una orden existente")
    void shouldDeleteOrden(){


        when(repository.existsById(1L))
                .thenReturn(true);



        assertDoesNotThrow(() ->
                merchOrdenService.deleteOrden(1L)
        );



        System.out.println(
                "Orden eliminada correctamente ID: 1"
        );



        verify(repository)
                .deleteById(1L);

    }





    @Test
    @DisplayName("Debe mostrar error al eliminar orden inexistente")
    void shouldFailDeleteOrden(){


        when(repository.existsById(999L))
                .thenReturn(false);



        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> merchOrdenService.deleteOrden(999L)
                );



        System.out.println("ERROR CONTROLADO:");

        System.out.println(exception.getMessage());



        verify(repository, never())
                .deleteById(anyLong());

    }

}