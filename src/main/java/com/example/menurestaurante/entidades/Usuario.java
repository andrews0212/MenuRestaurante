package com.example.menurestaurante.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "empresa", nullable = false)
    private String empresa;

    @Column(name = "`contraseña`", nullable = false)
    private String contraseña;

    @Column(name = "es_empleado", nullable = false)
    private Boolean esEmpleado;


}