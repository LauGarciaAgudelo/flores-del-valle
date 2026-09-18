package com.floresdelvalle.floresdelvalle.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "arreglos")
public class Arreglo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer precioBasico;
    private Integer precioEspecial;
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