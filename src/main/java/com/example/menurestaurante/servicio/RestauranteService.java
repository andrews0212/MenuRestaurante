package com.example.menurestaurante.servicio;

import com.example.menurestaurante.dto.RestauranteDTO;
import com.example.menurestaurante.entidades.Restaurante;
import com.example.menurestaurante.mappers.RestauranteMapper;
import com.example.menurestaurante.repositorio.RestauranteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepositorio restauranteRepositorio;
    @Autowired
    private RestauranteMapper restauranteMapper;

    // GET - Obtener restaurante por ID
    public RestauranteDTO obtenerRestaurante(Integer id) {
        Restaurante entidad = restauranteRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: El restaurante con ID " + id + " no existe."));
        return restauranteMapper.toDTO(entidad);
    }

    // GET ALL - Obtener todos los restaurantes
    public List<RestauranteDTO> obtenerTodosRestaurantes() {
        List<Restaurante> restaurantes = restauranteRepositorio.findAll();
        return restauranteMapper.toDTOList(restaurantes);
    }

    // POST - Crear un nuevo restaurante
    public RestauranteDTO insertarRestaurante(RestauranteDTO restauranteDTO) {
        try {
            Restaurante entidadNueva = restauranteMapper.toEntity(restauranteDTO);
            Restaurante entidadGuardada = restauranteRepositorio.save(entidadNueva);
            return restauranteMapper.toDTO(entidadGuardada);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error: No se pudo crear el restaurante. Verifica que los datos sean correctos.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar guardar el restaurante en el sistema.", e);
        }
    }

    // PUT - Actualizar un restaurante existente
    public RestauranteDTO actualizarRestaurante(RestauranteDTO restauranteDTO) {
        try {
            if (restauranteDTO.getId() == null) {
                throw new IllegalArgumentException("Error: Para actualizar un restaurante, el ID no puede ser nulo.");
            }

            Restaurante entidad = restauranteMapper.toEntity(restauranteDTO);
            Restaurante entidadActualizada = restauranteRepositorio.save(entidad);
            return restauranteMapper.toDTO(entidadActualizada);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error de integridad: Verifica que no estés duplicando datos únicos.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar actualizar el restaurante.", e);
        }
    }

    // DELETE - Eliminar un restaurante por ID
    public void eliminarRestaurante(Integer id) {
        try {
            Restaurante entidadAEliminar = restauranteRepositorio.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se puede eliminar. El restaurante con ID " + id + " no existe."));
            restauranteRepositorio.delete(entidadAEliminar);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar eliminar el restaurante.", e);
        }
    }
}
