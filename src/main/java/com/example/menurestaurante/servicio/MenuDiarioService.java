package com.example.menurestaurante.servicio;

import com.example.menurestaurante.dto.MenuDiarioDTO;
import com.example.menurestaurante.entidades.MenuDiario;
import com.example.menurestaurante.mappers.MenuDiarioMapper;
import com.example.menurestaurante.repositorio.MenuDiarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuDiarioService {

    @Autowired
    private MenuDiarioRepositorio menuDiarioRepositorio;
    @Autowired
    private MenuDiarioMapper menuDiarioMapper;

    // GET - Obtener menú diario por ID
    public MenuDiarioDTO obtenerMenuDiario(Integer id) {
        MenuDiario entidad = menuDiarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: El menú diario con ID " + id + " no existe."));
        return menuDiarioMapper.toDTO(entidad);
    }

    // GET ALL - Obtener todos los menús diarios
    public List<MenuDiarioDTO> obtenerTodosMenusDiarios() {
        List<MenuDiario> menus = menuDiarioRepositorio.findAll();
        return menus.stream()
                .map(menuDiarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    // POST - Crear un nuevo menú diario
    public MenuDiarioDTO insertarMenuDiario(MenuDiarioDTO menuDiarioDTO) {
        try {
            MenuDiario entidadNueva = menuDiarioMapper.toEntity(menuDiarioDTO);
            MenuDiario entidadGuardada = menuDiarioRepositorio.save(entidadNueva);
            return menuDiarioMapper.toDTO(entidadGuardada);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error: No se pudo crear el menú diario. Verifica que los datos sean correctos.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar guardar el menú diario en el sistema.", e);
        }
    }

    // PUT - Actualizar un menú diario existente
    public MenuDiarioDTO actualizarMenuDiario(MenuDiarioDTO menuDiarioDTO) {
        try {
            if (menuDiarioDTO.getId() == null) {
                throw new IllegalArgumentException("Error: Para actualizar un menú diario, el ID no puede ser nulo.");
            }

            MenuDiario entidad = menuDiarioMapper.toEntity(menuDiarioDTO);
            MenuDiario entidadActualizada = menuDiarioRepositorio.save(entidad);
            return menuDiarioMapper.toDTO(entidadActualizada);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error de integridad: Verifica que no estés duplicando datos únicos.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar actualizar el menú diario.", e);
        }
    }

    // DELETE - Eliminar un menú diario por ID
    public void eliminarMenuDiario(Integer id) {
        try {
            MenuDiario entidadAEliminar = menuDiarioRepositorio.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se puede eliminar. El menú diario con ID " + id + " no existe."));
            menuDiarioRepositorio.delete(entidadAEliminar);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar eliminar el menú diario.", e);
        }
    }
}

