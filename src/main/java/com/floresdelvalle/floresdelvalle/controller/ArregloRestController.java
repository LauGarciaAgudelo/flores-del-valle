package com.floresdelvalle.floresdelvalle.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floresdelvalle.floresdelvalle.model.Arreglo;
import com.floresdelvalle.floresdelvalle.model.ErrorRespuesta;
import com.floresdelvalle.floresdelvalle.repository.ArregloRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/arreglos")
@Tag(
    name = "Arreglos",
    description = """
        Consultas del catálogo de arreglos florales
        y sus opciones de precio.
        """
)
public class ArregloRestController {

    private final ArregloRepository arregloRepository;

    public ArregloRestController(
            ArregloRepository arregloRepository
    ) {
        this.arregloRepository = arregloRepository;
    }

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Consultar todos los arreglos",
        description = """
            Retorna los arreglos disponibles en el catálogo,
            ordenados alfabéticamente por nombre.

            Esta consulta permite conocer el arregloId y los
            precios válidos necesarios para registrar un pedido.

            Rol objetivo: administrador, empleado o cliente.
            """
    )
    @ApiResponse(
        responseCode = "200",
        description = "Arreglos consultados correctamente.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(
                schema = @Schema(
                    implementation = Arreglo.class
                )
            )
        )
    )
    public ResponseEntity<List<Arreglo>>
            consultarArreglos() {

        List<Arreglo> arreglos =
            arregloRepository.findAllByOrderByNombreAsc();

        return ResponseEntity.ok(arreglos);
    }

    @GetMapping(
        value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Consultar un arreglo por ID",
        description = """
            Busca un arreglo floral utilizando su identificador.

            Rol objetivo: administrador, empleado o cliente.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Arreglo encontrado.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = Arreglo.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El arreglo no existe.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ErrorRespuesta.class
                )
            )
        )
    })
    public ResponseEntity<?> consultarArreglo(
            @Parameter(
                description = "Identificador del arreglo.",
                example = "1",
                required = true
            )
            @PathVariable Long id
    ) {
        Optional<Arreglo> arreglo =
            arregloRepository.findById(id);

        if (arreglo.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    new ErrorRespuesta(
                        "El arreglo no existe."
                    )
                );
        }

        return ResponseEntity.ok(
            arreglo.get()
        );
    }
}