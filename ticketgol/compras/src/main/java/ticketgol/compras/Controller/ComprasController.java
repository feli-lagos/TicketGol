package ticketgol.compras.Controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ticketgol.compras.Model.Compras;
import ticketgol.compras.Service.ComprasService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {
    private final ComprasService service;

    public ComprasController(ComprasService service){
        this.service = service;
    }
    @GetMapping
    public  List<Compras> listarCompras(){
        return service.obtenerTodos();
    }@GetMapping("/{id}")
    public  ResponseEntity<Compras> buscarPorId(@PathVariable Long id){
      return service.obtenerPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/usuario/{usuarioId}")
    public List<Compras> listPorUsuario(@PathVariable String usuarioId){
        return  service.obtenerPorUsuario(usuarioId);
    }
    @PostMapping
    public ResponseEntity<Compras> crearCompra(@Valid @RequestBody Compras compras){
        Compras nuevaCompra = service.guardar(compras);
        return  new ResponseEntity<>(nuevaCompra,HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> eliminarCompra(@PathVariable Long id){
        if(service.obtenerPorId(id).isPresent()){
            service.eliminar(id);
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.notFound().build();
    }


}
