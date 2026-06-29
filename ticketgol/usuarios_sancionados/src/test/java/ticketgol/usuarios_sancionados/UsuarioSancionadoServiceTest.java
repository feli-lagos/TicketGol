package ticketgol.usuarios_sancionados;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import ticketgol.usuarios_sancionados.dto.UsuarioDTO;
import ticketgol.usuarios_sancionados.exception.UsuarioAlreadyExistsException;
import ticketgol.usuarios_sancionados.exception.UsuarioSancionadoNotFoundException;
import ticketgol.usuarios_sancionados.model.UsuarioSancionado;
import ticketgol.usuarios_sancionados.repository.UsuarioSancionadoRepository;
import ticketgol.usuarios_sancionados.service.UsuarioSancionadoService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioSancionadoServiceTest {

    @Mock
    private UsuarioSancionadoRepository usuarioSancionadoRepository;

    @InjectMocks
    private UsuarioSancionadoService usuarioSancionadoService;

    private UsuarioSancionado sancionMock;

    @BeforeEach
    void setUp() {

        sancionMock = new UsuarioSancionado();
        sancionMock.setId(1L);
        sancionMock.setRut("11111111-1");
        sancionMock.setMotivo("Falta Grave");
        sancionMock.setFechaSancion(LocalDate.now());
        sancionMock.setFechaExpiracion(LocalDate.now().plusDays(5));
    }

    private void mockWebClient() {

        WebClient webClientMock = Mockito.mock(WebClient.class);

        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec =
                Mockito.mock(WebClient.RequestHeadersUriSpec.class);

        WebClient.RequestHeadersSpec<?> requestHeadersSpec =
                Mockito.mock(WebClient.RequestHeadersSpec.class);

        WebClient.ResponseSpec responseSpec =
                Mockito.mock(WebClient.ResponseSpec.class);

        UsuarioDTO usuarioDTO =
                new UsuarioDTO(1L, "11111111-1", "correo@test.cl", "Juan");

        doReturn(requestHeadersUriSpec).when(webClientMock).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        doReturn(Mono.just(usuarioDTO)).when(responseSpec).bodyToMono(UsuarioDTO.class);

        ReflectionTestUtils.setField(usuarioSancionadoService, "webClient", webClientMock);
    }

    @Test
    @DisplayName("Debe retornar todas las sanciones cuando existen registros")
    void shouldReturnAllSanctions() {

        when(usuarioSancionadoRepository.findAll()).thenReturn(Arrays.asList(sancionMock));

        List<UsuarioSancionado> resultado = usuarioSancionadoService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Falta Grave", resultado.get(0).getMotivo());

        verify(usuarioSancionadoRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar una sanción cuando el ID existe")
    void shouldReturnSanctionWhenIdExists() {

        when(usuarioSancionadoRepository.findById(1L)).thenReturn(Optional.of(sancionMock));

        UsuarioSancionado resultado = usuarioSancionadoService.findById(1L);

        assertNotNull(resultado);
        assertEquals("11111111-1", resultado.getRut());

        verify(usuarioSancionadoRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la sanción no existe")
    void shouldThrowExceptionWhenSanctionNotFound() {

        when(usuarioSancionadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                UsuarioSancionadoNotFoundException.class,
                () -> usuarioSancionadoService.findById(99L)
        );

        verify(usuarioSancionadoRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar una sanción cuando el usuario no tiene una sanción registrada")
    void shouldSaveSanctionSuccessfully() {

        mockWebClient();

        when(usuarioSancionadoRepository.existsByRut(sancionMock.getRut())).thenReturn(false);
        when(usuarioSancionadoRepository.save(any(UsuarioSancionado.class))).thenReturn(sancionMock);

        UsuarioSancionado resultado = usuarioSancionadoService.save(sancionMock);

        assertNotNull(resultado);
        assertEquals("11111111-1", resultado.getRut());

        verify(usuarioSancionadoRepository).existsByRut(sancionMock.getRut());
        verify(usuarioSancionadoRepository).save(any(UsuarioSancionado.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando ya existe una sanción para el RUT")
    void shouldThrowExceptionWhenSanctionAlreadyExists() {

        mockWebClient();

        when(usuarioSancionadoRepository.existsByRut(sancionMock.getRut())).thenReturn(true);

        assertThrows(
                UsuarioAlreadyExistsException.class,
                () -> usuarioSancionadoService.save(sancionMock)
        );

        verify(usuarioSancionadoRepository).existsByRut(sancionMock.getRut());
        verify(usuarioSancionadoRepository, never()).save(any(UsuarioSancionado.class));
    }
}