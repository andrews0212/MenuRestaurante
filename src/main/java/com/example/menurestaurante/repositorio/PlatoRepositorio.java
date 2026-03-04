package com.example.menurestaurante.repositorio;

import com.example.menurestaurante.entidades.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatoRepositorio extends JpaRepository<Plato, Integer> {

    Optional<Plato> findByNombreIgnoreCase(String nombre);
}
