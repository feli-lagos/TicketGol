package ticketgol.estadio_secciones.dto;

// el DTO es traer los datos de la dependencia,
// para poder hacerlo depender

public record EstadioDTO(Long id, String nombre,
                         String ciudad, Integer capacidad,
                         String direccion) {}