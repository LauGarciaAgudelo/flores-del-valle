package com.floresdelvalle.floresdelvalle.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Flores del Valle API",
        version = "1.0.0",
        description = """
            API REST para la gestión de la floristería Flores del Valle.

            Permite consultar, registrar, actualizar y eliminar pedidos,
            además de gestionar el estado de cada solicitud.
            """,
        contact = @Contact(
            name = "Flores del Valle",
            email = "contacto@floresdelvalle.com"
        ),
        license = @License(
            name = "Uso académico"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Servidor local"
        )
    }
)
public class OpenApiConfig {
}
