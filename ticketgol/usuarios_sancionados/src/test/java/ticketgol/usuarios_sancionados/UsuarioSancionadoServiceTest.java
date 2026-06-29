package ticketgol.usuarios_sancionados;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioSancionadoServiceTest {

    @Autowired
    private UsuarioSancionadoService usuarioSancionadoService;

    @MockitoBean
    private UsuarioSancionadoRepository usuarioSancionadoRepository;

    private UsuarioSancionado sancionMock;

    @BeforeEach
    public void setUp() {
        WebClient webClientMock = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        UsuarioDTO usuarioDTOMock = Mockito.mock(UsuarioDTO.class);

        Mockito.doReturn(requestHeadersUriSpec).when(webClientMock).get();
        Mockito.doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
        Mockito.doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        Mockito.doReturn(responseSpec).when(responseSpec).onStatus(any(), any());
        Mockito.doReturn(Mono.just(usuarioDTOMock)).when(responseSpec).bodyToMono(UsuarioDTO.class);

        ReflectionTestUtils.setField(usuarioSancionadoService, "webClient", webClientMock);

        sancionMock = new UsuarioSancionado();
        sancionMock.setId(1L);
        sancionMock.setRut("11111111-1");
        sancionMock.setMotivo("Falta Grave");
        sancionMock.setFechaSancion(LocalDate.now());
        sancionMock.setFechaExpiracion(LocalDate.now().plusDays(5));
    }

    @Test
    public void testFindAll() {
        Mockito.when(usuarioSancionadoRepository.findAll()).thenReturn(Arrays.asList(sancionMock));

        List<UsuarioSancionado> resultado = usuarioSancionadoService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Falta Grave", resultado.get(0).getMotivo());
    }

    @Test
    public void testFindById_Success() {
        Mockito.when(usuarioSancionadoRepository.findById(1L)).thenReturn(Optional.of(sancionMock));

        UsuarioSancionado encontrado = usuarioSancionadoService.findById(1L);

        assertNotNull(encontrado);
        assertEquals("11111111-1", encontrado.getRut());
    }

    @Test
    public void testFindById_NotFound() {
        Mockito.when(usuarioSancionadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioSancionadoNotFoundException.class, () -> {
            usuarioSancionadoService.findById(99L);
        });
    }

    @Test
    public void testSave_Success() {
        Mockito.when(usuarioSancionadoRepository.existsByRut(sancionMock.getRut())).thenReturn(false);
        Mockito.when(usuarioSancionadoRepository.save(any(UsuarioSancionado.class))).thenReturn(sancionMock);

        UsuarioSancionado guardado = usuarioSancionadoService.save(sancionMock);

        assertNotNull(guardado);
        assertEquals("11111111-1", guardado.getRut());
    }

    @Test
    public void testSave_AlreadyExists_ThrowsException() {
        Mockito.when(usuarioSancionadoRepository.existsByRut(sancionMock.getRut())).thenReturn(true);

        assertThrows(UsuarioAlreadyExistsException.class, () -> {
            usuarioSancionadoService.save(sancionMock);
        });
    }
}