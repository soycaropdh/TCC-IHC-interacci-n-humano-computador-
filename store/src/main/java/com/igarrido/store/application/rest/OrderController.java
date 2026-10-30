package com.igarrido.store.application.rest;
import com.igarrido.store.application.request.OrderRequest;
import com.igarrido.store.application.request.OrderStatusRequest;
import com.igarrido.store.domain.model.Order;
import com.igarrido.store.domain.service.OrderService;
import com.igarrido.store.infrastructure.storage.LocalImageStorage;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@RestController @RequestMapping("/api/orders")
public class OrderController {
 private final OrderService service; private final LocalImageStorage storage;
 public OrderController(OrderService service,LocalImageStorage storage){this.service=service;this.storage=storage;}
 @GetMapping public List<Order> all(Authentication auth){return isAdmin(auth)?service.findAll():service.findAllByCustomer(auth.getName());}
 @GetMapping("/{id}") public ResponseEntity<Order> one(@PathVariable Long id,Authentication auth){return service.findById(id).filter(o->isAdmin(auth)||auth.getName().equals(o.getCustomerUsername())).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());}
 @PostMapping public ResponseEntity<Order> create(@RequestBody OrderRequest r,Authentication auth){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r.getCustomerName(),r.getPhone(),r.getProductId(),r.getQuantity(),r.getNotes(),auth.getName()));}
 @PutMapping("/{id}/status") public Order status(@PathVariable Long id,@RequestBody OrderStatusRequest r,Authentication auth){if(!isAdmin(auth)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Solo el administrador puede cambiar el estado");return service.updateStatus(id,r.isOrdered(),r.isPrepared(),r.isPaid());}
 @PutMapping("/{id}") public Order update(@PathVariable Long id,@RequestBody OrderRequest r,Authentication auth){if(isAdmin(auth)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"El administrador no edita encargos desde esta opción");return service.update(id,auth.getName(),r.getCustomerName(),r.getPhone(),r.getQuantity(),r.getNotes());}
 @PostMapping(value="/{id}/payment-proof",consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public Order paymentProof(@PathVariable Long id,@RequestParam("file") MultipartFile file,Authentication auth){if(file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Selecciona una imagen");String contentType=file.getContentType();if(contentType==null||!contentType.startsWith("image/")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El comprobante debe ser una imagen");try{String ext=".jpg";String name=file.getOriginalFilename();if(name!=null&&name.lastIndexOf('.')>=0)ext=name.substring(name.lastIndexOf('.'));String url=storage.save("comprobante-"+UUID.randomUUID()+ext,file.getBytes());return service.markPaid(id,auth.getName(),url);}catch(IOException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se pudo guardar el comprobante",e);}}
 @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id,Authentication auth){if(isAdmin(auth)){service.deleteAdmin(id);}else{service.delete(id,auth.getName());}return ResponseEntity.noContent().build();}
 private boolean isAdmin(Authentication auth){return auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));}
}
