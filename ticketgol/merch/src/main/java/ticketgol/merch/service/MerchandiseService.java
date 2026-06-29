package ticketgol.merch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ticketgol.merch.dto.MerchandiseDTO;
import ticketgol.merch.exception.MerchandiseNotFoundException;
import ticketgol.merch.model.Merchandise;
import ticketgol.merch.repository.MerchandiseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MerchandiseService {

    private final MerchandiseRepository repository;
    private final RestTemplate restTemplate;

    public MerchandiseService(MerchandiseRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public List<MerchandiseDTO> obtenerTodosLosArticulos() {
        log.info("Consultando todos los artículos de merchandise en la base de datos");
        return repository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public MerchandiseDTO buscarPorId(Long id) {
        log.info("Buscando artículo de merchandise con ID: {}", id);
        Merchandise articulo = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error en búsqueda: No se encontró el artículo con ID {}", id);
                    return new MerchandiseNotFoundException("No se encontró el artículo con el ID: " + id);
                });
        return convertirADto(articulo);
    }

    public MerchandiseDTO guardarArticulo(MerchandiseDTO dto) {
        log.info("Iniciando registro de artículo de merchandise para el Club ID: {}", dto.getClubId());

        validarClubExistente(dto.getClubId());

        Merchandise articulo = convertirAEntidad(dto);
        Merchandise articuloGuardado = repository.save(articulo);

        log.info("Artículo de merchandise guardado exitosamente con ID: {}", articuloGuardado.getId());
        return convertirADto(articuloGuardado);
    }

    public MerchandiseDTO actualizarArticulo(Long id, MerchandiseDTO dto) {
        log.info("Intentando actualizar artículo de merchandise con ID: {}", id);

        Merchandise articuloExistente = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al actualizar: No se encontró el artículo con ID {}", id);
                    return new MerchandiseNotFoundException("No se encontró el artículo con el ID: " + id);
                });

        if (!articuloExistente.getClubId().equals(dto.getClubId())) {
            log.info("El Club ID fue modificado. Iniciando validación del nuevo club...");
            validarClubExistente(dto.getClubId());
        }

        articuloExistente.setNombre(dto.getNombre());
        articuloExistente.setDescripcion(dto.getDescripcion());
        articuloExistente.setPrecio(dto.getPrecio());
        articuloExistente.setStock(dto.getStock());
        articuloExistente.setImagenUrl(dto.getImagenUrl());
        articuloExistente.setClubId(dto.getClubId());

        Merchandise articuloGuardado = repository.save(articuloExistente);
        log.info("Artículo con ID: {} actualizado correctamente", id);

        return convertirADto(articuloGuardado);
    }

    public void eliminarArticulo(Long id) {
        log.info("Intentando eliminar artículo de merchandise con ID: {}", id);

        if (!repository.existsById(id)) {
            log.error("Error al eliminar: No se encontró el artículo con ID {}", id);
            throw new MerchandiseNotFoundException("No se encontró el artículo con el ID: " + id);
        }

        repository.deleteById(id);
        log.info("Artículo con ID: {} eliminado exitosamente de la base de datos", id);
    }

    private void validarClubExistente(Long clubId) {
        String urlClubes = "http://localhost:8110/api/clubes/" + clubId;

        try {
            log.info("Verificando existencia del club ID: {} mediante llamada síncrona al puerto 8110", clubId);
            restTemplate.getForEntity(urlClubes, Object.class);
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Validación fallida: El microservicio de Clubes confirmó que el ID {} no existe.", clubId);
            throw new MerchandiseNotFoundException("El club con ID " + clubId + " no existe en el sistema.");
        } catch (ResourceAccessException ex) {
            log.error("Error de red: El microservicio de Clubes (puerto 8110) se encuentra fuera de línea.");
            throw new RuntimeException("Error de conexión: El microservicio de Clubes (Puerto 8110) no está respondiendo.");
        }
    }

    // --- MÉTODOS DE MAPEO ---

    private MerchandiseDTO convertirADto(Merchandise articulo) {
        return new MerchandiseDTO(
                articulo.getId(),
                articulo.getNombre(),
                articulo.getDescripcion(),
                articulo.getPrecio(),
                articulo.getStock(),
                articulo.getImagenUrl(),
                articulo.getClubId()
        );
    }

    private Merchandise convertirAEntidad(MerchandiseDTO dto) {
        Merchandise articulo = new Merchandise();
        articulo.setId(dto.getId());
        articulo.setNombre(dto.getNombre());
        articulo.setDescripcion(dto.getDescripcion());
        articulo.setPrecio(dto.getPrecio());
        articulo.setStock(dto.getStock());
        articulo.setImagenUrl(dto.getImagenUrl());
        articulo.setClubId(dto.getClubId());
        return articulo;
    }
}