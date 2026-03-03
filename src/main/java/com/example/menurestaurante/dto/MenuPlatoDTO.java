package com.example.menurestaurante.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Recomendado para JPA
@AllArgsConstructor
@Builder
public class MenuPlatoDTO {

    private Integer id;
    private Integer idMenuDiario;
    private Integer idPlato;
}
