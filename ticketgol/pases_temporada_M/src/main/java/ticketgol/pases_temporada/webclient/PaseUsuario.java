package ticketgol.pases_temporada.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Component
public class PaseUsuario {
    private final WebClient webClient;
    public PaseUsuario(@Value("${sancionado-service.url}") String clienteServidor){
        this.webClient = WebClient.builder().baseUrl(clienteServidor).build();
    }

    public Map<String, Object> getUsuarioById(Long id){
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(),
                clientResponse -> clientResponse.bodyToMono(String.class)
                .map(body -> new RuntimeException("cliente no encontrado"))).bodyToMono(Map.class).block();
    }
}
