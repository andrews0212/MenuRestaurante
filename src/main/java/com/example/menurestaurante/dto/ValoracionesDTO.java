package com.example.menurestaurante.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValoracionesDTO {
    private Integer id;
    private Integer idUsuario;
    private Integer idRestaurante;
    private Integer idPlato;
    private Integer puntuacion;
    private String comentario;
}
