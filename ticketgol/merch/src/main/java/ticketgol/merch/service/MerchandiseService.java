package ticketgol.merch.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ticketgol.merch.dto.MerchandiseRequestDTO;
import ticketgol.merch.model.Merchandise;
import ticketgol.merch.repository.MerchandiseRepository;

@Service
public class MerchandiseService {

    private final MerchandiseRepository repository;
    private final RestTemplate restTemplate;

    public MerchandiseService(MerchandiseRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Merchandise guardarArticulo(MerchandiseRequestDTO dto) {
        // 1. Validamos cruzando la red hacia el microservicio de clubes
        validarClubExistente(dto.clubId());

        // 2. Si el club existe, transformamos el DTO a Entidad
        Merchandise articulo = new Merchandise();
        articulo.setNombre(dto.nombre());
        articulo.setDescripcion(dto.descripcion());
        articulo.setPrecio(dto.precio());
        articulo.setStock(dto.stock());
        articulo.setImagenUrl(dto.imagenUrl());
        articulo.setClubId(dto.clubId());

        // 3. Guardamos en la base de datos de merchandise
        return repository.save(articulo);
    }

    private void validarClubExistente(Long clubId) {
        // Apuntamos directo al puerto 8110
        String urlClubes = "http://localhost:8110/api/clubes/" + clubId;

        try {
            restTemplate.getForEntity(urlClubes, Object.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new RuntimeException("El club con ID " + clubId + " no existe en el sistema.");
        }
    }
}