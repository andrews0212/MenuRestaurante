package com.example.menurestaurante.servicio;

import com.example.menurestaurante.dto.ValoracionesDTO;
import com.example.menurestaurante.entidades.Valoraciones;
import com.example.menurestaurante.mappers.ValoracionesMapper;
import com.example.menurestaurante.repositorio.ValoracionesRepositorios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValoracionesService {

    @Autowired
    private ValoracionesRepositorios valoracionesRepositorio;
    @Autowired
    private ValoracionesMapper valoracionesMapper;

    // GET - Obtener valoración por ID
    public ValoracionesDTO obtenerValoracion(Integer id) {
        Valoraciones entidad = valoracionesRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La valoración con ID " + id + " no existe."));
        return valoracionesMapper.toDTO(entidad);
    }

    // GET ALL - Obtener todas las valoraciones
    public List<ValoracionesDTO> obtenerTodasValoraciones() {
        List<Valoraciones> valoraciones = valoracionesRepositorio.findAll();
        return valoracionesMapper.toDTOList(valoraciones);
    }

    // POST - Crear una nueva valoración
    public ValoracionesDTO insertarValoracion(ValoracionesDTO valoracionesDTO) {
        try {
            validarIdsRequeridos(valoracionesDTO);
            Valoraciones entidadNueva = valoracionesMapper.toEntity(valoracionesDTO);
            Valoraciones entidadGuardada = valoracionesRepositorio.save(entidadNueva);
            return valoracionesMapper.toDTO(entidadGuardada);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error: No se pudo crear la valoración. Verifica que usuario, restaurante y plato existan.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar guardar la valoración en el sistema.", e);
        }
    }

    // PUT - Actualizar una valoración existente
    public ValoracionesDTO actualizarValoracion(ValoracionesDTO valoracionesDTO) {
        try {
            if (valoracionesDTO.getId() == null) {
                throw new IllegalArgumentException("Error: Para actualizar una valoración, el ID no puede ser nulo.");
            }
            validarIdsRequeridos(valoracionesDTO);

            Valoraciones entidad = valoracionesMapper.toEntity(valoracionesDTO);
            Valoraciones entidadActualizada = valoracionesRepositorio.save(entidad);
            return valoracionesMapper.toDTO(entidadActualizada);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error de integridad: Verifica que usuario, restaurante y plato referenciados existan.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar actualizar la valoración.", e);
        }
    }

    // DELETE - Eliminar una valoración por ID
    public void eliminarValoracion(Integer id) {
        try {
            Valoraciones entidadAEliminar = valoracionesRepositorio.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se puede eliminar. La valoración con ID " + id + " no existe."));
            valoracionesRepositorio.delete(entidadAEliminar);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar eliminar la valoración.", e);
        }
    }

    private void validarIdsRequeridos(ValoracionesDTO valoracionesDTO) {
        if (valoracionesDTO.getIdUsuario() == null) {
            throw new IllegalArgumentException("Error: idUsuario es obligatorio.");
        }
        if (valoracionesDTO.getIdRestaurante() == null) {
            throw new IllegalArgumentException("Error: idRestaurante es obligatorio.");
        }
        if (valoracionesDTO.getIdPlato() == null) {
            throw new IllegalArgumentException("Error: idPlato es obligatorio.");
        }
    }
}
