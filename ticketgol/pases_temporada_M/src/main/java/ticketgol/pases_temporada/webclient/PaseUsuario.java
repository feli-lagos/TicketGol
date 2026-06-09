package ticketgol.pases_temporada.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Component
public class PaseUsuario {

    private final WebClient webClient;

    public PaseUsuario(@Value("${usuario.service.url}") String clienteServidor) {
        this.webClient = WebClient.builder().baseUrl(clienteServidor).build();
    }

    public Mono<Map<String, Object>> getUsuarioByRut(String rut) {
        return this.webClient.get()
                .uri("/rut/{rut}", rut)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}