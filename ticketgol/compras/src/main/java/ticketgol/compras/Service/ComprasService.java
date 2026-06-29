package ticketgol.compras.Service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ticketgol.compras.Exception.ComprasNotFoundException;
import ticketgol.compras.Model.Compras;
import ticketgol.compras.Dto.CompraRequestDTO;
import ticketgol.compras.Dto.CompraResponseDTO;
import ticketgol.compras.Dto.UsuarioEstadoDto;
import ticketgol.compras.Repository.ComprasRepository;
import ticketgol.compras.Webclient.ComprasCliente;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComprasService {

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private ComprasCliente comprasCliente;

    private static final Logger log = LoggerFactory.getLogger(ComprasService.class.getName());

    // ------------------------------------- MAPPER INTERNO --------------------------------------------
    private CompraResponseDTO convertToResponseDTO(Compras compra) {
        CompraResponseDTO response = new CompraResponseDTO();
        response.setId(compra.getId());
        response.setUsuarioId(compra.getUsuarioId());
        response.setTicketId(compra.getTicketId());
        response.setCodigoQr(compra.getCodigoQr());
        response.setFechaCompra(compra.getFechaCompra());
        response.setMetodoPago(compra.getMetodoPago());
        response.setEstado(compra.getEstado());
        return response;
    }

    // ------------------------------------- CRUD: READ ALL --------------------------------------------
    public List<CompraResponseDTO> findAllCompras() {
        log.info("Obteniendo el listado completo de compras.");
        return comprasRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ------------------------------------- CRUD: READ BY ID ------------------------------------------
    public CompraResponseDTO findCompraById(Long id) {
        log.info("Buscando compra con ID: {}", id);
        Compras compra = comprasRepository.findById(id)
                .orElseThrow(() -> new ComprasNotFoundException("La compra con id: " + id + " no fue encontrada"));
        return convertToResponseDTO(compra);
    }

    // ---------------------------- ADICIONAL: READ BY USUARIO ID ---------------------------------------
    public List<CompraResponseDTO> findComprasByUsuarioId(String usuarioId) {
        log.info("Buscando compras vinculadas al usuario ID: {}", usuarioId);
        return comprasRepository.findAll().stream()
                .filter(compra -> usuarioId.equals(compra.getUsuarioId()))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ------------------------------------- CRUD: CREATE (POST) ---------------------------------------
    public CompraResponseDTO saveCompra(CompraRequestDTO request) {
        log.info("Iniciando proceso de guardado de compra para el usuario: {}", request.getUsuarioId());


        UsuarioEstadoDto usuarioEstadoDto = comprasCliente.getUsuarioDtoById(request.getUsuarioId())
                .subscribeOn(Schedulers.boundedElastic())
                .block();


        if (usuarioEstadoDto != null && "SANCIONADO".equals(usuarioEstadoDto.getEstadoSancion())) {
            throw new RuntimeException("El usuario se encuentra SANCIONADO y no puede realizar compras.");
        }


        Compras compra = new Compras();
        compra.setUsuarioId(request.getUsuarioId());
        compra.setTicketId(request.getTicketId());
        compra.setCodigoQr(request.getCodigoQr());
        compra.setFechaCompra(request.getFechaCompra());
        compra.setMetodoPago(request.getMetodoPago());
        compra.setEstado(request.getEstado());

        Compras compraGuardada = comprasRepository.save(compra);
        log.info("Compra registrada de forma exitosa con ID: {}", compraGuardada.getId());

        return convertToResponseDTO(compraGuardada);
    }

    // ------------------------------------- CRUD: UPDATE (PUT) ----------------------------------------
    public CompraResponseDTO updateCompra(Long id, CompraRequestDTO request) {
        log.info("Actualizando datos de la compra con ID: {}", id);
        Compras compraReal = comprasRepository.findById(id)
                .orElseThrow(() -> new ComprasNotFoundException("La compra con id: " + id + " no fue encontrada"));

        compraReal.setUsuarioId(request.getUsuarioId());
        compraReal.setTicketId(request.getTicketId());
        compraReal.setCodigoQr(request.getCodigoQr());
        compraReal.setFechaCompra(request.getFechaCompra());
        compraReal.setMetodoPago(request.getMetodoPago());
        compraReal.setEstado(request.getEstado());

        Compras compraGuardada = comprasRepository.save(compraReal);
        return convertToResponseDTO(compraGuardada);
    }

    // ------------------------------------- CRUD: DELETE ----------------------------------------------
    public String deleteCompra(Long id) {
        log.info("Eliminando compra con ID: {}", id);
        comprasRepository.findById(id)
                .orElseThrow(() -> new ComprasNotFoundException("La compra con id: " + id + " no se puede eliminar porque no existe"));

        comprasRepository.deleteById(id);
        return "La compra con id: " + id + " fue eliminada exitosamente";
    }
}