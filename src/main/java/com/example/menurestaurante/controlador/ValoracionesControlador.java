package com.example.menurestaurante.controlador;

import com.example.menurestaurante.dto.ValoracionesDTO;
import com.example.menurestaurante.servicio.ValoracionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/valoraciones")
@CrossOrigin(origins = "*")
public class ValoracionesControlador {

    @Autowired
    private ValoracionesService valoracionesService;

    // GET - Obtener todas las valoraciones
    @GetMapping
    public ResponseEntity<List<ValoracionesDTO>> obtenerTodas() {
        List<ValoracionesDTO> valoraciones = valoracionesService.obtenerTodasValoraciones();
        return ResponseEntity.ok(valoraciones);
    }

    // GET - Obtener valoración por ID
    @GetMapping("/{id}")
    public ResponseEntity<ValoracionesDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            ValoracionesDTO valoracion = valoracionesService.obtenerValoracion(id);
            return ResponseEntity.ok(valoracion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Crear una nueva valoración
    @PostMapping
    public ResponseEntity<ValoracionesDTO> crear(@RequestBody ValoracionesDTO valoracionesDTO) {
        try {
            ValoracionesDTO valoracionCreada = valoracionesService.insertarValoracion(valoracionesDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(valoracionCreada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT - Actualizar una valoración existente
    @PutMapping("/{id}")
    public ResponseEntity<ValoracionesDTO> actualizar(@PathVariable Integer id, @RequestBody ValoracionesDTO valoracionesDTO) {
        try {
            valoracionesDTO.setId(id);
            ValoracionesDTO valoracionActualizada = valoracionesService.actualizarValoracion(valoracionesDTO);
            return ResponseEntity.ok(valoracionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE - Eliminar una valoración por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            valoracionesService.eliminarValoracion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

