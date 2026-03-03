package com.example.menurestaurante.repositorio;

import com.example.menurestaurante.entidades.MenuPlato;
import com.example.menurestaurante.entidades.MenuPlatoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuPlatoRepositorio extends JpaRepository<MenuPlato, MenuPlatoId> {
}
