package com.example.menurestaurante.repositorio;

import com.example.menurestaurante.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    List<Usuario> getUsuariosById(Integer id);
}
