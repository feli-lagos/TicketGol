package ticketgol.merch.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.merch.dto.MerchandiseRequestDTO;
import ticketgol.merch.model.Merchandise;
import ticketgol.merch.service.MerchandiseService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/merchandise")
public class MerchandiseController {

    private final MerchandiseService service;

    public MerchandiseController(MerchandiseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Merchandise> registrarArticulo(@Valid @RequestBody MerchandiseRequestDTO dto) {
        log.info("Recibida petición POST para registrar un nuevo artículo de merchandise");
        Merchandise nuevoArticulo = service.guardarArticulo(dto);
        return new ResponseEntity<>(nuevoArticulo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Merchandise>> listarArticulos() {
        log.info("Recibida petición GET para listar todos los artículos de merchandise");
        List<Merchandise> articulos = service.obtenerTodosLosArticulos();
        return ResponseEntity.ok(articulos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Merchandise> getOrden(@PathVariable Long id) {
        Merchandise orden = service.getOrdenById(id);
        return ResponseEntity.ok(orden);
    }
}