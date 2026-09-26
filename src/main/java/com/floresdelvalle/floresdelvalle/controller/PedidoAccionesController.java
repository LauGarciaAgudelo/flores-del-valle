package com.floresdelvalle.floresdelvalle.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floresdelvalle.floresdelvalle.model.Arreglo;
import com.floresdelvalle.floresdelvalle.model.ErrorRespuesta;
import com.floresdelvalle.floresdelvalle.model.EstadoPedido;
import com.floresdelvalle.floresdelvalle.model.EstadoPedidoSolicitud;
import com.floresdelvalle.floresdelvalle.model.MensajeRespuesta;
import com.floresdelvalle.floresdelvalle.model.Pedido;
import com.floresdelvalle.floresdelvalle.model.PedidoFormulario;
import com.floresdelvalle.floresdelvalle.repository.ArregloRepository;
import com.floresdelvalle.floresdelvalle.repository.PedidoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pedidos")
@Tag(
    name = "Pedidos",
    description = "Operaciones para gestionar pedidos de la floristería."
)
public class PedidoAccionesController {

    private final PedidoRepository pedidoRepository;
    private final ArregloRepository arregloRepository;

    public PedidoAccionesController(
            PedidoRepository pedidoRepository,
            ArregloRepository arregloRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.arregloRepository = arregloRepository;
    }

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Consultar todos los pedidos",
        description = """
            Retorna todos los pedidos registrados, ordenados
            por fecha de entrega.

            Rol objetivo: administrador o empleado.
            """
    )
    @ApiResponse(
        responseCode = "200",
        description = "Pedidos consultados correctamente.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(
                schema = @Schema(
                    implementation = Pedido.class
                )
            )
        )
    )
    public ResponseEntity<List<Pedido>> consultarPedidos() {
        List<Pedido> pedidos =
            pedidoRepository.findAllByOrderByFechaEntregaAsc();

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping(
        value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Consultar un pedido por ID",
        description = """
            Busca un pedido mediante su identificador.

            Rol objetivo: administrador o empleado.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Pedido encontrado.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = Pedido.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El pedido no existe.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ErrorRespuesta.class
                )
            )
        )
    })
    public ResponseEntity<?> consultarPedido(
            @Parameter(
                description = "Identificador del pedido.",
                example = "1",
                required = true
            )
            @PathVariable Long id
    ) {
        Optional<Pedido> pedido =
            pedidoRepository.findById(id);

        if (pedido.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    new ErrorRespuesta(
                        "El pedido no existe."
                    )
                );
        }

        return ResponseEntity.ok(
            pedido.get()
        );
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Registrar un pedido",
        description = """
            Registra un pedido utilizando un arreglo existente
            y uno de sus precios disponibles. El pedido se crea
            inicialmente con estado EN_CURSO.

            Rol objetivo: administrador o empleado.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Pedido registrado correctamente.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = Pedido.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Los datos enviados no son válidos.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ErrorRespuesta.class
                )
            )
        )
    })
    public ResponseEntity<?> crearPedido(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del pedido nuevo.",
                required = true,
                content = @Content(
                    mediaType =
                        MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                        implementation =
                            PedidoFormulario.class
                    )
                )
            )
            @RequestBody PedidoFormulario formulario
    ) {
        String error = validarFormulario(formulario);

        if (error != null) {
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorRespuesta(error)
                );
        }

        Optional<Arreglo> arregloEncontrado =
            arregloRepository.findById(
                formulario.getArregloId()
            );

        if (arregloEncontrado.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorRespuesta(
                        "El arreglo seleccionado no existe."
                    )
                );
        }

        Arreglo arreglo = arregloEncontrado.get();

        if (!arreglo.incluyePrecio(
                formulario.getPresupuesto()
            )) {
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorRespuesta(
                        "El precio no corresponde al arreglo."
                    )
                );
        }

        Pedido pedido = new Pedido(
            formulario.getNombreCliente().trim(),
            formulario.getDireccion().trim(),
            formulario.getContacto().trim(),
            arreglo.getNombre(),
            formulario.getOcasion().trim(),
            formulario.getFechaEntrega(),
            formulario.getPresupuesto(),
            EstadoPedido.EN_CURSO
        );

        Pedido pedidoGuardado =
            pedidoRepository.save(pedido);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(pedidoGuardado);
    }

    @PatchMapping(
        value = "/{id}/estado",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Cambiar el estado de un pedido",
        description = """
            Actualiza solamente el estado del pedido.

            Estados permitidos: EN_CURSO, COMPLETADO y ENTREGADO.

            Rol objetivo: administrador o empleado.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estado actualizado correctamente.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = MensajeRespuesta.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "El estado enviado no es válido.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ErrorRespuesta.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El pedido no existe.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ErrorRespuesta.class
                )
            )
        )
    })
    public ResponseEntity<?> cambiarEstado(
            @Parameter(
                description = "Identificador del pedido.",
                example = "1",
                required = true
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Nuevo estado del pedido.",
                required = true,
                content = @Content(
                    mediaType =
                        MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                        implementation =
                            EstadoPedidoSolicitud.class
                    )
                )
            )
            @RequestBody
            EstadoPedidoSolicitud solicitud
    ) {
        Optional<Pedido> pedidoEncontrado =
            pedidoRepository.findById(id);

        if (pedidoEncontrado.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    new ErrorRespuesta(
                        "El pedido no existe."
                    )
                );
        }

        if (solicitud == null
                || !StringUtils.hasText(
                    solicitud.getEstado()
                )) {
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorRespuesta(
                        "Debes seleccionar un estado."
                    )
                );
        }

        EstadoPedido estado;

        try {
            estado = EstadoPedido.valueOf(
                solicitud
                    .getEstado()
                    .trim()
                    .toUpperCase()
            );
        } catch (IllegalArgumentException excepcion) {
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorRespuesta(
                        "El estado seleccionado no es válido."
                    )
                );
        }

        Pedido pedido = pedidoEncontrado.get();
        pedido.setEstado(estado);

        pedidoRepository.save(pedido);

        return ResponseEntity.ok(
            new MensajeRespuesta(
                "Estado actualizado correctamente."
            )
        );
    }

    @DeleteMapping(
        value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Eliminar un pedido",
        description = """
            Elimina permanentemente un pedido mediante
            su identificador.

            Rol objetivo: administrador.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Pedido eliminado correctamente.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = MensajeRespuesta.class
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El pedido no existe.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ErrorRespuesta.class
                )
            )
        )
    })
    public ResponseEntity<?> eliminarPedido(
            @Parameter(
                description = "Identificador del pedido.",
                example = "1",
                required = true
            )
            @PathVariable Long id
    ) {
        Optional<Pedido> pedidoEncontrado =
            pedidoRepository.findById(id);

        if (pedidoEncontrado.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    new ErrorRespuesta(
                        "El pedido no existe."
                    )
                );
        }

        pedidoRepository.delete(
            pedidoEncontrado.get()
        );

        return ResponseEntity.ok(
            new MensajeRespuesta(
                "Pedido eliminado correctamente."
            )
        );
    }

    private String validarFormulario(
            PedidoFormulario formulario
    ) {
        if (formulario == null) {
            return "Debes enviar los datos del pedido.";
        }

        if (!StringUtils.hasText(
                formulario.getNombreCliente()
            )
                || formulario
                    .getNombreCliente()
                    .trim()
                    .split("\\s+")
                    .length < 2) {
            return "Ingresa nombre y apellido.";
        }

        if (!StringUtils.hasText(
                formulario.getDireccion()
            )) {
            return "Ingresa la dirección de entrega.";
        }

        if (!StringUtils.hasText(
                formulario.getContacto()
            )
                || !formulario
                    .getContacto()
                    .trim()
                    .matches("3[0-9]{9}")) {
            return "Ingresa un celular colombiano "
                + "de 10 dígitos.";
        }

        if (formulario.getArregloId() == null) {
            return "Selecciona un arreglo.";
        }

        if (!StringUtils.hasText(
                formulario.getOcasion()
            )) {
            return "Ingresa la ocasión.";
        }

        if (formulario.getFechaEntrega() == null) {
            return "Selecciona la fecha de entrega.";
        }

        if (formulario.getPresupuesto() == null
                || formulario.getPresupuesto() <= 0) {
            return "Selecciona un precio válido.";
        }

        return null;
    }
}