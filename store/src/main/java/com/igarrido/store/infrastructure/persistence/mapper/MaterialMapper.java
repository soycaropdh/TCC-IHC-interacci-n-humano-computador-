package com.igarrido.store.infrastructure.persistence.mapper;
import com.igarrido.store.domain.model.Material;
import com.igarrido.store.infrastructure.persistence.entity.MaterialEntity;
public class MaterialMapper {
 public static Material toDomain(MaterialEntity e){return Material.builder().id(e.getId()).name(e.getName()).unit(e.getUnit()).stock(e.getStock()).price(e.getPrice()).batchConsumption(e.getBatchConsumption()).minStock(e.getMinStock()).build();}
 public static MaterialEntity toEntity(Material d){return MaterialEntity.builder().id(d.getId()).name(d.getName()).unit(d.getUnit()).stock(d.getStock()).price(d.getPrice()).batchConsumption(d.getBatchConsumption()).minStock(d.getMinStock()).build();}
}
