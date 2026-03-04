package com.example.menurestaurante.servicio;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClientBuilder;
import com.azure.ai.documentintelligence.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.example.menurestaurante.dto.MenuExtraidoResponseDTO;
import com.example.menurestaurante.dto.PlatoExtraidoDTO;
import com.example.menurestaurante.entidades.*;
import com.example.menurestaurante.repositorio.MenuDiarioRepositorio;
import com.example.menurestaurante.repositorio.MenuPlatoRepositorio;
import com.example.menurestaurante.repositorio.PlatoRepositorio;
import com.example.menurestaurante.repositorio.RestauranteRepositorio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servicio que utiliza Azure Document Intelligence con un modelo custom entrenado
 * para analizar imágenes de menús de restaurante y extraer los platos automáticamente.
 *
 * Etiquetas del modelo custom:
 * - FechaMenu, TipoMenu, PrecioMenu1, PrecioMenu2, NotaMenu, TlfRestaurante
 * - Plato1, Plato2, Plato3, ... (platos principales)
 * - Postre1, Postre2, Postre3, ... (postres)
 */
@Service
public class AzureDocumentIntelligenceService {

    @Value("${azure.document.intelligence.endpoint}")
    private String endpoint;

    @Value("${azure.document.intelligence.key}")
    private String apiKey;

    @Value("${azure.document.intelligence.model-id}")
    private String modelId;

    private DocumentIntelligenceClient client;

    @Autowired
    private PlatoRepositorio platoRepositorio;

    @Autowired
    private MenuDiarioRepositorio menuDiarioRepositorio;

    @Autowired
    private MenuPlatoRepositorio menuPlatoRepositorio;

    @Autowired
    private RestauranteRepositorio restauranteRepositorio;

    @PostConstruct
    public void init() {
        this.client = new DocumentIntelligenceClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(apiKey))
                .buildClient();
    }

    // ======================== MÉTODOS PÚBLICOS ========================

    /**
     * Analiza una imagen de menú subida como archivo usando el modelo custom.
     * Extrae los campos etiquetados y el texto completo sin guardar en BD.
     */
    public MenuExtraidoResponseDTO analizarMenuDesdeArchivo(MultipartFile archivo) {
        try {
            byte[] contenido = archivo.getBytes();

            AnalyzeDocumentRequest analyzeRequest = new AnalyzeDocumentRequest();
            analyzeRequest.setBase64Source(contenido);

            AnalyzeResult resultado = client.beginAnalyzeDocument(
                    modelId,
                    null, null, null, null, null, null, null,
                    analyzeRequest
            ).getFinalResult();

            return extraerDatosDelModeloCustom(resultado);

        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo subido: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al analizar el documento con Azure Document Intelligence: " + e.getMessage(), e);
        }
    }

    /**
     * Analiza un menú desde una URL pública de imagen usando el modelo custom.
     */
    public MenuExtraidoResponseDTO analizarMenuDesdeUrl(String urlImagen) {
        try {
            AnalyzeDocumentRequest analyzeRequest = new AnalyzeDocumentRequest();
            analyzeRequest.setUrlSource(urlImagen);

            AnalyzeResult resultado = client.beginAnalyzeDocument(
                    modelId,
                    null, null, null, null, null, null, null,
                    analyzeRequest
            ).getFinalResult();

            return extraerDatosDelModeloCustom(resultado);

        } catch (Exception e) {
            throw new RuntimeException("Error al analizar la imagen desde URL con Azure Document Intelligence: " + e.getMessage(), e);
        }
    }

    /**
     * Analiza una imagen de menú, extrae los platos con el modelo custom,
     * los guarda en la BD y crea un menú diario asociado al restaurante.
     * También guarda el precio extraído del menú.
     */
    public MenuExtraidoResponseDTO analizarYGuardarMenu(MultipartFile archivo, Integer restauranteId, String urlImagen) {
        // 1. Verificar que el restaurante existe
        Restaurante restaurante = restauranteRepositorio.findById(restauranteId)
                .orElseThrow(() -> new RuntimeException("Error: El restaurante con ID " + restauranteId + " no existe."));

        // 2. Analizar la imagen con el modelo custom de Azure
        MenuExtraidoResponseDTO resultadoAnalisis = analizarMenuDesdeArchivo(archivo);

        List<PlatoExtraidoDTO> todosLosPlatos = new ArrayList<>();
        if (resultadoAnalisis.getPlatosExtraidos() != null) {
            todosLosPlatos.addAll(resultadoAnalisis.getPlatosExtraidos());
        }
        if (resultadoAnalisis.getPostresExtraidos() != null) {
            todosLosPlatos.addAll(resultadoAnalisis.getPostresExtraidos());
        }

        if (todosLosPlatos.isEmpty()) {
            throw new RuntimeException("No se pudieron extraer platos del documento proporcionado.");
        }

        // 3. Crear el menú diario con el precio extraído
        MenuDiario menuDiario = new MenuDiario();
        menuDiario.setFecha(LocalDate.now());
        menuDiario.setIdRestaurante(restaurante);
        menuDiario.setUrlImagen(urlImagen != null ? urlImagen : "sin-imagen");

        // Extraer precio del menú y guardarlo (intentar PrecioMenu1, si no PrecioMenu2)
        BigDecimal precio = parsearPrecio(resultadoAnalisis.getPrecioMenu1());
        if (precio == null) {
            precio = parsearPrecio(resultadoAnalisis.getPrecioMenu2());
        }
        menuDiario.setPrecioMenu(precio);

        menuDiario = menuDiarioRepositorio.save(menuDiario);

        // 4. Guardar cada plato extraído y asociarlo al menú diario
        //    Si el plato ya existe en la BD (por nombre), se reutiliza; si no, se crea nuevo
        for (PlatoExtraidoDTO platoExtraido : todosLosPlatos) {
            String nombrePlato = platoExtraido.getNombre();

            // Buscar si ya existe un plato con ese nombre
            Plato plato = platoRepositorio.findByNombreIgnoreCase(nombrePlato)
                    .orElseGet(() -> {
                        // No existe, crear uno nuevo
                        Plato nuevoPlato = new Plato();
                        nuevoPlato.setNombre(nombrePlato);
                        nuevoPlato.setCategoria(platoExtraido.getCategoria() != null ? platoExtraido.getCategoria() : "Sin categoría");
                        nuevoPlato.setTipoCocina("Menú del día");
                        return platoRepositorio.save(nuevoPlato);
                    });

            MenuPlatoId menuPlatoId = new MenuPlatoId();
            menuPlatoId.setIdMenuDiario(menuDiario.getId());
            menuPlatoId.setIdPlato(plato.getId());

            MenuPlato menuPlato = new MenuPlato();
            menuPlato.setId(menuPlatoId);
            menuPlato.setIdMenuDiario(menuDiario);
            menuPlato.setIdPlato(plato);
            menuPlatoRepositorio.save(menuPlato);
        }

        return MenuExtraidoResponseDTO.builder()
                .mensaje("Menú del día creado correctamente para el restaurante '" + restaurante.getNombre()
                        + "'. Se guardaron " + todosLosPlatos.size() + " platos. Precio: " + (precio != null ? precio + "€" : "no detectado"))
                .fechaMenu(resultadoAnalisis.getFechaMenu())
                .tipoMenu(resultadoAnalisis.getTipoMenu())
                .precioMenu1(resultadoAnalisis.getPrecioMenu1())
                .precioMenu2(resultadoAnalisis.getPrecioMenu2())
                .notaMenu(resultadoAnalisis.getNotaMenu())
                .tlfRestaurante(resultadoAnalisis.getTlfRestaurante())
                .textoExtraido(resultadoAnalisis.getTextoExtraido())
                .totalPlatosExtraidos(todosLosPlatos.size())
                .platosExtraidos(resultadoAnalisis.getPlatosExtraidos())
                .postresExtraidos(resultadoAnalisis.getPostresExtraidos())
                .build();
    }

    // ======================== LÓGICA DE EXTRACCIÓN ========================

    /**
     * Extrae todos los datos del resultado del modelo custom de Azure Document Intelligence.
     * Lee los campos etiquetados del documento Y también el texto completo de las páginas.
     */
    private MenuExtraidoResponseDTO extraerDatosDelModeloCustom(AnalyzeResult resultado) {
        List<PlatoExtraidoDTO> platos = new ArrayList<>();
        List<PlatoExtraidoDTO> postres = new ArrayList<>();

        String fechaMenu = null;
        String tipoMenu = null;
        String precioMenu1 = null;
        String precioMenu2 = null;
        String notaMenu = null;
        String tlfRestaurante = null;

        // 1. Extraer campos etiquetados del modelo custom
        if (resultado != null && resultado.getDocuments() != null) {
            for (Document documento : resultado.getDocuments()) {
                Map<String, DocumentField> campos = documento.getFields();
                if (campos == null) continue;

                fechaMenu = obtenerValorCampo(campos, "FechaMenu");
                tipoMenu = obtenerValorCampo(campos, "TipoMenu");
                precioMenu1 = obtenerValorCampo(campos, "PrecioMenu1");
                precioMenu2 = obtenerValorCampo(campos, "PrecioMenu2");
                notaMenu = obtenerValorCampo(campos, "NotaMenu");
                tlfRestaurante = obtenerValorCampo(campos, "TlfRestaurante");

                // Platos: Plato1, Plato2, Plato3, ...
                for (int i = 1; i <= 20; i++) {
                    String valorPlato = obtenerValorCampo(campos, "Plato" + i);
                    if (valorPlato != null && !valorPlato.isBlank()) {
                        platos.add(PlatoExtraidoDTO.builder()
                                .nombre(limpiarNombrePlato(valorPlato))
                                .categoria("Plato")
                                .descripcion(null)
                                .precio(null)
                                .build());
                    }
                }

                // Postres: Postre1, Postre2, Postre3, ...
                for (int i = 1; i <= 10; i++) {
                    String valorPostre = obtenerValorCampo(campos, "Postre" + i);
                    if (valorPostre != null && !valorPostre.isBlank()) {
                        postres.add(PlatoExtraidoDTO.builder()
                                .nombre(limpiarNombrePlato(valorPostre))
                                .categoria("Postre")
                                .descripcion(null)
                                .precio(null)
                                .build());
                    }
                }
            }
        }

        // 2. Extraer texto completo de todas las páginas
        String textoExtraido = extraerTextoDeResultado(resultado);

        int total = platos.size() + postres.size();

        return MenuExtraidoResponseDTO.builder()
                .mensaje("Menú analizado correctamente. Se extrajeron " + total + " platos"
                        + " (" + platos.size() + " platos + " + postres.size() + " postres).")
                .fechaMenu(fechaMenu)
                .tipoMenu(tipoMenu)
                .precioMenu1(precioMenu1)
                .precioMenu2(precioMenu2)
                .notaMenu(notaMenu)
                .tlfRestaurante(tlfRestaurante)
                .textoExtraido(textoExtraido)
                .totalPlatosExtraidos(total)
                .platosExtraidos(platos)
                .postresExtraidos(postres)
                .build();
    }

    /**
     * Extrae todo el texto de las páginas del documento analizado.
     * Concatena línea por línea el contenido de cada página.
     */
    private String extraerTextoDeResultado(AnalyzeResult resultado) {
        if (resultado == null) return null;

        // Si el resultado tiene content global, devolverlo directamente
        if (resultado.getContent() != null && !resultado.getContent().isBlank()) {
            return resultado.getContent();
        }

        // Si no, concatenar líneas de cada página
        if (resultado.getPages() == null) return null;

        StringBuilder textoCompleto = new StringBuilder();
        for (DocumentPage pagina : resultado.getPages()) {
            if (pagina.getLines() == null) continue;
            for (DocumentLine linea : pagina.getLines()) {
                String contenidoLinea = linea.getContent();
                if (contenidoLinea != null && !contenidoLinea.isBlank()) {
                    textoCompleto.append(contenidoLinea.trim()).append("\n");
                }
            }
            textoCompleto.append("\n"); // Separar páginas
        }

        String texto = textoCompleto.toString().trim();
        return texto.isEmpty() ? null : texto;
    }

    /**
     * Obtiene el valor de texto de un campo del documento analizado.
     * Soporta campos de tipo STRING y DATE.
     */
    private String obtenerValorCampo(Map<String, DocumentField> campos, String nombreCampo) {
        DocumentField campo = campos.get(nombreCampo);
        if (campo == null) {
            return null;
        }

        String contenido = campo.getContent();
        if (contenido != null && !contenido.isBlank()) {
            return contenido.trim();
        }

        if (campo.getType() == DocumentFieldType.STRING) {
            return campo.getValueString();
        }
        if (campo.getType() == DocumentFieldType.DATE) {
            return campo.getValueDate() != null ? campo.getValueDate().toString() : null;
        }

        return null;
    }

    /**
     * Limpia el nombre de un plato extraído (quita asteriscos, guiones iniciales, etc.)
     */
    private String limpiarNombrePlato(String nombre) {
        if (nombre == null) return null;
        return nombre
                .replaceAll("^[*\\-•·\\s]+", "")
                .replaceAll("[*\\-•·\\s]+$", "")
                .trim();
    }

    /**
     * Convierte un texto de precio extraído (ej: "13'00€", "12,50€", "10.00") a BigDecimal.
     */
    private BigDecimal parsearPrecio(String precioTexto) {
        if (precioTexto == null || precioTexto.isBlank()) return null;
        try {
            // Limpiar: quitar €, espacios, y normalizar separadores
            String limpio = precioTexto
                    .replaceAll("[€$\\s]", "")       // Quitar símbolo moneda y espacios
                    .replace("'", ".")               // 13'00 -> 13.00
                    .replace(",", ".");              // 13,00 -> 13.00
            return new BigDecimal(limpio);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

