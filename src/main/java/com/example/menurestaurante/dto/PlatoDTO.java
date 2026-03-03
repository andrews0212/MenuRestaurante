package com.example.menurestaurante.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Recomendado para JPA
@AllArgsConstructor
@Builder
public class PlatoDTO {

    private Integer id;
    private String nombre;
    private String categoria;
    private String tipoCocina;

}
