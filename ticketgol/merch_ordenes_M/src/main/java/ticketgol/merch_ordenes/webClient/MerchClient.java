package ticketgol.merch_ordenes.webClient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MerchClient {

    private final RestClient restClient;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public MerchClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8111/api/merchandise")
                .build();
    }

    public Map<String, Object> getMerchandise(Long id) {
        try {
            return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                return restClient.get()
                        .uri("/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                            // 1. Imprimimos el código de estado
                            System.out.println("--- ERROR DETECTADO ---");
                            System.out.println("Status Code: " + res.getStatusCode());

                            // 2. Intentamos imprimir el cuerpo del error
                            try {
                                String errorBody = new String(res.getBody().readAllBytes());
                                System.out.println("Cuerpo del error: " + errorBody);
                            } catch (Exception e) {
                                System.out.println("No se pudo leer el cuerpo del error");
                            }

                            throw new RuntimeException("Merchandise no encontrado");
                        })
                        .body(new ParameterizedTypeReference<Map<String, Object>>() {});
            }, executor).join();
        } catch (Exception e) {
            // Esto captura la RuntimeException lanzada arriba
            throw new RuntimeException("Error en la llamada al servicio: " + e.getMessage());
        }
    }
}