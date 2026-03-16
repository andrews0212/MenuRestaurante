package com.example.menurestaurante.controlador;

import com.example.menurestaurante.dto.MenuExtraidoResponseDTO;
import com.example.menurestaurante.servicio.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ControladoresEndpointTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private PlatoService platoService;
    private RestauranteService restauranteService;
    private UsuarioService usuarioService;
    private MenuDiarioService menuDiarioService;
    private MenuPlatoService menuPlatoService;
    private ValoracionesService valoracionesService;
    private AzureDocumentIntelligenceService azureService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        platoService = mock(PlatoService.class);
        restauranteService = mock(RestauranteService.class);
        usuarioService = mock(UsuarioService.class);
        menuDiarioService = mock(MenuDiarioService.class);
        menuPlatoService = mock(MenuPlatoService.class);
        valoracionesService = mock(ValoracionesService.class);
        azureService = mock(AzureDocumentIntelligenceService.class);

        PlatoControlador platoControlador = new PlatoControlador();
        ReflectionTestUtils.setField(platoControlador, "platoService", platoService);

        RestauranteControlador restauranteControlador = new RestauranteControlador();
        ReflectionTestUtils.setField(restauranteControlador, "restauranteService", restauranteService);

        UsuarioControlador usuarioControlador = new UsuarioControlador();
        ReflectionTestUtils.setField(usuarioControlador, "usuarioService", usuarioService);

        MenuDiarioControlador menuDiarioControlador = new MenuDiarioControlador();
        ReflectionTestUtils.setField(menuDiarioControlador, "menuDiarioService", menuDiarioService);

        MenuPlatoControlador menuPlatoControlador = new MenuPlatoControlador();
        ReflectionTestUtils.setField(menuPlatoControlador, "menuPlatoService", menuPlatoService);

        ValoracionesControlador valoracionesControlador = new ValoracionesControlador();
        ReflectionTestUtils.setField(valoracionesControlador, "valoracionesService", valoracionesService);

        DocumentIntelligenceControlador documentControlador = new DocumentIntelligenceControlador();
        ReflectionTestUtils.setField(documentControlador, "azureDocumentIntelligenceService", azureService);

        mockMvc = MockMvcBuilders.standaloneSetup(
                platoControlador,
                restauranteControlador,
                usuarioControlador,
                menuDiarioControlador,
                menuPlatoControlador,
                valoracionesControlador,
                documentControlador
        ).build();
    }

    @Test
    void endpointsGetAllDevuelven200ConBdVacia() throws Exception {
        when(platoService.obtenerTodosPlatos()).thenReturn(List.of());
        when(restauranteService.obtenerTodosRestaurantes()).thenReturn(List.of());
        when(usuarioService.obtenerTodosUsuarios()).thenReturn(List.of());
        when(menuDiarioService.obtenerTodosMenusDiarios()).thenReturn(List.of());
        when(menuPlatoService.obtenerTodosMenuPlatos()).thenReturn(List.of());
        when(valoracionesService.obtenerTodasValoraciones()).thenReturn(List.of());

        mockMvc.perform(get("/api/platos")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        mockMvc.perform(get("/api/restaurantes")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        mockMvc.perform(get("/api/usuarios")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        mockMvc.perform(get("/api/menus-diarios")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        mockMvc.perform(get("/api/menu-platos")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        mockMvc.perform(get("/api/valoraciones")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
    }

    @Test
    void endpointsGetByIdDevuelven404SiNoExiste() throws Exception {
        when(platoService.obtenerPlato(anyInt())).thenThrow(new RuntimeException("No existe"));
        when(restauranteService.obtenerRestaurante(anyInt())).thenThrow(new RuntimeException("No existe"));
        when(usuarioService.obtenerUsuario(anyInt())).thenThrow(new RuntimeException("No existe"));
        when(menuDiarioService.obtenerMenuDiario(anyInt())).thenThrow(new RuntimeException("No existe"));
        when(menuPlatoService.obtenerMenuPlato(anyInt(), anyInt())).thenThrow(new RuntimeException("No existe"));
        when(valoracionesService.obtenerValoracion(anyInt())).thenThrow(new RuntimeException("No existe"));

        mockMvc.perform(get("/api/platos/999")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/restaurantes/999")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/usuarios/999")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/menus-diarios/999")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/menu-platos/999/999")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/valoraciones/999")).andExpect(status().isNotFound());
    }

    @Test
    void endpointsPostPutDeleteDevuelvenErroresControlados() throws Exception {
        when(platoService.insertarPlato(any())).thenThrow(new RuntimeException("Error"));
        when(platoService.actualizarPlato(any())).thenThrow(new RuntimeException("Error"));
        doThrow(new RuntimeException("Error")).when(platoService).eliminarPlato(anyInt());

        when(restauranteService.insertarRestaurante(any())).thenThrow(new RuntimeException("Error"));
        when(restauranteService.actualizarRestaurante(any())).thenThrow(new RuntimeException("Error"));
        doThrow(new RuntimeException("Error")).when(restauranteService).eliminarRestaurante(anyInt());

        when(usuarioService.insectarUsuario(any())).thenThrow(new RuntimeException("Error"));
        when(usuarioService.actualizarUsuario(any())).thenThrow(new RuntimeException("Error"));
        doThrow(new RuntimeException("Error")).when(usuarioService).eliminarUsuario(anyInt());

        when(menuDiarioService.insertarMenuDiario(any())).thenThrow(new RuntimeException("Error"));
        when(menuDiarioService.actualizarMenuDiario(any())).thenThrow(new RuntimeException("Error"));
        doThrow(new RuntimeException("Error")).when(menuDiarioService).eliminarMenuDiario(anyInt());

        when(menuPlatoService.insertarMenuPlato(any())).thenThrow(new RuntimeException("Error"));
        doThrow(new RuntimeException("Error")).when(menuPlatoService).eliminarMenuPlato(anyInt(), anyInt());

        when(valoracionesService.insertarValoracion(any())).thenThrow(new RuntimeException("Error"));
        when(valoracionesService.actualizarValoracion(any())).thenThrow(new RuntimeException("Error"));
        doThrow(new RuntimeException("Error")).when(valoracionesService).eliminarValoracion(anyInt());

        String jsonBase = "{}";

        mockMvc.perform(post("/api/platos").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/platos/1").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/platos/1")).andExpect(status().isNotFound());

        mockMvc.perform(post("/api/restaurantes").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/restaurantes/1").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/restaurantes/1")).andExpect(status().isNotFound());

        mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/usuarios/1").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/usuarios/1")).andExpect(status().isNotFound());

        mockMvc.perform(post("/api/menus-diarios").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/menus-diarios/1").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/menus-diarios/1")).andExpect(status().isNotFound());

        mockMvc.perform(post("/api/menu-platos").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/menu-platos/1/1")).andExpect(status().isNotFound());

        mockMvc.perform(post("/api/valoraciones").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/valoraciones/1").contentType(MediaType.APPLICATION_JSON).content(jsonBase)).andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/valoraciones/1")).andExpect(status().isNotFound());
    }

    @Test
    void documentIntelligenceValidacionesBasicas() throws Exception {
        MockMultipartFile archivoVacio = new MockMultipartFile("archivo", "menu.jpg", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/api/document-intelligence/analizar-menu").file(archivoVacio))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        mockMvc.perform(post("/api/document-intelligence/analizar-menu-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("urlImagen", ""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        mockMvc.perform(multipart("/api/document-intelligence/analizar-y-guardar/1").file(archivoVacio))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void documentIntelligenceEndpointsFelices() throws Exception {
        MockMultipartFile archivo = new MockMultipartFile("archivo", "menu.jpg", "image/jpeg", "contenido".getBytes());

        MenuExtraidoResponseDTO respuesta = MenuExtraidoResponseDTO.builder()
                .mensaje("ok")
                .totalPlatosExtraidos(0)
                .build();

        when(azureService.analizarMenuDesdeArchivo(any())).thenReturn(respuesta);
        when(azureService.analizarMenuDesdeUrl(anyString())).thenReturn(respuesta);
        when(azureService.analizarYGuardarMenu(any(), anyInt(), any())).thenReturn(respuesta);

        mockMvc.perform(multipart("/api/document-intelligence/analizar-menu").file(archivo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("ok"));

        mockMvc.perform(post("/api/document-intelligence/analizar-menu-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("urlImagen", "https://ejemplo.com/menu.jpg"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("ok"));

        mockMvc.perform(multipart("/api/document-intelligence/analizar-y-guardar/1")
                        .file(archivo)
                        .param("urlImagen", "https://ejemplo.com/menu.jpg"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("ok"));
    }
}

