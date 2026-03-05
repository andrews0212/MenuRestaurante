package com.example.menurestaurante.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor // Recomendado para JPA
@AllArgsConstructor
@Builder
public class RestauranteDTO{
    private Integer id;
    private String nombre;
    private String direccion;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private Integer capacidadMaxima;
    private Integer idUsuario;
}
