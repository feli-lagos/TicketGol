package ticketgol.tickets.webClient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TicketUsuario {

    private final RestClient restClient;

    // le puse este bloqueo de workers para que no colapse
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public TicketUsuario() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080/api/v1/usuarios")
                .build();
    }

    public Map<String, Object> getUsuario(Long id) {
        try {
            return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                return restClient.get()
                        .uri("/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            throw new RuntimeException("Usuario no encontrado en servicio externo");
                        })
                        .body(new ParameterizedTypeReference<Map<String, Object>>() {});
            }, executor).join();
        } catch (Exception e) {
            throw new RuntimeException("Error en la llamada al servicio: " + e.getMessage());
        }
    }
}