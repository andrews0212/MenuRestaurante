package com.example.menurestaurante.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class MenuPlatoId implements Serializable {
    private static final long serialVersionUID = -4899308703727936215L;
    @Column(name = "id_menu_diario", nullable = false)
    private Integer idMenuDiario;

    @Column(name = "id_plato", nullable = false)
    private Integer idPlato;


}