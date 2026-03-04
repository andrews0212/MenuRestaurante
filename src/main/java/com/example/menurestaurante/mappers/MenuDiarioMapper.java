package com.example.menurestaurante.mappers;


import com.example.menurestaurante.dto.MenuDiarioDTO;

import com.example.menurestaurante.entidades.MenuDiario;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {PlatoMapper.class, RestauranteMapper.class})
public interface MenuDiarioMapper {

    MenuDiarioDTO toDTO(MenuDiario menuDiario);

    MenuDiario toEntity(MenuDiarioDTO menuDiarioDTO);
}