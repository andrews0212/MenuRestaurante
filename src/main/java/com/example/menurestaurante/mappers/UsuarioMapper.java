package com.example.menurestaurante.mappers;

import com.example.menurestaurante.dto.UsuarioDTO;
import com.example.menurestaurante.entidades.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "idRestaurante", ignore = true)
    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(target = "id", source = "id")
    Usuario toEntity(UsuarioDTO usuarioDTO);

    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);
}