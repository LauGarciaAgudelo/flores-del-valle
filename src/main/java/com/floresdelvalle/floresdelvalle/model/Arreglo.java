package com.floresdelvalle.floresdelvalle.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "arreglos")
@Schema(
    name = "Arreglo",
    description = """
        Arreglo floral disponible en el catálogo,
        con sus tres opciones de precio.
        """
)
public class Arreglo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Identificador único del arreglo.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre comercial del arreglo.",
        example = "Ramo de rosas"
    )
    private String nombre;

    @Schema(
        description = "Precio de la presentación básica.",
        example = "75000",
        minimum = "1"
    )
    private Integer precioBasico;

    @Schema(
        description = "Precio de la presentación especial.",
        example = "95000",
        minimum = "1"
    )
    private Integer precioEspecial;

    @Schema(
        description = "Precio de la presentación premium.",
        example = "125000",
        minimum = "1"
    )
    private Integer precioPremium;

    public Arreglo() {
    }

    public Arreglo(
            String nombre,
            Integer precioBasico,
            Integer precioEspecial,
            Integer precioPremium
    ) {
        this.nombre = nombre;
        this.precioBasico = precioBasico;
        this.precioEspecial = precioEspecial;
        this.precioPremium = precioPremium;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrecioBasico() {
        return precioBasico;
    }

    public void setPrecioBasico(Integer precioBasico) {
        this.precioBasico = precioBasico;
    }

    public Integer getPrecioEspecial() {
        return precioEspecial;
    }

    public void setPrecioEspecial(Integer precioEspecial) {
        this.precioEspecial = precioEspecial;
    }

    public Integer getPrecioPremium() {
        return precioPremium;
    }

    public void setPrecioPremium(Integer precioPremium) {
        this.precioPremium = precioPremium;
    }

    public boolean incluyePrecio(Integer precio) {
        return precio != null
            && (
                precio.equals(precioBasico)
                || precio.equals(precioEspecial)
                || precio.equals(precioPremium)
            );
    }
}