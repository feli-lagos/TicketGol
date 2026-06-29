package ticketgol.clubes.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ticketgol.clubes.dto.ClubDTO;
import ticketgol.clubes.model.Club;
import ticketgol.clubes.repository.ClubRepository;
import static org.mockito.ArgumentMatchers.any;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Le decimos a JUnit 5 que usaremos Mockito
@ExtendWith(MockitoExtension.class)
class ClubServicesTest {

    // 1. Mockeamos el repositorio (SIN BD REAL)
    @Mock
    private ClubRepository clubRepository;

    // 2. Inyectamos ese repositorio falso en nuestro servicio real
    @InjectMocks
    private ClubServices clubServices;

    @Test
    @DisplayName("Debe retornar una lista de clubes cuando existen registros en la BD")
    void shouldReturnClubListWhenClubsExist() {

        // --- GIVEN (Arrange) ---
        // Creamos clubes de mentira en la memoria
        Club club1 = new Club(1L, "Colo Colo", "Primera División", "contacto@colocolo.cl");
        Club club2 = new Club(2L, "Universidad de Chile", "Primera División", "contacto@udechile.cl");

        // Le decimos a Mockito: "Cuando el servicio llame a findAll(), devuélvele esta lista falsa"
        when(clubRepository.findAll()).thenReturn(Arrays.asList(club1, club2));

        // --- WHEN (Act) ---
        // Ejecutamos el método real de nuestro servicio
        List<ClubDTO> resultado = clubServices.obtenerTodos(); // <- Ajusta este nombre si es necesario

        // --- THEN (Assert) ---
        // Verificamos los resultados
        assertNotNull(resultado, "La lista no debería ser nula");
        assertEquals(2, resultado.size(), "Deberían retornar exactamente 2 clubes");
        assertEquals("Colo Colo", resultado.get(0).getNombre());

        // Verificamos que el repositorio efectivamente fue consultado 1 vez
        verify(clubRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe lanzar una excepción cuando se busca un club por un ID que no existe")
    void shouldThrowExceptionWhenClubNotFound() {

        // --- GIVEN (Arrange) ---
        Long idInexistente = 99L;

        // Le decimos a Mockito: "Cuando el servicio llame a findById(99), devuélvele un Optional vacío"
        when(clubRepository.findById(idInexistente)).thenReturn(java.util.Optional.empty());

        // --- WHEN & THEN (Act & Assert) ---
        // En JUnit 5, cuando esperamos que el código falle y lance una excepción (ej: RuntimeException o una tuya propia como ClubNotFoundException),
        // encapsulamos la ejecución (Act) dentro de la aserción (Assert) usando assertThrows.

        Exception exception = assertThrows(RuntimeException.class, () -> {
            clubServices.buscarPorId(idInexistente); // <- Ajusta el nombre de tu método y la excepción que lanza (ej: EntityNotFoundException.class)
        });

        // Opcional: Verificamos que el mensaje de error sea el correcto
        // assertTrue(exception.getMessage().contains("no encontrado"));

        // Verificamos que el repositorio efectivamente fue consultado 1 vez
        verify(clubRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Debe retornar un ClubDTO cuando se busca por un ID que existe")
    void shouldReturnClubDTOWhenIdExists() {

        // --- GIVEN ---
        Club club = new Club(1L, "Colo Colo", "Primera División", "contacto@colocolo.cl");
        when(clubRepository.findById(1L)).thenReturn(java.util.Optional.of(club));

        // --- WHEN ---
        ClubDTO resultado = clubServices.buscarPorId(1L);

        // --- THEN ---
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Colo Colo", resultado.getNombre());
        assertEquals("Primera División", resultado.getDivision());
        verify(clubRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe guardar y retornar un ClubDTO con el ID asignado")
    void shouldSaveAndReturnClubDTO() {

        // --- GIVEN ---
        ClubDTO dto = new ClubDTO(null, "Audax Italiano", "Primera División", "contacto@audax.cl");
        Club clubGuardado = new Club(3L, "Audax Italiano", "Primera División", "contacto@audax.cl");
        when(clubRepository.save(any(Club.class))).thenReturn(clubGuardado);

        // --- WHEN ---
        ClubDTO resultado = clubServices.guardarClub(dto);

        // --- THEN ---
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Audax Italiano", resultado.getNombre());
        verify(clubRepository, times(1)).save(any(Club.class));
    }

    @Test
    @DisplayName("Debe actualizar y retornar el ClubDTO con los nuevos datos")
    void shouldUpdateAndReturnClubDTO() {

        // --- GIVEN ---
        Club clubExistente = new Club(1L, "Colo Colo", "Primera División", "contacto@colocolo.cl");
        ClubDTO dto = new ClubDTO(1L, "Colo Colo Actualizado", "Segunda División", "nuevo@colocolo.cl");
        Club clubActualizado = new Club(1L, "Colo Colo Actualizado", "Segunda División", "nuevo@colocolo.cl");

        when(clubRepository.findById(1L)).thenReturn(java.util.Optional.of(clubExistente));
        when(clubRepository.save(any(Club.class))).thenReturn(clubActualizado);

        // --- WHEN ---
        ClubDTO resultado = clubServices.actualizarClub(1L, dto);

        // --- THEN ---
        assertNotNull(resultado);
        assertEquals("Colo Colo Actualizado", resultado.getNombre());
        assertEquals("Segunda División", resultado.getDivision());
        verify(clubRepository, times(1)).findById(1L);
        verify(clubRepository, times(1)).save(any(Club.class));
    }

    @Test
    @DisplayName("Debe eliminar el club cuando el ID existe")
    void shouldDeleteClubWhenIdExists() {

        // --- GIVEN ---
        when(clubRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clubRepository).deleteById(1L);

        // --- WHEN ---
        assertDoesNotThrow(() -> clubServices.eliminarClub(1L));

        // --- THEN ---
        verify(clubRepository, times(1)).existsById(1L);
        verify(clubRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar eliminar un club con ID que no existe")
    void shouldThrowWhenDeletingNonExistentClub() {

        // --- GIVEN ---
        when(clubRepository.existsById(99L)).thenReturn(false);

        // --- WHEN & THEN ---
        assertThrows(RuntimeException.class, () -> clubServices.eliminarClub(99L));
        verify(clubRepository, never()).deleteById(any());
    }
}