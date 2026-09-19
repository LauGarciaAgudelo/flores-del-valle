package com.floresdelvalle.floresdelvalle.model;

public class CatalogoItem {

    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final String imagen;
    private final Integer precioBasico;
    private final Integer precioEspecial;
    private final Integer precioPremium;

    public CatalogoItem(
            Long id,
            String nombre,
            String descripcion,
            String imagen,
            Integer precioBasico,
            Integer precioEspecial,
            Integer precioPremium
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
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

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public Integer getPrecioBasico() {
        return precioBasico;
    }

    public Integer getPrecioEspecial() {
        return precioEspecial;
    }

    public Integer getPrecioPremium() {
        return precioPremium;
    }
}