package com.example.menurestaurante.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Recomendado para JPA
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Integer id;
    private String nombre;
    private String email;
    private String empresa;
    private String contraseña;
    private Boolean esEmpleado;
}
