package com.example.menurestaurante.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu_plato")
public class MenuPlato {
    @EmbeddedId
    private MenuPlatoId id;

    @MapsId("idMenuDiario")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_menu_diario", nullable = false)
    private MenuDiario idMenuDiario;

    @MapsId("idPlato")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_plato", nullable = false)
    private Plato idPlato;


}