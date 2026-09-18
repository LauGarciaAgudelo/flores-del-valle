package com.floresdelvalle.floresdelvalle.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class PedidoFormulario {

    private String nombreCliente;
    private String direccion;
    private String contacto;
    private Long arregloId;
    private String ocasion;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaEntrega;

    private Integer presupuesto;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Long getArregloId() {
        return arregloId;
    }

    public void setArregloId(Long arregloId) {
        this.arregloId = arregloId;
    }

    public String getOcasion() {
        return ocasion;
    }

    public void setOcasion(String ocasion) {
        this.ocasion = ocasion;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Integer getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Integer presupuesto) {
        this.presupuesto = presupuesto;
    }
}