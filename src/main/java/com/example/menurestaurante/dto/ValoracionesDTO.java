package com.example.menurestaurante.dto;

import com.example.menurestaurante.entidades.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Recomendado para JPA
@AllArgsConstructor
@Builder
public class ValoracionesDTO {
    private Integer id;
    private Usuario idUsuario;
    private Integer idRestaurante;
    private Integer puntuacion;
    private String comentario;
}
