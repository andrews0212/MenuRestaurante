package com.example.menurestaurante.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Recomendado para JPA
@AllArgsConstructor
@Builder
public class MenuPlatoIdDTO {

    private Integer idMenuDiario;
    private Integer idPlato;
}

