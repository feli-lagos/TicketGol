package ticketgol.estadios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ticketgol.estadios.model.Estadio;
import ticketgol.estadios.repository.EstadioRepository;
import ticketgol.estadios.service.EstadioService;
import ticketgol.estadios.exception.EstadioNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
public class EstadioServiceTest {

    @Autowired
    private EstadioService estadioService;

    @MockitoBean
    private EstadioRepository estadioRepository;

    private Estadio stadiumMock;

    @BeforeEach
    public void setUp() {
        stadiumMock = new Estadio();
        stadiumMock.setId(1L);
        stadiumMock.setNombre("National Stadium");
        stadiumMock.setCiudad("Santiago");
        stadiumMock.setCapacidad(45000);
        stadiumMock.setDireccion("Grecia Avenue 2001");
    }

    @Test
    public void testFindAll() {
        Mockito.when(estadioRepository.findAll()).thenReturn(Arrays.asList(stadiumMock));

        List<Estadio> result = estadioService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindById_Success() {
        Mockito.when(estadioRepository.findById(1L)).thenReturn(Optional.of(stadiumMock));

        Estadio result = estadioService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindById_NotFound() {
        Mockito.when(estadioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EstadioNotFoundException.class, () -> {
            estadioService.findById(99L);
        });
    }

    @Test
    public void testSave_Success() {
        Mockito.when(estadioRepository.save(any(Estadio.class))).thenReturn(stadiumMock);

        Estadio result = estadioService.save(stadiumMock);

        assertNotNull(result);
        assertEquals("Santiago", result.getCiudad());
    }

    @Test
    public void testUpdate_Success() {
        Mockito.when(estadioRepository.findById(1L)).thenReturn(Optional.of(stadiumMock));
        Mockito.when(estadioRepository.save(any(Estadio.class))).thenReturn(stadiumMock);

        Estadio result = estadioService.update(1L, stadiumMock);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testDelete_Success() {
        Mockito.when(estadioRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(estadioRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            estadioService.delete(1L);
        });
    }

    @Test
    public void testDelete_NotFound() {
        Mockito.when(estadioRepository.existsById(99L)).thenReturn(false);

        assertThrows(EstadioNotFoundException.class, () -> {
            estadioService.delete(99L);
        });
    }
}