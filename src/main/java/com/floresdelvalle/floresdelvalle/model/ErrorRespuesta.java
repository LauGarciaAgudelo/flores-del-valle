package com.floresdelvalle.floresdelvalle.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "ErrorRespuesta",
    description = "Respuesta generada cuando una operación no puede completarse."
)
public class ErrorRespuesta {

    @Schema(
        description = "Descripción del error encontrado.",
        example = "El pedido no existe."
    )
    private String error;

    public ErrorRespuesta() {
    }

    public ErrorRespuesta(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}