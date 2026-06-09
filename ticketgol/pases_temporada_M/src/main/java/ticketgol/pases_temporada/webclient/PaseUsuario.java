package ticketgol.pases_temporada.webclient;

<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ticketgol.pases_temporada.mapper.UsuarioMapper;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;

=======
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
>>>>>>> main
import java.util.Map;

@Component
public class PaseUsuario {

<<<<<<< HEAD
    @Autowired
    private UsuarioMapper usuarioMapper;

    private final WebClient webClient;
    private final WebClient.Builder webClientBuilder;


    public PaseUsuario(@Value("${sancionado-service.url}") String clienteServidor, WebClient.Builder webClientBuilder){
=======
    private final WebClient webClient;

    public PaseUsuario(@Value("${usuario.service.url}") String clienteServidor) {
>>>>>>> main
        this.webClient = WebClient.builder().baseUrl(clienteServidor).build();
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Map<String, Object>> getUsuarioByRut(String rut) {
        return this.webClient.get()
                .uri("/rut/{rut}", rut)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
<<<<<<< HEAD

    public UsuarioEstadoDto getUsuarioDtoById(Long id){
        Map<String, Object> respuestaUsuario = webClientBuilder.build().get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> new RuntimeException("cliente no encontrado"))).bodyToMono(Map.class)
                .block();
        return usuarioMapper.toDto(respuestaUsuario);
    }
}
=======
}
>>>>>>> main
