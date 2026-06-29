package ticketgol.merch_ordenes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketgol.merch_ordenes.model.MerchOrden;
import ticketgol.merch_ordenes.repository.MerchRepository;
import ticketgol.merch_ordenes.webClient.MerchClient;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MerchOrdenService {

    @Autowired
    private MerchRepository repository;

    @Autowired
    private MerchClient merchandiseClient;

    // READ ALL
    public List<MerchOrden> findAllOrdenes() {
        return repository.findAll();
    }

    // READ BY ID
    public MerchOrden getOrdenById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
    }

    // CREATE (Con validación de Merchandise)
    public MerchOrden crearOrden(MerchOrden orden) {
        // Validamos que el producto exista
        Map<String, Object> producto = merchandiseClient.getMerchandise(orden.getMerchandiseId());

        if (producto == null || producto.isEmpty()) {
            throw new RuntimeException("No se puede crear la orden: El merchandise con ID " + orden.getMerchandiseId() + " no existe.");
        }

        // Calculamos el total (lógica de negocio simple)
        orden.setPrecioTotal(orden.getCantidad() * orden.getPrecioUnitario());

        return repository.save(orden);
    }

    // UPDATE
    public MerchOrden updateOrden(Long id, MerchOrden ordenNueva) {
        MerchOrden ordenExistente = getOrdenById(id);

        ordenExistente.setCantidad(ordenNueva.getCantidad());
        ordenExistente.setPrecioUnitario(ordenNueva.getPrecioUnitario());
        ordenExistente.setPrecioTotal(ordenNueva.getCantidad() * ordenNueva.getPrecioUnitario());

        return repository.save(ordenExistente);
    }

    // DELETE
    public void deleteOrden(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, orden no encontrada con ID: " + id);
        }
        repository.deleteById(id);
    }
}