package com.floresdelvalle.floresdelvalle.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "PedidoSolicitud",
    description = """
        Datos necesarios para registrar o actualizar completamente
        un pedido de la floristería.
        """
)
public class PedidoFormulario {

    @Schema(
        description = "Nombre y apellido del cliente.",
        example = "Laura Martínez",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombreCliente;

    @Schema(
        description = "Dirección en la que se entregará el pedido.",
        example = "Carrera 32 # 18-25",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String direccion;

    @Schema(
        description = "Número de celular colombiano de diez dígitos.",
        example = "3004567890",
        pattern = "3[0-9]{9}",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String contacto;

    @Schema(
        description = "Identificador del arreglo seleccionado.",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long arregloId;

    @Schema(
        description = "Ocasión para la cual se solicita el arreglo.",
        example = "Cumpleaños",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String ocasion;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(
        description = "Fecha programada para la entrega.",
        example = "2026-10-15",
        type = "string",
        format = "date",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate fechaEntrega;

    @Schema(
        description = """
            Precio seleccionado. Debe coincidir con una de las
            opciones disponibles para el arreglo.
            """,
        example = "95000",
        minimum = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer presupuesto;

    public PedidoFormulario() {
    }

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