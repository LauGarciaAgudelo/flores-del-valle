package com.floresdelvalle.floresdelvalle.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.floresdelvalle.floresdelvalle.model.Arreglo;
import com.floresdelvalle.floresdelvalle.model.ErrorRespuesta;
import com.floresdelvalle.floresdelvalle.model.Pedido;
import com.floresdelvalle.floresdelvalle.model.PedidoFormulario;
import com.floresdelvalle.floresdelvalle.repository.ArregloRepository;
import com.floresdelvalle.floresdelvalle.repository.PedidoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(
    name = "Pedidos",
    description = "Operaciones para gestionar pedidos de la floristería."
)
public class PedidoEdicionController {

    private final PedidoRepository pedidoRepository;
    private final ArregloRepository arregloRepository;

    public PedidoEdicionController(
            PedidoRepository pedidoRepository,
            ArregloRepository arregloRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.arregloRepository = arregloRepository;
    }

    /*
     * Muestra el formulario HTML de edición.
     * Esta ruta no forma parte de la API REST.
     */
    @GetMapping("/pedidos/{id}/editar")
    @Operation(hidden = true)
    public String mostrarFormulario(
            @PathVariable Long id,
            Model model
    ) {
        Pedido pedido = pedidoRepository
            .findById(id)
            .orElseThrow(
                () -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El pedido no existe."
                )
            );

        PedidoFormulario formulario =
            new PedidoFormulario();

        formulario.setNombreCliente(
            pedido.getNombreCliente()
        );

        formulario.setDireccion(
            pedido.getDireccion()
        );

        formulario.setContacto(
            pedido
                .getContacto()
                .replaceAll("\\s+", "")
        );

        formulario.setOcasion(
            pedido.getOcasion()
        );

        formulario.setFechaEntrega(
            pedido.getFechaEntrega()
        );

        formulario.setPresupuesto(
            pedido.getPresupuesto()
        );

        arregloRepository
            .findAllByOrderByNombreAsc()
            .stream()
            .filter(
                arreglo ->
                    arreglo
                        .getNombre()
                        .equalsIgnoreCase(
                            pedido.getTipoArreglo()
                        )
            )
            .findFirst()
            .ifPresent(
                arreglo ->
                    formulario.setArregloId(
                        arreglo.getId()
                    )
            );

        model.addAttribute(
            "pedidoId",
            pedido.getId()
        );

        model.addAttribute(
            "formulario",
            formulario
        );

        model.addAttribute(
            "arreglos",
            arregloRepository
                .findAllByOrderByNombreAsc()
        );

        return "pedido-editar";
    }

    /*
     * Actualiza completamente un pedido mediante PUT.
     */
    @ResponseBody
    @PutMapping(
        value = "/api/pedidos/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        tags = {"Pedidos"},
        summary = "Actualizar completamente un pedido",
        description = """
            Actualiza los datos del cliente, contacto, dirección,
            arreglo, ocasión, fecha de entrega y presupuesto.

            Esta operación conserva el estado actual del pedido.

            Rol objetivo: administrador o empleado de la floristería.
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Pedido actualizado correctamente.",
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
    public ResponseEntity<?> actualizarPedido(
            @Parameter(
                description = "Identificador del pedido.",
                example = "1",
                required = true
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = """
                    Información completa que reemplazará los datos
                    actuales del pedido.
                    """,
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

        Pedido pedido = pedidoEncontrado.get();

        pedido.setNombreCliente(
            formulario.getNombreCliente().trim()
        );

        pedido.setDireccion(
            formulario.getDireccion().trim()
        );

        pedido.setContacto(
            formulario.getContacto().trim()
        );

        pedido.setTipoArreglo(
            arreglo.getNombre()
        );

        pedido.setOcasion(
            formulario.getOcasion().trim()
        );

        pedido.setFechaEntrega(
            formulario.getFechaEntrega()
        );

        pedido.setPresupuesto(
            formulario.getPresupuesto()
        );

        Pedido pedidoActualizado =
            pedidoRepository.save(pedido);

        return ResponseEntity.ok(
            pedidoActualizado
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