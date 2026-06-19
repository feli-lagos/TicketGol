package ticketgol.merch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ticketgol.merch.dto.MerchandiseRequestDTO;
import ticketgol.merch.model.Merchandise;
import ticketgol.merch.repository.MerchandiseRepository;

import java.util.List;

@Slf4j
@Service
public class MerchandiseService {

    private final MerchandiseRepository repository;
    private final RestTemplate restTemplate;

    public MerchandiseService(MerchandiseRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Merchandise guardarArticulo(MerchandiseRequestDTO dto) {
        log.info("Iniciando registro de artículo de merchandise para el Club ID: {}", dto.clubId());

        // Validación de la red hacia el microservicio de clubes
        validarClubExistente(dto.clubId());

        //Si el club existe, transformamos el DTO a Entidad
        Merchandise articulo = new Merchandise();
        articulo.setNombre(dto.nombre());
        articulo.setDescripcion(dto.descripcion());
        articulo.setPrecio(dto.precio());
        articulo.setStock(dto.stock());
        articulo.setImagenUrl(dto.imagenUrl());
        articulo.setClubId(dto.clubId());

        // Se guarda en la base de datos de merchandise
        Merchandise articuloGuardado = repository.save(articulo);
        log.info("Artículo de merchandise guardado exitosamente con ID: {}", articuloGuardado.getId());

        return articuloGuardado;
    }

    public List<Merchandise> obtenerTodosLosArticulos() {
        log.info("Consultando todos los artículos de merchandise en la base de datos");
        return repository.findAll();
    }

    private void validarClubExistente(Long clubId) {
        // puerto 8110
        String urlClubes = "http://localhost:8110/api/clubes/" + clubId;

        try {
            log.info("Verificando existencia del club ID: {} mediante llamada síncrona al puerto 8110", clubId);
            restTemplate.getForEntity(urlClubes, Object.class);
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Validación fallida: El microservicio de Clubes confirmó que el ID {} no existe.", clubId);
            throw new RuntimeException("El club con ID " + clubId + " no existe en el sistema.");
        } catch (ResourceAccessException ex) {
            log.error("Error de red: El microservicio de Clubes (puerto 8110) se encuentra fuera de línea o inaccesible.");
            throw new RuntimeException("Error de conexión: El microservicio de Clubes (Puerto 8110) no está respondiendo.");
        }
    }

    public Merchandise getOrdenById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
    }
}