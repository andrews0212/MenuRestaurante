package com.example.menurestaurante.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "restaurante")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "Direccion", nullable = false)
    private String direccion;

    @Column(name = "latitud", nullable = false, precision = 10, scale = 2)
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false, precision = 10, scale = 2)
    private BigDecimal longitud;

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;


}