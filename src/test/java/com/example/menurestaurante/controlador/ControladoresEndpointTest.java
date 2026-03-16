package com.example.menurestaurante.controlador;

import com.example.menurestaurante.entidades.MenuDiario;
import com.example.menurestaurante.entidades.Plato;
import com.example.menurestaurante.entidades.Restaurante;
import com.example.menurestaurante.entidades.Usuario;
import com.example.menurestaurante.repositorio.MenuDiarioRepositorio;
import com.example.menurestaurante.repositorio.MenuPlatoRepositorio;
import com.example.menurestaurante.repositorio.PlatoRepositorio;
import com.example.menurestaurante.repositorio.RestauranteRepositorio;
import com.example.menurestaurante.repositorio.UsuarioRepositorio;
import com.example.menurestaurante.repositorio.ValoracionesRepositorios;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ControladoresEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private ValoracionesRepositorios valoracionesRepositorio;

    @Autowired
    private MenuPlatoRepositorio menuPlatoRepositorio;

    @Autowired
    private MenuDiarioRepositorio menuDiarioRepositorio;

    @Autowired
    private RestauranteRepositorio restauranteRepositorio;

    @Autowired
    private PlatoRepositorio platoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @BeforeEach
    void limpiarBaseDeDatos() {
        valoracionesRepositorio.deleteAllInBatch();
        menuPlatoRepositorio.deleteAllInBatch();
        menuDiarioRepositorio.deleteAllInBatch();
        restauranteRepositorio.deleteAllInBatch();
        platoRepositorio.deleteAllInBatch();
        usuarioRepositorio.deleteAllInBatch();
    }

    @Test
    void usuariosCrudYListaVaciaFuncionan() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/api/usuarios/99999"))
                .andExpect(status().isNotFound());

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("nombre", "Ana");
        payload.put("email", "ana@test.com");
        payload.put("empresa", "Empresa Test");
        payload.put("contraseña", "secreta");
        payload.put("esEmpleado", true);

        String respuestaCreacion = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.idRestaurante").doesNotExist())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer usuarioId = objectMapper.readTree(respuestaCreacion).get("id").asInt();

        mockMvc.perform(get("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@test.com"));

        payload.put("nombre", "Ana Actualizada");
        payload.put("email", "ana.actualizada@test.com");

        mockMvc.perform(put("/api/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioId))
                .andExpect(jsonPath("$.nombre").value("Ana Actualizada"));

        mockMvc.perform(delete("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isNotFound());
    }

    @Test
    void restaurantesCrudYListaVaciaFuncionan() throws Exception {
        mockMvc.perform(get("/api/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/api/restaurantes/99999"))
                .andExpect(status().isNotFound());

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("nombre", "Casa Paco");
        payload.put("direccion", "Calle Mayor 1");
        payload.put("latitud", 40.42);
        payload.put("longitud", -3.70);
        payload.put("capacidadMaxima", 80);

        String respuestaCreacion = mockMvc.perform(post("/api/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Casa Paco"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer restauranteId = objectMapper.readTree(respuestaCreacion).get("id").asInt();

        mockMvc.perform(get("/api/restaurantes/{id}", restauranteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccion").value("Calle Mayor 1"));

        payload.put("direccion", "Calle Nueva 2");
        payload.put("capacidadMaxima", 100);

        mockMvc.perform(put("/api/restaurantes/{id}", restauranteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restauranteId))
                .andExpect(jsonPath("$.direccion").value("Calle Nueva 2"));

        mockMvc.perform(delete("/api/restaurantes/{id}", restauranteId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/restaurantes/{id}", restauranteId))
                .andExpect(status().isNotFound());
    }

    @Test
    void platosCrudYListaVaciaFuncionan() throws Exception {
        mockMvc.perform(get("/api/platos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/api/platos/99999"))
                .andExpect(status().isNotFound());

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("nombre", "Paella");
        payload.put("categoria", "Principal");
        payload.put("tipoCocina", "Mediterránea");

        String respuestaCreacion = mockMvc.perform(post("/api/platos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Paella"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer platoId = objectMapper.readTree(respuestaCreacion).get("id").asInt();

        mockMvc.perform(get("/api/platos/{id}", platoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria").value("Principal"));

        payload.put("categoria", "Arroz");

        mockMvc.perform(put("/api/platos/{id}", platoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(platoId))
                .andExpect(jsonPath("$.categoria").value("Arroz"));

        mockMvc.perform(delete("/api/platos/{id}", platoId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/platos/{id}", platoId))
                .andExpect(status().isNotFound());
    }

    @Test
    void menuDiarioCrudYListaVaciaFuncionan() throws Exception {
        mockMvc.perform(get("/api/menus-diarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/api/menus-diarios/99999"))
                .andExpect(status().isNotFound());

        Restaurante restaurante = crearRestaurantePersistido();

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("fecha", "2026-03-16");
        ObjectNode restauranteNode = payload.putObject("idRestaurante");
        restauranteNode.put("id", restaurante.getId());
        payload.put("urlImagen", "https://ejemplo.com/menu.jpg");
        payload.put("precioMenu", 13.95);

        String respuestaCreacion = mockMvc.perform(post("/api/menus-diarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.fecha").value("2026-03-16"))
                .andExpect(jsonPath("$.precioMenu").value(13.95))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer menuId = objectMapper.readTree(respuestaCreacion).get("id").asInt();

        mockMvc.perform(get("/api/menus-diarios/{id}", menuId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.urlImagen").value("https://ejemplo.com/menu.jpg"));

        payload.put("urlImagen", "https://ejemplo.com/menu-actualizado.jpg");
        payload.put("precioMenu", 15.5);

        mockMvc.perform(put("/api/menus-diarios/{id}", menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(menuId))
                .andExpect(jsonPath("$.precioMenu").value(15.5));

        mockMvc.perform(delete("/api/menus-diarios/{id}", menuId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/menus-diarios/{id}", menuId))
                .andExpect(status().isNotFound());
    }

    @Test
    void menuPlatoCrudYListaVaciaFuncionan() throws Exception {
        mockMvc.perform(get("/api/menu-platos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/api/menu-platos/99999/99999"))
                .andExpect(status().isNotFound());

        Restaurante restaurante = crearRestaurantePersistido();
        MenuDiario menuDiario = crearMenuDiarioPersistido(restaurante);
        Plato plato = crearPlatoPersistido();

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("idMenuDiario", menuDiario.getId());
        payload.put("idPlato", plato.getId());

        mockMvc.perform(post("/api/menu-platos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idMenuDiario").value(menuDiario.getId()))
                .andExpect(jsonPath("$.idPlato").value(plato.getId()));

        mockMvc.perform(get("/api/menu-platos/{idMenuDiario}/{idPlato}", menuDiario.getId(), plato.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMenuDiario").value(menuDiario.getId()))
                .andExpect(jsonPath("$.idPlato").value(plato.getId()));

        mockMvc.perform(delete("/api/menu-platos/{idMenuDiario}/{idPlato}", menuDiario.getId(), plato.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/menu-platos/{idMenuDiario}/{idPlato}", menuDiario.getId(), plato.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void valoracionesCrudYValidacionDePlatoFuncionan() throws Exception {
        mockMvc.perform(get("/api/valoraciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/api/valoraciones/99999"))
                .andExpect(status().isNotFound());

        Usuario usuario = crearUsuarioPersistido();
        Restaurante restaurante = crearRestaurantePersistido();
        Plato plato = crearPlatoPersistido();

        ObjectNode invalido = objectMapper.createObjectNode();
        invalido.put("idUsuario", usuario.getId());
        invalido.put("idRestaurante", restaurante.getId());
        invalido.put("puntuacion", 4);
        invalido.put("comentario", "Falta el plato");

        mockMvc.perform(post("/api/valoraciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido.toString()))
                .andExpect(status().isBadRequest());

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("idUsuario", usuario.getId());
        payload.put("idRestaurante", restaurante.getId());
        payload.put("idPlato", plato.getId());
        payload.put("puntuacion", 5);
        payload.put("comentario", "Muy bueno");

        String respuestaCreacion = mockMvc.perform(post("/api/valoraciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.idPlato").value(plato.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode creada = objectMapper.readTree(respuestaCreacion);
        Integer valoracionId = creada.get("id").asInt();

        mockMvc.perform(get("/api/valoraciones/{id}", valoracionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.puntuacion").value(5));

        payload.put("puntuacion", 3);
        payload.put("comentario", "Ha bajado un poco");

        mockMvc.perform(put("/api/valoraciones/{id}", valoracionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(valoracionId))
                .andExpect(jsonPath("$.puntuacion").value(3));

        mockMvc.perform(delete("/api/valoraciones/{id}", valoracionId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/valoraciones/{id}", valoracionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearRestauranteAsociaElUsuarioYElListadoDeUsuariosDevuelveIdRestaurante() throws Exception {
        Usuario usuario = crearUsuarioPersistido();

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("nombre", "Restaurante Empleado");
        payload.put("direccion", "Calle Unión 10");
        payload.put("latitud", 41.38);
        payload.put("longitud", 2.17);
        payload.put("capacidadMaxima", 60);
        payload.put("idUsuario", usuario.getId());

        String respuestaCreacion = mockMvc.perform(post("/api/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer restauranteId = objectMapper.readTree(respuestaCreacion).get("id").asInt();

        String listadoUsuarios = mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(usuario.getId()))
                .andExpect(jsonPath("$[0].idRestaurante").value(restauranteId))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(objectMapper.readTree(listadoUsuarios).get(0).get("idRestaurante").asInt()).isEqualTo(restauranteId);
    }

    private Usuario crearUsuarioPersistido() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Test");
        usuario.setEmail("usuario" + System.nanoTime() + "@test.com");
        usuario.setEmpresa("Empresa Test");
        usuario.setContraseña("password");
        usuario.setEsEmpleado(true);
        return usuarioRepositorio.save(usuario);
    }

    private Restaurante crearRestaurantePersistido() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNombre("Restaurante Test");
        restaurante.setDireccion("Calle Falsa 123");
        restaurante.setLatitud(new BigDecimal("40.42"));
        restaurante.setLongitud(new BigDecimal("-3.70"));
        restaurante.setCapacidadMaxima(50);
        return restauranteRepositorio.save(restaurante);
    }

    private Plato crearPlatoPersistido() {
        Plato plato = new Plato();
        plato.setNombre("Plato Test " + System.nanoTime());
        plato.setCategoria("Principal");
        plato.setTipoCocina("Casera");
        return platoRepositorio.save(plato);
    }

    private MenuDiario crearMenuDiarioPersistido(Restaurante restaurante) {
        MenuDiario menuDiario = new MenuDiario();
        menuDiario.setFecha(LocalDate.of(2026, 3, 16));
        menuDiario.setIdRestaurante(restaurante);
        menuDiario.setUrlImagen("https://ejemplo.com/base.jpg");
        menuDiario.setPrecioMenu(new BigDecimal("12.50"));
        return menuDiarioRepositorio.save(menuDiario);
    }
}

