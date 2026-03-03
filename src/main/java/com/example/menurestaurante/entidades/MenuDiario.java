package com.example.menurestaurante.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "menu_diario")
public class MenuDiario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_restaurante", nullable = false)
    private Restaurante idRestaurante;

    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;

    @ManyToMany
    private Set<Plato> platoes = new LinkedHashSet<>();


}