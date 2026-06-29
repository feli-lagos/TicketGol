package ticketgol.merch_ordenes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ticketgol.merch_ordenes.model.MerchOrden;
import ticketgol.merch_ordenes.repository.MerchRepository;
import ticketgol.merch_ordenes.service.MerchOrdenService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchOrdenTestService {

    @Mock
    private MerchRepository merchRepository;

    @InjectMocks
    private MerchOrdenService merchOrdenService;

    @Test
    @DisplayName("Debe retornar una lista de órdenes cuando existen registros en la BD")
    void shouldReturnOrdenListWhenOrdenesExist() {

        // --- GIVEN (Arrange) ---
        // Creamos la primera orden ficticia simulando tus atributos reales
        MerchOrden orden1 = new MerchOrden();
        orden1.setId(1L);
        orden1.setCantidad(2);
        orden1.setPrecioUnitario(5000);
        orden1.setPrecioTotal(10000);
        orden1.setFechaOrden(LocalDateTime.now());
        orden1.setStatus(true);
        orden1.setMerchandiseId(101L);
        orden1.setUserId(55L);

        // Creamos la segunda orden ficticia
        MerchOrden orden2 = new MerchOrden();
        orden2.setId(2L);
        orden2.setCantidad(1);
        orden2.setPrecioUnitario(15000);
        orden2.setPrecioTotal(15000);
        orden2.setFechaOrden(LocalDateTime.now());
        orden2.setStatus(true);
        orden2.setMerchandiseId(102L);
        orden2.setUserId(56L);

        // Simulamos el comportamiento del repositorio
        when(merchRepository.findAll()).thenReturn(Arrays.asList(orden1, orden2));

        // --- WHEN (Act) ---
        List<MerchOrden> resultado = merchOrdenService.findAllOrdenes();

        // --- THEN (Assert) ---
        assertNotNull(resultado, "La lista de órdenes no debería ser nula");
        assertEquals(2, resultado.size(), "Deberían retornar exactamente 2 órdenes");

        // Verificaciones específicas basadas en tu modelo
        assertEquals(2, resultado.get(0).getCantidad(), "La cantidad de la primera orden debe ser 2");
        assertEquals(10000, resultado.get(0).getPrecioTotal(), "El precio total debe ser 10000");
        assertEquals(55L, resultado.get(0).getUserId(), "El ID de usuario debe ser 55");
        assertTrue(resultado.get(0).isStatus(), "El estado de la orden debe ser true (activa)");

        verify(merchRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe lanzar una excepción cuando se busca una orden por un ID que no existe")
    void shouldThrowExceptionWhenOrdenNotFound() {

        // --- GIVEN (Arrange) ---
        Long idInexistente = 999L;
        when(merchRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // --- WHEN & THEN (Act & Assert) ---
        assertThrows(RuntimeException.class, () -> {
            merchOrdenService.getOrdenById(idInexistente);
        });

        verify(merchRepository, times(1)).findById(idInexistente);
    }
}