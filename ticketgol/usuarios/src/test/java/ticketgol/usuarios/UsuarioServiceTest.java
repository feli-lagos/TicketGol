package ticketgol.usuarios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ticketgol.usuarios.dto.UsuarioSancionadoDTO;
import ticketgol.usuarios.model.Usuario;
import ticketgol.usuarios.repository.UsuarioRepository;
import ticketgol.usuarios.service.UsuarioService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private RestTemplate restTemplate;

    private Usuario usuarioMock;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(usuarioService, "restTemplate", restTemplate);

        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setRut("12345678-9");
        usuarioMock.setCorreo("juan.perez@duocuc.cl");
        usuarioMock.setNombreVisible("Juan Pérez");
        usuarioMock.setTelefono("+56912345678");
        usuarioMock.setHashClave("secure_password_hash");
    }

    @Test
    public void testFindAll() {
        Mockito.when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioMock));

        Mockito.when(restTemplate.getForEntity(anyString(), any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<Usuario> usuarios = usuarioService.findAll();

        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
    }

    @Test
    public void testFindById_Exitoso() {
        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        UsuarioSancionadoDTO sancionDTO = new UsuarioSancionadoDTO();
        sancionDTO.setFechaExpiracion(LocalDate.now().plusDays(5));

        Mockito.when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity(sancionDTO, HttpStatus.OK));

        Usuario encontrado = usuarioService.findById(1L);

        assertNotNull(encontrado);
        assertEquals("SANCIONADO", encontrado.getEstadoSancion());
    }

    @Test
    public void testFindById_NoEncontrado() {
        Mockito.when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            usuarioService.findById(99L);
        });
    }

    @Test
    public void testGuardar_Exitoso() {
        Mockito.when(usuarioRepository.existsByRut(usuarioMock.getRut())).thenReturn(false);
        Mockito.when(usuarioRepository.existsByCorreo(usuarioMock.getCorreo())).thenReturn(false);
        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);
        Mockito.when(restTemplate.getForEntity(anyString(), any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Usuario guardado = usuarioService.save(usuarioMock);

        assertNotNull(guardado);
        assertEquals("juan.perez@duocuc.cl", guardado.getCorreo());
    }
}