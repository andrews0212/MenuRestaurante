package com.example.menurestaurante.repositorio;

import com.example.menurestaurante.entidades.MenuDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuDiarioRepositorio extends JpaRepository<MenuDiario, Integer> {
}

