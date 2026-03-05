package com.example.menurestaurante.servicio;

import com.example.menurestaurante.dto.UsuarioDTO;
import com.example.menurestaurante.entidades.Usuario;
import com.example.menurestaurante.mappers.UsuarioMapper;
import com.example.menurestaurante.repositorio.RestauranteRepositorio;
import com.example.menurestaurante.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private UsuarioMapper usuarioMapper;
    @Autowired
    private RestauranteRepositorio restauranteRepositorio;

    //get
    public UsuarioDTO obtenerUsuario(Integer id){
        Usuario entidad = usuarioRepositorio.getUsuariosById(id).get(0);
        return convertirADTOConRestaurante(entidad);
    }
    //getAll
    public List<UsuarioDTO> obtenerTodosUsuarios(){
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return usuarios.stream()
                .map(this::convertirADTOConRestaurante)
                .collect(Collectors.toList());
    }
    //post
    public UsuarioDTO insectarUsuario(UsuarioDTO usuarioDTO){
        try {
            // 1. Convertimos el DTO a Entidad
            Usuario entidadNueva = usuarioMapper.toEntity(usuarioDTO);

            // 2. INTENTAMOS guardar en la base de datos
            Usuario entidadGuardada = usuarioRepositorio.save(entidadNueva);

            // 3. Si tiene éxito, mapeamos a DTO y retornamos
            return convertirADTOConRestaurante(entidadGuardada);

        } catch (DataIntegrityViolationException e) {
            // Este error específico salta si rompes una regla de la base de datos.
            // Ejemplo: intentas guardar un correo que ya existe (Unique) o falta un campo obligatorio.
            // Aquí lanzamos una excepción controlada con un mensaje amigable
            throw new RuntimeException("Error: No se pudo crear el usuario. Es posible que el correo ya exista o falten datos.", e);

        } catch (Exception e) {
            // Este bloque captura CUALQUIER otro error inesperado
            throw new RuntimeException("Error inesperado al intentar guardar el usuario en el sistema.", e);
        }
    }
    //put
    public UsuarioDTO actualizarUsuario(UsuarioDTO usuarioDTO){
        try {
            // 1. Validación de seguridad: Asegurarnos de que el DTO trae un ID
            // Si no trae ID, save() crearía un usuario nuevo en vez de actualizar.
            if (usuarioDTO.getId() == null) {
                throw new IllegalArgumentException("Error: Para actualizar un usuario, el ID no puede ser nulo.");
            }

            // 2. Convertimos el DTO a Entidad.
            // Como esta entidad ya llevará su ID, Spring sabrá que debe sobrescribir.
            Usuario entidad = usuarioMapper.toEntity(usuarioDTO);

            // 3. Guardamos. Al detectar que el ID ya existe en la BD, ejecutará un UPDATE.
            Usuario entidadActualizada = usuarioRepositorio.save(entidad);

            // 4. Retornamos la entidad actualizada ya mapeada a DTO
            return convertirADTOConRestaurante(entidadActualizada);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error de integridad: Verifica que no estés duplicando datos únicos como el correo.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar actualizar el usuario.", e);
        }
    }
    // --- DELETE (Eliminar) ---
    public void eliminarUsuario(Integer id) {
        try {
            // Buscamos la entidad primero usando el mismo método que usas en el GET
            Usuario entidadAEliminar = usuarioRepositorio.getUsuariosById(id).get(0);

            // Eliminamos la entidad de la base de datos
            usuarioRepositorio.delete(entidadAEliminar);

        } catch (IndexOutOfBoundsException e) {
            // Si getUsuariosById().get(0) falla, significa que la lista está vacía
            throw new RuntimeException("Error: No se puede eliminar. El usuario con ID " + id + " no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al intentar eliminar el usuario.", e);
        }
    }

    /**
     * Convierte un Usuario a UsuarioDTO rellenando el idRestaurante
     * buscando en la tabla restaurante si tiene uno asociado.
     */
    private UsuarioDTO convertirADTOConRestaurante(Usuario usuario) {
        UsuarioDTO dto = usuarioMapper.toDTO(usuario);
        // Buscar si este usuario tiene un restaurante asociado
        restauranteRepositorio.findByIdUsuarioId(usuario.getId())
                .ifPresent(restaurante -> dto.setIdRestaurante(restaurante.getId()));
        return dto;
    }
}
