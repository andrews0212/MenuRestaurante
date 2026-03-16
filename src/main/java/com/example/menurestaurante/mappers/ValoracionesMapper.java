package com.example.menurestaurante.mappers;

import com.example.menurestaurante.dto.ValoracionesDTO;
import com.example.menurestaurante.entidades.Valoraciones;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ValoracionesMapper {

    @Mapping(source = "idUsuario.id", target = "idUsuario")
    @Mapping(source = "idRestaurante.id", target = "idRestaurante")
    @Mapping(source = "idPlato.id", target = "idPlato")
    ValoracionesDTO toDTO(Valoraciones valoraciones);

    @Mapping(source = "idUsuario", target = "idUsuario.id")
    @Mapping(source = "idRestaurante", target = "idRestaurante.id")
    @Mapping(source = "idPlato", target = "idPlato.id")
    Valoraciones toEntity(ValoracionesDTO valoracionesDTO);

    List<ValoracionesDTO> toDTOList(List<Valoraciones> valoraciones);
}