package com.example.menurestaurante.mappers;

import com.example.menurestaurante.dto.PlatoDTO;
import com.example.menurestaurante.entidades.Plato;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlatoMapper {

    // 1. Convierte una Entidad (BD) a DTO (Frontend)
    PlatoDTO toDTO(Plato plato);

    // 2. Convierte un DTO (Frontend) a Entidad (BD) - Útil para crear o actualizar
    Plato toEntity(PlatoDTO platoDTO);

    // 3. ¡Súper útil! Convierte una lista entera automáticamente
    List<PlatoDTO> toDTOList(List<Plato> platos);
}