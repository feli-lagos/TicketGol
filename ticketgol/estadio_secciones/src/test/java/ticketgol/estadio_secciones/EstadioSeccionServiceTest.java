package ticketgol.estadio_secciones;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadioSeccionServiceTest {

    @InjectMocks
    private EstadioSeccionService estadioSeccionService;

    @Mock
    private EstadioSeccionRepository seccionRepository;

    @Mock
    private RestTemplate restTemplate;

    private EstadioSeccion seccionMock;
    private EstadioDTO estadioDtoMock;


    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(
                estadioSeccionService,
                "restTemplate",
                restTemplate
        );


        seccionMock = new EstadioSeccion();

        seccionMock.setId(1L);
        seccionMock.setEstadioId(10L);
        seccionMock.setNombre("Tribuna Norte");
        seccionMock.setCantidadAsientos(5000);


        estadioDtoMock =
                new EstadioDTO(
                        10L,
                        "Estadio Nacional",
                        "Santiago",
                        45000,
                        "Av. Grecia 2001"
                );
    }


    @Test
    void shouldReturnAllSections() {

        when(seccionRepository.findAll())
                .thenReturn(Arrays.asList(seccionMock));


        List<EstadioSeccion> resultado =
                estadioSeccionService.findAll();


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }


    @Test
    void shouldReturnSectionById() {

        when(seccionRepository.findById(1L))
                .thenReturn(Optional.of(seccionMock));


        EstadioSeccion resultado =
                estadioSeccionService.findById(1L);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }


    @Test
    void shouldThrowExceptionWhenSectionNotFound() {

        when(seccionRepository.findById(99L))
                .thenReturn(Optional.empty());


        assertThrows(
                EstadioSeccionNotFoundException.class,
                () -> estadioSeccionService.findById(99L)
        );
    }


    @Test
    void shouldFindByStadiumId() {

        when(seccionRepository.findByEstadioId(10L))
                .thenReturn(Arrays.asList(seccionMock));


        List<EstadioSeccion> resultado =
                estadioSeccionService.findByEstadioId(10L);


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(
                10L,
                resultado.get(0).getEstadioId()
        );
    }


    @Test
    void shouldSaveSectionSuccessfully() {

        when(restTemplate.getForObject(
                anyString(),
                eq(EstadioDTO.class)
        ))
                .thenReturn(estadioDtoMock);


        when(seccionRepository.save(any(EstadioSeccion.class)))
                .thenReturn(seccionMock);


        EstadioSeccion resultado =
                estadioSeccionService.save(seccionMock);


        assertNotNull(resultado);
        assertEquals(
                "Tribuna Norte",
                resultado.getNombre()
        );
    }


    @Test
    void shouldThrowExceptionWhenStadiumDoesNotExist() {


        when(restTemplate.getForObject(
                anyString(),
                eq(EstadioDTO.class)
        ))
                .thenThrow(
                        new HttpClientErrorException(HttpStatus.NOT_FOUND)
                );


        assertThrows(
                RuntimeException.class,
                () -> estadioSeccionService.save(seccionMock)
        );
    }


    @Test
    void shouldUpdateSectionSuccessfully() {


        when(seccionRepository.findById(1L))
                .thenReturn(Optional.of(seccionMock));


        when(restTemplate.getForObject(
                anyString(),
                eq(EstadioDTO.class)
        ))
                .thenReturn(estadioDtoMock);


        when(seccionRepository.save(any(EstadioSeccion.class)))
                .thenReturn(seccionMock);



        EstadioSeccion resultado =
                estadioSeccionService.update(
                        1L,
                        seccionMock
                );


        assertNotNull(resultado);
        assertEquals(
                1L,
                resultado.getId()
        );
    }


    @Test
    void shouldDeleteSectionSuccessfully() {


        when(seccionRepository.existsById(1L))
                .thenReturn(true);


        doNothing()
                .when(seccionRepository)
                .deleteById(1L);



        assertDoesNotThrow(
                () -> estadioSeccionService.delete(1L)
        );
    }


    @Test
    void shouldThrowExceptionWhenDeletingNonExistingSection() {


        when(seccionRepository.existsById(99L))
                .thenReturn(false);


        assertThrows(
                EstadioSeccionNotFoundException.class,
                () -> estadioSeccionService.delete(99L)
        );
    }

}