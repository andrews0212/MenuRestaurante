package com.example.menurestaurante.controlador;

import com.example.menurestaurante.dto.UsuarioDTO;
import com.example.menurestaurante.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioControlador {

    @Autowired
    private UsuarioService usuarioService;

    // GET - Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // GET - Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            UsuarioDTO usuario = usuarioService.obtenerUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO usuarioCreado = usuarioService.insectarUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT - Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Integer id, @RequestBody UsuarioDTO usuarioDTO) {
        try {
            usuarioDTO.setId(id);
            UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(usuarioDTO);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE - Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

