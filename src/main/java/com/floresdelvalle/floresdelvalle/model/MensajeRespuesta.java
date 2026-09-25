package com.floresdelvalle.floresdelvalle.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "MensajeRespuesta",
    description = "Respuesta de confirmación de una operación."
)
public class MensajeRespuesta {

    @Schema(
        description = "Descripción del resultado de la operación.",
        example = "Estado actualizado correctamente."
    )
    private String mensaje;

    public MensajeRespuesta() {
    }

    public MensajeRespuesta(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}