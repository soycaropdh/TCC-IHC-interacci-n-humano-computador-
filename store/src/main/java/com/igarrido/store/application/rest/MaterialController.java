package com.igarrido.store.application.rest;
import com.igarrido.store.application.request.MaterialRequest;
import com.igarrido.store.application.request.StockRequest;
import com.igarrido.store.domain.model.Material;
import com.igarrido.store.domain.service.MaterialService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/materials")
public class MaterialController {
 private final MaterialService service; public MaterialController(MaterialService service){this.service=service;}
 @GetMapping public List<Material> all(){return service.all();}
 @PostMapping public ResponseEntity<Material> create(@RequestBody MaterialRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(toDomain(r)));}
 @PutMapping("/{id}") public Material update(@PathVariable Long id,@RequestBody MaterialRequest r){return service.update(id,toDomain(r));}
 @PostMapping("/{id}/register-batch") public Material registerBatch(@PathVariable Long id){return service.registerBatch(id);}
 @PostMapping("/{id}/add-stock") public Material addStock(@PathVariable Long id,@RequestBody StockRequest r){return service.addStock(id,r.getQuantity());}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){service.delete(id);return ResponseEntity.noContent().build();}
 private Material toDomain(MaterialRequest r){return Material.builder().name(r.getName()).unit(r.getUnit()).stock(r.getStock()).price(r.getPrice()).batchConsumption(r.getBatchConsumption()).minStock(r.getMinStock()).build();}
}
