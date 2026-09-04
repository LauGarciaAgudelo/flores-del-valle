package com.floresdelvalle.floresdelvalle.model;

public enum EstadoPedido {

    EN_CURSO("En curso"),
    COMPLETADO("Completado"),
    ENTREGADO("Entregado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

