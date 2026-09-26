package com.floresdelvalle.floresdelvalle.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "EstadoPedidoSolicitud",
    description = "Información requerida para cambiar el estado de un pedido."
)
public class EstadoPedidoSolicitud {

    @Schema(
        description = "Nuevo estado del pedido.",
        example = "COMPLETADO",
        allowableValues = {
            "EN_CURSO",
            "COMPLETADO",
            "ENTREGADO"
        },
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String estado;

    public EstadoPedidoSolicitud() {
    }

    public EstadoPedidoSolicitud(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}