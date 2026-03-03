package com.example.menurestaurante.mappers;


import com.example.menurestaurante.dto.UsuarioDTO;
import com.example.menurestaurante.entidades.Usuario;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO usuarioDTO);

    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);
}