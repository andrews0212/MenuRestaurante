package com.example.menurestaurante.repositorio;

import com.example.menurestaurante.entidades.Valoraciones;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ValoracionesRepositorios extends JpaRepository<Valoraciones, Integer> {
}
