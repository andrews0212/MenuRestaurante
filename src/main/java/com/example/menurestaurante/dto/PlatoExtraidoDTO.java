package com.example.menurestaurante.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un plato extraído por Azure Document Intelligence
 * a partir de una imagen o documento de menú de restaurante.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatoExtraidoDTO {

    private String nombre;
    private String categoria;   // Primero, Segundo, Postre, Entrante, etc.
    private String descripcion;
    private String precio;
}

