package ticketgol.pases_temporada.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ticketgol.pases_temporada.mapper.UsuarioMapper;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;

import java.util.Map;

@Component
public class PaseUsuario {

    @Autowired
    private UsuarioMapper usuarioMapper;

    private final WebClient webClient;
    private final WebClient.Builder webClientBuilder;


    public PaseUsuario(@Value("${usuario-service.url}") String clienteServidor, WebClient.Builder webClientBuilder, UsuarioMapper usuarioMapper){
        this.webClient = WebClient.builder().baseUrl(clienteServidor).build();
        this.webClientBuilder = webClientBuilder;
        this.usuarioMapper = usuarioMapper;
    }

    public Map<String, Object> getUsuarioById(Long id){
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(),
                clientResponse -> clientResponse.bodyToMono(String.class)
                .map(body -> new RuntimeException("cliente no encontrado"))).bodyToMono(Map.class).block();
    }

    public UsuarioEstadoDto getUsuarioDtoById(Long id){
        Map<String, Object> respuestaUsuario = webClientBuilder.build().get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(),
                        clientResponse -> clientResponse.bodyToMono(UsuarioEstadoDto.class)
                                .map(body -> new RuntimeException("cliente no encontrado"))).bodyToMono(Map.class)
                .block();
        return usuarioMapper.toDto(respuestaUsuario);
    }
}
