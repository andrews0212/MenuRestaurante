package com.example.menurestaurante.servicio;

import com.example.menurestaurante.dto.PlatoDTO;
import com.example.menurestaurante.entidades.Plato;
import com.example.menurestaurante.mappers.PlatoMapper;
import com.example.menurestaurante.repositorio.PlatoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatoService {

    @Autowired
    private PlatoRepositorio platoRepositorio;
    @Autowired
    private PlatoMapper platoMapper;

    // GET - Obtener plato por ID
    public PlatoDTO obtenerPlato(Integer id) {
        Plato entidad = platoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: El plato con ID " + id + " no existe."));
        return platoMapper.toDTO(entidad);
    }

    // GET ALL - Obtener todos los platos
    public List<PlatoDTO> obtenerTodosPlatos() {
        List<Plato> platos = platoRepositorio.findAll();
        return platoMapper.toDTOList(platos);
    }

    // POST - Crear un nuevo plato
    public PlatoDTO insertarPlato(PlatoDTO platoDTO) {
        try {
            Plato entidadNueva = platoMapper.toEntity(platoDTO);
            Plato entidadGuardada = platoRepositorio.save(entidadNueva);
            return platoMapper.toDTO(entidadGuardada);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error: No se pudo crear el plato. Verifica que los datos sean correctos.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar guardar el plato en el sistema.", e);
        }
    }

    // PUT - Actualizar un plato existente
    public PlatoDTO actualizarPlato(PlatoDTO platoDTO) {
        try {
            if (platoDTO.getId() == null) {
                throw new IllegalArgumentException("Error: Para actualizar un plato, el ID no puede ser nulo.");
            }

            Plato entidad = platoMapper.toEntity(platoDTO);
            Plato entidadActualizada = platoRepositorio.save(entidad);
            return platoMapper.toDTO(entidadActualizada);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error de integridad: Verifica que no estés duplicando datos únicos.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar actualizar el plato.", e);
        }
    }

    // DELETE - Eliminar un plato por ID
    public void eliminarPlato(Integer id) {
        try {
            Plato entidadAEliminar = platoRepositorio.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se puede eliminar. El plato con ID " + id + " no existe."));
            platoRepositorio.delete(entidadAEliminar);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar eliminar el plato.", e);
        }
    }
}
