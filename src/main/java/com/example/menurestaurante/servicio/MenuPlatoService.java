package com.example.menurestaurante.servicio;

import com.example.menurestaurante.dto.MenuPlatoDTO;
import com.example.menurestaurante.entidades.MenuPlato;
import com.example.menurestaurante.entidades.MenuPlatoId;
import com.example.menurestaurante.mappers.MenuPlatoMapper;
import com.example.menurestaurante.repositorio.MenuPlatoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuPlatoService {

    @Autowired
    private MenuPlatoRepositorio menuPlatoRepositorio;
    @Autowired
    private MenuPlatoMapper menuPlatoMapper;

    // GET - Obtener relación menú-plato por ID compuesto
    public MenuPlatoDTO obtenerMenuPlato(Integer idMenuDiario, Integer idPlato) {
        MenuPlatoId id = new MenuPlatoId();
        id.setIdMenuDiario(idMenuDiario);
        id.setIdPlato(idPlato);

        MenuPlato entidad = menuPlatoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La relación menú-plato con ID (" + idMenuDiario + ", " + idPlato + ") no existe."));
        return menuPlatoMapper.toDTO(entidad);
    }

    // GET ALL - Obtener todas las relaciones menú-plato
    public List<MenuPlatoDTO> obtenerTodosMenuPlatos() {
        List<MenuPlato> menuPlatos = menuPlatoRepositorio.findAll();
        return menuPlatoMapper.toDTOList(menuPlatos);
    }

    // POST - Crear una nueva relación menú-plato
    public MenuPlatoDTO insertarMenuPlato(MenuPlatoDTO menuPlatoDTO) {
        try {
            MenuPlato entidadNueva = menuPlatoMapper.toEntity(menuPlatoDTO);
            MenuPlato entidadGuardada = menuPlatoRepositorio.save(entidadNueva);
            return menuPlatoMapper.toDTO(entidadGuardada);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error: No se pudo crear la relación menú-plato. Verifica que los datos sean correctos.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar guardar la relación menú-plato en el sistema.", e);
        }
    }

    // DELETE - Eliminar una relación menú-plato por ID compuesto
    public void eliminarMenuPlato(Integer idMenuDiario, Integer idPlato) {
        try {
            MenuPlatoId id = new MenuPlatoId();
            id.setIdMenuDiario(idMenuDiario);
            id.setIdPlato(idPlato);

            MenuPlato entidadAEliminar = menuPlatoRepositorio.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se puede eliminar. La relación menú-plato con ID (" + idMenuDiario + ", " + idPlato + ") no existe."));
            menuPlatoRepositorio.delete(entidadAEliminar);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar eliminar la relación menú-plato.", e);
        }
    }
}
