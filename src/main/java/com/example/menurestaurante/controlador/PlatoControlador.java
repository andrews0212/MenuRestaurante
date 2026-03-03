package com.example.menurestaurante.controlador;

import com.example.menurestaurante.dto.PlatoDTO;
import com.example.menurestaurante.servicio.PlatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platos")
@CrossOrigin(origins = "*")
public class PlatoControlador {

    @Autowired
    private PlatoService platoService;

    // GET - Obtener todos los platos
    @GetMapping
    public ResponseEntity<List<PlatoDTO>> obtenerTodos() {
        List<PlatoDTO> platos = platoService.obtenerTodosPlatos();
        return ResponseEntity.ok(platos);
    }

    // GET - Obtener plato por ID
    @GetMapping("/{id}")
    public ResponseEntity<PlatoDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            PlatoDTO plato = platoService.obtenerPlato(id);
            return ResponseEntity.ok(plato);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Crear un nuevo plato
    @PostMapping
    public ResponseEntity<PlatoDTO> crear(@RequestBody PlatoDTO platoDTO) {
        try {
            PlatoDTO platoCreado = platoService.insertarPlato(platoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(platoCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT - Actualizar un plato existente
    @PutMapping("/{id}")
    public ResponseEntity<PlatoDTO> actualizar(@PathVariable Integer id, @RequestBody PlatoDTO platoDTO) {
        try {
            platoDTO.setId(id);
            PlatoDTO platoActualizado = platoService.actualizarPlato(platoDTO);
            return ResponseEntity.ok(platoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE - Eliminar un plato por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            platoService.eliminarPlato(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

