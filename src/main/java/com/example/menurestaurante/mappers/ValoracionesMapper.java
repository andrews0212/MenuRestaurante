package com.example.menurestaurante.mappers;

import com.example.menurestaurante.dto.ValoracionesDTO;
import com.example.menurestaurante.entidades.Valoraciones;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ValoracionesMapper {

    @Mapping(source = "idRestaurante.id", target = "idRestaurante")
    ValoracionesDTO toDTO(Valoraciones valoraciones);

    @Mapping(source = "idRestaurante", target = "idRestaurante.id")
    Valoraciones toEntity(ValoracionesDTO valoracionesDTO);

    List<ValoracionesDTO> toDTOList(List<Valoraciones> valoraciones);
}