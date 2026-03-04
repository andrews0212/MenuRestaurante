package com.example.menurestaurante.controlador;

import com.example.menurestaurante.dto.MenuExtraidoResponseDTO;
import com.example.menurestaurante.servicio.AzureDocumentIntelligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Controlador REST para la integración con Azure Document Intelligence.
 * Permite analizar imágenes/documentos de menús de restaurante y extraer
 * automáticamente los platos del menú del día.
 */
@RestController
@RequestMapping("/api/document-intelligence")
@CrossOrigin(origins = "*")
public class DocumentIntelligenceControlador {

    @Autowired
    private AzureDocumentIntelligenceService azureDocumentIntelligenceService;

    /**
     * POST - Analizar una imagen/documento de menú subida como archivo.
     * Extrae los platos del menú sin guardarlos en la BD.
     *
     * Ejemplo: POST /api/document-intelligence/analizar-menu
     * Body: form-data con campo "archivo" (imagen/pdf del menú)
     */
    @PostMapping(value = "/analizar-menu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> analizarMenuDesdeArchivo(@RequestParam("archivo") MultipartFile archivo) {
        try {
            // Validar que el archivo no esté vacío
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El archivo está vacío. Por favor sube una imagen o PDF del menú."));
            }

            // Validar tipo de archivo
            String contentType = archivo.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Tipo de archivo no soportado. Se acepta: imagen (JPEG, PNG) o PDF."));
            }

            MenuExtraidoResponseDTO resultado = azureDocumentIntelligenceService.analizarMenuDesdeArchivo(archivo);
            return ResponseEntity.ok(resultado);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST - Analizar un menú desde una URL pública de imagen.
     * Extrae los platos del menú sin guardarlos en la BD.
     *
     * Ejemplo: POST /api/document-intelligence/analizar-menu-url
     * Body JSON: { "urlImagen": "https://ejemplo.com/menu.jpg" }
     */
    @PostMapping("/analizar-menu-url")
    public ResponseEntity<?> analizarMenuDesdeUrl(@RequestBody Map<String, String> request) {
        try {
            String urlImagen = request.get("urlImagen");

            if (urlImagen == null || urlImagen.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "La URL de la imagen es obligatoria."));
            }

            MenuExtraidoResponseDTO resultado = azureDocumentIntelligenceService.analizarMenuDesdeUrl(urlImagen);
            return ResponseEntity.ok(resultado);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST - Analizar una imagen de menú, extraer los platos y guardarlos
     * automáticamente en la BD como menú diario del restaurante.
     *
     * Ejemplo: POST /api/document-intelligence/analizar-y-guardar/{restauranteId}
     * Body: form-data con campo "archivo" (imagen/pdf del menú)
     *       y opcionalmente "urlImagen" (URL donde se almacenó la imagen)
     */
    @PostMapping(value = "/analizar-y-guardar/{restauranteId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> analizarYGuardarMenu(
            @PathVariable Integer restauranteId,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(value = "urlImagen", required = false) String urlImagen) {
        try {
            // Validar archivo
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El archivo está vacío. Por favor sube una imagen o PDF del menú."));
            }

            String contentType = archivo.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Tipo de archivo no soportado. Se acepta: imagen (JPEG, PNG) o PDF."));
            }

            MenuExtraidoResponseDTO resultado = azureDocumentIntelligenceService
                    .analizarYGuardarMenu(archivo, restauranteId, urlImagen);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

