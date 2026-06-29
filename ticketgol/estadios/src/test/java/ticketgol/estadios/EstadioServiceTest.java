package ticketgol.estadios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ticketgol.estadios.exception.EstadioNotFoundException;
import ticketgol.estadios.model.Estadio;
import ticketgol.estadios.repository.EstadioRepository;
import ticketgol.estadios.service.EstadioService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadioServiceTest {

    @Mock
    private EstadioRepository estadioRepository;

    @InjectMocks
    private EstadioService estadioService;

    private Estadio stadiumMock;

    @BeforeEach
    void setUp() {
        stadiumMock = new Estadio();
        stadiumMock.setId(1L);
        stadiumMock.setNombre("National Stadium");
        stadiumMock.setCiudad("Santiago");
        stadiumMock.setCapacidad(45000);
        stadiumMock.setDireccion("Grecia Avenue 2001");
    }

    @Test
    @DisplayName("Debe retornar todos los estadios cuando existen registros")
    void shouldReturnAllStadiums() {

        // GIVEN
        when(estadioRepository.findAll()).thenReturn(Arrays.asList(stadiumMock));

        // WHEN
        List<Estadio> resultado = estadioService.findAll();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("National Stadium", resultado.get(0).getNombre());

        verify(estadioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar un estadio cuando el ID existe")
    void shouldReturnStadiumWhenIdExists() {

        // GIVEN
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(stadiumMock));

        // WHEN
        Estadio resultado = estadioService.findById(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Santiago", resultado.getCiudad());

        verify(estadioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el estadio no existe")
    void shouldThrowExceptionWhenStadiumNotFound() {

        // GIVEN
        when(estadioRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(
                EstadioNotFoundException.class,
                () -> estadioService.findById(99L)
        );

        verify(estadioRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar correctamente un estadio")
    void shouldSaveStadiumSuccessfully() {

        // GIVEN
        when(estadioRepository.save(any(Estadio.class))).thenReturn(stadiumMock);

        // WHEN
        Estadio resultado = estadioService.save(stadiumMock);

        assertNotNull(resultado);
        assertEquals("Santiago", resultado.getCiudad());

        verify(estadioRepository, times(1)).save(any(Estadio.class));
    }

    @Test
    @DisplayName("Debe actualizar correctamente un estadio")
    void shouldUpdateStadiumSuccessfully() {

        when(estadioRepository.findById(1L)).thenReturn(Optional.of(stadiumMock));
        when(estadioRepository.save(any(Estadio.class))).thenReturn(stadiumMock);

        Estadio resultado = estadioService.update(1L, stadiumMock);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(estadioRepository, times(1)).findById(1L);
        verify(estadioRepository, times(1)).save(any(Estadio.class));
    }

    @Test
    @DisplayName("Debe eliminar un estadio cuando el ID existe")
    void shouldDeleteStadiumWhenIdExists() {

        when(estadioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(estadioRepository).deleteById(1L);

        assertDoesNotThrow(() -> estadioService.delete(1L));

        verify(estadioRepository, times(1)).existsById(1L);
        verify(estadioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar un estadio inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentStadium() {

        when(estadioRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                EstadioNotFoundException.class,
                () -> estadioService.delete(99L)
        );

        verify(estadioRepository, times(1)).existsById(99L);
        verify(estadioRepository, never()).deleteById(any());
    }
}