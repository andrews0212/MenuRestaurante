package com.example.menurestaurante.controlador;

import com.example.menurestaurante.dto.MenuPlatoDTO;
import com.example.menurestaurante.servicio.MenuPlatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-platos")
@CrossOrigin(origins = "*")
public class MenuPlatoControlador {

    @Autowired
    private MenuPlatoService menuPlatoService;

    // GET - Obtener todas las relaciones menú-plato
    @GetMapping
    public ResponseEntity<List<MenuPlatoDTO>> obtenerTodos() {
        List<MenuPlatoDTO> menuPlatos = menuPlatoService.obtenerTodosMenuPlatos();
        return ResponseEntity.ok(menuPlatos);
    }

    // GET - Obtener relación menú-plato por ID compuesto
    @GetMapping("/{idMenuDiario}/{idPlato}")
    public ResponseEntity<MenuPlatoDTO> obtenerPorId(@PathVariable Integer idMenuDiario, @PathVariable Integer idPlato) {
        try {
            MenuPlatoDTO menuPlato = menuPlatoService.obtenerMenuPlato(idMenuDiario, idPlato);
            return ResponseEntity.ok(menuPlato);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Crear una nueva relación menú-plato
    @PostMapping
    public ResponseEntity<MenuPlatoDTO> crear(@RequestBody MenuPlatoDTO menuPlatoDTO) {
        try {
            MenuPlatoDTO menuPlatoCreado = menuPlatoService.insertarMenuPlato(menuPlatoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(menuPlatoCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE - Eliminar una relación menú-plato por ID compuesto
    @DeleteMapping("/{idMenuDiario}/{idPlato}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idMenuDiario, @PathVariable Integer idPlato) {
        try {
            menuPlatoService.eliminarMenuPlato(idMenuDiario, idPlato);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

