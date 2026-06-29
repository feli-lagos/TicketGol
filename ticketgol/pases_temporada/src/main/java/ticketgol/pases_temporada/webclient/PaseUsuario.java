package ticketgol.pases_temporada.webclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ticketgol.pases_temporada.exception.UserNotFoundException;
import ticketgol.pases_temporada.mapper.UsuarioMapper;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;

import java.util.Map;

@Component
public class PaseUsuario {

    @Autowired
    private UsuarioMapper usuarioMapper;

    Logger log = LoggerFactory.getLogger(PaseUsuario.class);
    private final WebClient webClient;

    public PaseUsuario(@Value("${usuario-service.url}") String clienteServidor,
                       WebClient.Builder webClientBuilder,
                       UsuarioMapper usuarioMapper) {
        this.webClient = webClientBuilder.baseUrl(clienteServidor).build();
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioEstadoDto getUsuarioDtoById(Long id) {
        try {
            return webClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            Mono.error(new RuntimeException("Error en servicio de usuarios")))
                    .bodyToMono(UsuarioEstadoDto.class) // Asegúrate que el DTO sea el correcto
                    .block();
        } catch (Exception e) {
            log.error("Fallo crítico al contactar Usuarios para ID {}: {}", id, e.getMessage());
            // Aquí podrías retornar un DTO vacío o lanzar una excepción personalizada más limpia
            throw new UserNotFoundException("No se pudo completar la solicitud de pase temporal");
        }
    }
}
