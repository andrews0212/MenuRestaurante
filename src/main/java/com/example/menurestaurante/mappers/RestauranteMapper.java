package com.example.menurestaurante.mappers;

import com.example.menurestaurante.dto.RestauranteDTO;
import com.example.menurestaurante.entidades.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface RestauranteMapper {

    @Mapping(source = "idUsuario.id", target = "idUsuario")
    RestauranteDTO toDTO(Restaurante restaurante);

    @Mapping(source = "idUsuario", target = "idUsuario.id")
    Restaurante toEntity(RestauranteDTO restauranteDTO);

    List<RestauranteDTO> toDTOList(List<Restaurante> restaurantes);
}