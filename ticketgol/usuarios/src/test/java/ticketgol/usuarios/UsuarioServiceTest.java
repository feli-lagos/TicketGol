package ticketgol.usuarios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {

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
    @DisplayName("Debe retornar todos los usuarios cuando existen registros")
    void shouldReturnAllUsers() {


        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioMock));

        when(restTemplate.getForEntity(anyString(), eq(UsuarioSancionadoDTO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));


        List<Usuario> resultado = usuarioService.findAll();


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombreVisible());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar un usuario sancionado cuando existe una sanción vigente")
    void shouldReturnSanctionedUser() {

        // GIVEN
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        UsuarioSancionadoDTO dto = new UsuarioSancionadoDTO();
        dto.setFechaExpiracion(LocalDate.now().plusDays(5));

        when(restTemplate.getForEntity(anyString(), eq(UsuarioSancionadoDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));

        // WHEN
        Usuario resultado = usuarioService.findById(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals("SANCIONADO", resultado.getEstadoSancion());

        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el usuario no existe")
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // GIVEN
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResponseStatusException.class,
                () -> usuarioService.findById(99L));

        verify(usuarioRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Debe guardar un usuario cuando no existe otro con el mismo rut o correo")
    void shouldSaveUserSuccessfully() {

        // GIVEN
        when(usuarioRepository.existsByRut(usuarioMock.getRut())).thenReturn(false);
        when(usuarioRepository.existsByCorreo(usuarioMock.getCorreo())).thenReturn(false);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        when(restTemplate.getForEntity(anyString(), eq(UsuarioSancionadoDTO.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // WHEN
        Usuario resultado = usuarioService.save(usuarioMock);

        // THEN
        assertNotNull(resultado);
        assertEquals(usuarioMock.getCorreo(), resultado.getCorreo());

        verify(usuarioRepository, times(1)).existsByRut(usuarioMock.getRut());
        verify(usuarioRepository, times(1)).existsByCorreo(usuarioMock.getCorreo());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}