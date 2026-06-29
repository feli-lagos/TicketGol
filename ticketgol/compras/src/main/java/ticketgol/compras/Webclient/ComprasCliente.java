package ticketgol.compras.Webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ticketgol.compras.Dto.UsuarioEstadoDto;

@Component
public class ComprasCliente {

    private final WebClient webClient;

    public ComprasCliente(WebClient.Builder webClientBuilder) {
        // Enlazado al puerto real 8082 del servicio de usuarios
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    }


    public Mono<UsuarioEstadoDto> getUsuarioDtoById(String usuarioId) {
        return this.webClient.get()
                .uri("/api/v1/usuarios/{id}", usuarioId)
                .retrieve()
                .bodyToMono(UsuarioEstadoDto.class);
    }
}