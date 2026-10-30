package com.igarrido.store.domain.service;
import com.igarrido.store.domain.model.Material;
import com.igarrido.store.infrastructure.persistence.entity.MaterialEntity;
import com.igarrido.store.infrastructure.persistence.jpa.MaterialJpaRepository;
import com.igarrido.store.infrastructure.persistence.mapper.MaterialMapper;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class MaterialService {
 private final MaterialJpaRepository repo; public MaterialService(MaterialJpaRepository repo){this.repo=repo;}
 public List<Material> all(){return repo.findAll().stream().map(MaterialMapper::toDomain).collect(Collectors.toList());}
 public Material create(Material m){validate(m); return MaterialMapper.toDomain(repo.save(MaterialMapper.toEntity(m)));}
 public Material update(Long id,Material m){if(!repo.existsById(id))throw new IllegalArgumentException("El material no existe");m.setId(id);validate(m);return MaterialMapper.toDomain(repo.save(MaterialMapper.toEntity(m)));}
 public Material registerBatch(Long id){MaterialEntity e=repo.findById(id).orElseThrow(()->new IllegalArgumentException("El material no existe"));BigDecimal next=e.getStock().subtract(e.getBatchConsumption());if(next.compareTo(BigDecimal.ZERO)<0)throw new IllegalArgumentException("No hay suficiente stock de "+e.getName()+" para registrar una tanda");e.setStock(next);return MaterialMapper.toDomain(repo.save(e));}
 public Material addStock(Long id,BigDecimal quantity){if(quantity==null||quantity.compareTo(BigDecimal.ZERO)<=0)throw new IllegalArgumentException("La cantidad debe ser mayor que 0");MaterialEntity e=repo.findById(id).orElseThrow(()->new IllegalArgumentException("El material no existe"));e.setStock(e.getStock().add(quantity));return MaterialMapper.toDomain(repo.save(e));}
 public void delete(Long id){repo.deleteById(id);}
 private void validate(Material m){if(m.getName()==null||m.getName().trim().isEmpty())throw new IllegalArgumentException("El nombre es obligatorio");if(m.getUnit()==null||m.getUnit().trim().isEmpty())throw new IllegalArgumentException("La unidad es obligatoria");if(m.getStock()==null||m.getStock().compareTo(BigDecimal.ZERO)<0)throw new IllegalArgumentException("El stock no puede ser negativo");if(m.getBatchConsumption()==null||m.getBatchConsumption().compareTo(BigDecimal.ZERO)<0)throw new IllegalArgumentException("El consumo por tanda no puede ser negativo");if(m.getMinStock()==null||m.getMinStock().compareTo(BigDecimal.ZERO)<0)throw new IllegalArgumentException("El mínimo no puede ser negativo");m.setName(m.getName().trim());m.setUnit(m.getUnit().trim());}
}
