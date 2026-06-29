package ticketgol.compras.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import ticketgol.compras.Model.Compras;
import ticketgol.compras.Repository.ComprasRepository;
import ticketgol.compras.Exception.ComprasNotFoundException;
import java.util.List;

@Slf4j
@Service
public class ComprasService {

    private final ComprasRepository repositorio;
    private final WebClient webClient;

    public ComprasService(ComprasRepository repositorio, WebClient.Builder webClientBuilder) {
        this.repositorio = repositorio;
        this.webClient = webClientBuilder.build();
    }

    public List<Compras> obtenerTodos() {
        log.info("Solicitando todas las compras.");
        return repositorio.findAll();
    }

    public Compras obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ComprasNotFoundException("La compra con ID " + id + " no existe."));
    }

    public List<Compras> obtenerPorUsuario(String usuarioId) {
        return repositorio.findByUsuarioId(usuarioId);
    }

    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ComprasNotFoundException("La compra con ID " + id + " no existe.");
        }
        repositorio.deleteById(id);
    }

    public Compras guardar(Compras compras) {
        log.info("Iniciando validación para usuario: {}", compras.getUsuarioId());


        Boolean existeUsuario = webClient.get()
                .uri("http://localhost:8087/api/v1/usuarios/" + compras.getUsuarioId())
                .retrieve().toBodilessEntity().map(r -> r.getStatusCode().is2xxSuccessful())
                .onErrorReturn(false).block();

        if (existeUsuario == null || !existeUsuario) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario inexistente.");
        }

        Boolean estaSancionado = webClient.get()
                .uri("http://localhost:8085/api/v1/usuarios-sancionados/" + compras.getUsuarioId())
                .retrieve().toBodilessEntity().map(r -> r.getStatusCode().is2xxSuccessful())
                .onErrorReturn(false).block();

        if (estaSancionado != null && estaSancionado) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario sancionado. Compra denegada.");
        }

        Boolean existeTicket = webClient.get()
                .uri("http://localhost:8089/api/v1/tickets/" + compras.getTicketId())
                .retrieve().toBodilessEntity().map(r -> r.getStatusCode().is2xxSuccessful())
                .onErrorReturn(false).block();

        if (existeTicket == null || !existeTicket) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket inválido.");
        }

        return repositorio.save(compras);
    }
}