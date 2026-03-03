package com.example.menurestaurante.mappers;

import com.example.menurestaurante.dto.MenuPlatoDTO;
import com.example.menurestaurante.entidades.MenuPlato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuPlatoMapper {

    @Mapping(source = "id.idMenuDiario", target = "idMenuDiario")
    @Mapping(source = "id.idPlato", target = "idPlato")
    @Mapping(target = "id", ignore = true)
    MenuPlatoDTO toDTO(MenuPlato menuPlato);

    @Mapping(source = "idMenuDiario", target = "id.idMenuDiario")
    @Mapping(source = "idPlato", target = "id.idPlato")
    @Mapping(source = "idMenuDiario", target = "idMenuDiario.id")
    @Mapping(source = "idPlato", target = "idPlato.id")
    MenuPlato toEntity(MenuPlatoDTO menuPlatoDTO);

    List<MenuPlatoDTO> toDTOList(List<MenuPlato> menuPlatos);
}
