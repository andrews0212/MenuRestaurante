package com.example.menurestaurante.mappers;

import com.example.menurestaurante.dto.RestauranteDTO;
import com.example.menurestaurante.entidades.Restaurante;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RestauranteMapper {

    RestauranteDTO toDTO(Restaurante restaurante);

    Restaurante toEntity(RestauranteDTO restauranteDTO);

    List<RestauranteDTO> toDTOList(List<Restaurante> restaurantes);
}