package com.example.menurestaurante.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta con toda la información extraída del documento de menú
 * usando el modelo custom de Azure Document Intelligence.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuExtraidoResponseDTO {

    private String mensaje;

    // Campos extraídos del modelo custom
    private String fechaMenu;
    private String tipoMenu;
    private String precioMenu1;
    private String precioMenu2;
    private String notaMenu;
    private String tlfRestaurante;

    // Texto completo extraído del documento
    private String textoExtraido;

    // Platos y postres extraídos
    private int totalPlatosExtraidos;
    private List<PlatoExtraidoDTO> platosExtraidos;
    private List<PlatoExtraidoDTO> postresExtraidos;
}

