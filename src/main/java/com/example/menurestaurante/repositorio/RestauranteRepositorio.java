package com.example.menurestaurante.repositorio;

import com.example.menurestaurante.entidades.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestauranteRepositorio extends JpaRepository<Restaurante, Integer> {

    Optional<Restaurante> findByIdUsuarioId(Integer idUsuario);
}
