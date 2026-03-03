package com.example.menurestaurante.controlador;

import com.example.menurestaurante.dto.MenuDiarioDTO;
import com.example.menurestaurante.servicio.MenuDiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus-diarios")
@CrossOrigin(origins = "*")
public class MenuDiarioControlador {

    @Autowired
    private MenuDiarioService menuDiarioService;

    // GET - Obtener todos los menús diarios
    @GetMapping
    public ResponseEntity<List<MenuDiarioDTO>> obtenerTodos() {
        List<MenuDiarioDTO> menus = menuDiarioService.obtenerTodosMenusDiarios();
        return ResponseEntity.ok(menus);
    }

    // GET - Obtener menú diario por ID
    @GetMapping("/{id}")
    public ResponseEntity<MenuDiarioDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            MenuDiarioDTO menu = menuDiarioService.obtenerMenuDiario(id);
            return ResponseEntity.ok(menu);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Crear un nuevo menú diario
    @PostMapping
    public ResponseEntity<MenuDiarioDTO> crear(@RequestBody MenuDiarioDTO menuDiarioDTO) {
        try {
            MenuDiarioDTO menuCreado = menuDiarioService.insertarMenuDiario(menuDiarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(menuCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT - Actualizar un menú diario existente
    @PutMapping("/{id}")
    public ResponseEntity<MenuDiarioDTO> actualizar(@PathVariable Integer id, @RequestBody MenuDiarioDTO menuDiarioDTO) {
        try {
            menuDiarioDTO.setId(id);
            MenuDiarioDTO menuActualizado = menuDiarioService.actualizarMenuDiario(menuDiarioDTO);
            return ResponseEntity.ok(menuActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE - Eliminar un menú diario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            menuDiarioService.eliminarMenuDiario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

