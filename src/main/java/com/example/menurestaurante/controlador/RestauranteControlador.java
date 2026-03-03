package com.example.menurestaurante.controlador;

import com.example.menurestaurante.dto.RestauranteDTO;
import com.example.menurestaurante.servicio.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteControlador {

    @Autowired
    private RestauranteService restauranteService;

    // GET - Obtener todos los restaurantes
    @GetMapping
    public ResponseEntity<List<RestauranteDTO>> obtenerTodos() {
        List<RestauranteDTO> restaurantes = restauranteService.obtenerTodosRestaurantes();
        return ResponseEntity.ok(restaurantes);
    }

    // GET - Obtener restaurante por ID
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            RestauranteDTO restaurante = restauranteService.obtenerRestaurante(id);
            return ResponseEntity.ok(restaurante);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Crear un nuevo restaurante
    @PostMapping
    public ResponseEntity<RestauranteDTO> crear(@RequestBody RestauranteDTO restauranteDTO) {
        try {
            RestauranteDTO restauranteCreado = restauranteService.insertarRestaurante(restauranteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(restauranteCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT - Actualizar un restaurante existente
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteDTO> actualizar(@PathVariable Integer id, @RequestBody RestauranteDTO restauranteDTO) {
        try {
            restauranteDTO.setId(id);
            RestauranteDTO restauranteActualizado = restauranteService.actualizarRestaurante(restauranteDTO);
            return ResponseEntity.ok(restauranteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE - Eliminar un restaurante por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            restauranteService.eliminarRestaurante(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

