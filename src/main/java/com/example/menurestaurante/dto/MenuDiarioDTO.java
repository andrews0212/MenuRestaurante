package com.example.menurestaurante.dto;

import com.example.menurestaurante.entidades.Restaurante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor // Recomendado para JPA
@AllArgsConstructor
@Builder
public class MenuDiarioDTO {

    private Integer id;
    private LocalDate fecha;
    private Restaurante idRestaurante;
    private String urlImagen;
    private List<PlatoDTO> platos;
    private BigDecimal precioMenu;
}
