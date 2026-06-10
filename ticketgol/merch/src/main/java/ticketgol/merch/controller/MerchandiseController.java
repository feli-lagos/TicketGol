package ticketgol.merch.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.merch.dto.MerchandiseRequestDTO;
import ticketgol.merch.model.Merchandise;
import ticketgol.merch.service.MerchandiseService;

@RestController
@RequestMapping("/api/merchandise")
public class MerchandiseController {

    private final MerchandiseService service;

    public MerchandiseController(MerchandiseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Merchandise> registrarArticulo(@Valid @RequestBody MerchandiseRequestDTO dto) {
        Merchandise nuevoArticulo = service.guardarArticulo(dto);
        return new ResponseEntity<>(nuevoArticulo, HttpStatus.CREATED);
    }
}