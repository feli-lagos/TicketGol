package ticketgol.eventos.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ticketgol.eventos.dto.EventoDTO;
import ticketgol.eventos.exception.EventoNotFoundException;
import ticketgol.eventos.model.Evento;
import ticketgol.eventos.repository.EventoRepository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EventoService eventoService;

    // Fecha en el futuro para pasar la validación @Future del DTO
    private final LocalDateTime fechaFutura = LocalDateTime.now().plusDays(30);

    // -------------------------------------------------------
    // obtenerTodosLosEventos()
    // -------------------------------------------------------

    @Test
    @DisplayName("Debe retornar una lista de eventos cuando existen registros en la BD")
    void shouldReturnEventListWhenEventsExist() {

        // GIVEN
        Evento e1 = new Evento(1L, 1L, 2L, 10L, fechaFutura, "PROGRAMADO");
        Evento e2 = new Evento(2L, 3L, 4L, 11L, fechaFutura, "PROGRAMADO");
        when(eventoRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        // WHEN
        List<EventoDTO> resultado = eventoService.obtenerTodosLosEventos();

        // THEN
        assertNotNull(resultado, "La lista no debería ser nula");
        assertEquals(2, resultado.size(), "Deberían retornarse exactamente 2 eventos");
        assertEquals(1L, resultado.get(0).getClubLocalId());
        verify(eventoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando no hay eventos en la BD")
    void shouldReturnEmptyListWhenNoEventsExist() {

        // GIVEN
        when(eventoRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<EventoDTO> resultado = eventoService.obtenerTodosLosEventos();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");
        verify(eventoRepository, times(1)).findAll();
    }

    // -------------------------------------------------------
    // buscarPorId()
    // -------------------------------------------------------

    @Test
    @DisplayName("Debe retornar un EventoDTO cuando el ID existe en la BD")
    void shouldReturnEventoDTOWhenIdExists() {

        // GIVEN
        Evento evento = new Evento(1L, 1L, 2L, 10L, fechaFutura, "PROGRAMADO");
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        // WHEN
        EventoDTO resultado = eventoService.buscarPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("PROGRAMADO", resultado.getEstado());
        verify(eventoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar EventoNotFoundException cuando el ID no existe")
    void shouldThrowExceptionWhenEventoNotFound() {

        // GIVEN
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(EventoNotFoundException.class, () -> eventoService.buscarPorId(99L));
        verify(eventoRepository, times(1)).findById(99L);
    }

    // -------------------------------------------------------
    // guardarEvento()
    // -------------------------------------------------------

    @Test
    @DisplayName("Debe guardar un evento cuando los dos clubes existen en el microservicio")
    void shouldSaveEventoWhenBothClubsExist() {

        // GIVEN
        EventoDTO dto = new EventoDTO(null, 1L, 2L, 10L, fechaFutura, "PROGRAMADO");
        Evento eventoGuardado = new Evento(1L, 1L, 2L, 10L, fechaFutura, "PROGRAMADO");

        // Simulamos que el microservicio de clubes responde 200 OK para ambos IDs
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenReturn(ResponseEntity.ok().build());
        when(eventoRepository.save(any(Evento.class))).thenReturn(eventoGuardado);

        // WHEN
        EventoDTO resultado = eventoService.guardarEvento(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getClubLocalId());
        // Verificamos que se llamó al microservicio 2 veces (una por cada club)
        verify(restTemplate, times(2)).getForEntity(anyString(), eq(Object.class));
        verify(eventoRepository, times(1)).save(any(Evento.class));
    }

    @Test
    @DisplayName("Debe lanzar EventoNotFoundException cuando el club local no existe en el microservicio")
    void shouldThrowWhenClubLocalNotFound() {

        // GIVEN
        EventoDTO dto = new EventoDTO(null, 99L, 2L, 10L, fechaFutura, "PROGRAMADO");

        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        // WHEN & THEN
        assertThrows(EventoNotFoundException.class, () -> eventoService.guardarEvento(dto));
        verify(eventoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando el microservicio de clubes no está disponible")
    void shouldThrowRuntimeExceptionWhenClubServiceIsDown() {

        // GIVEN
        EventoDTO dto = new EventoDTO(null, 1L, 2L, 10L, fechaFutura, "PROGRAMADO");

        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenThrow(new ResourceAccessException("Servicio caído"));

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> eventoService.guardarEvento(dto));
        verify(eventoRepository, never()).save(any());
    }

    // -------------------------------------------------------
    // eliminarEvento()
    // -------------------------------------------------------

    @Test
    @DisplayName("Debe eliminar el evento cuando el ID existe")
    void shouldDeleteEventoWhenIdExists() {

        // GIVEN
        when(eventoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(eventoRepository).deleteById(1L);

        // WHEN
        assertDoesNotThrow(() -> eventoService.eliminarEvento(1L));

        // THEN
        verify(eventoRepository, times(1)).existsById(1L);
        verify(eventoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar EventoNotFoundException al intentar eliminar un ID que no existe")
    void shouldThrowWhenDeletingNonExistentEvento() {

        // GIVEN
        when(eventoRepository.existsById(99L)).thenReturn(false);

        // WHEN & THEN
        assertThrows(EventoNotFoundException.class, () -> eventoService.eliminarEvento(99L));
        verify(eventoRepository, never()).deleteById(any());
    }
}