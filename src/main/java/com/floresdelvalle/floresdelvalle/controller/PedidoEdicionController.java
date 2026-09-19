package com.floresdelvalle.floresdelvalle.controller;

import java.util.Map;
import java.util.Optional;

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
import org.springframework.http.HttpStatus;

import com.floresdelvalle.floresdelvalle.model.Arreglo;
import com.floresdelvalle.floresdelvalle.model.Pedido;
import com.floresdelvalle.floresdelvalle.model.PedidoFormulario;
import com.floresdelvalle.floresdelvalle.repository.ArregloRepository;
import com.floresdelvalle.floresdelvalle.repository.PedidoRepository;

@Controller
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

    @GetMapping("/pedidos/{id}/editar")
    public String mostrarFormulario(
            @PathVariable Long id,
            Model model
    ) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "El pedido no existe."
            ));

        PedidoFormulario formulario = new PedidoFormulario();

        formulario.setNombreCliente(pedido.getNombreCliente());
        formulario.setDireccion(pedido.getDireccion());

        // Los pedidos de ejemplo tienen espacios en el número.
        formulario.setContacto(
            pedido.getContacto().replaceAll("\\s+", "")
        );

        formulario.setOcasion(pedido.getOcasion());
        formulario.setFechaEntrega(pedido.getFechaEntrega());
        formulario.setPresupuesto(pedido.getPresupuesto());

        arregloRepository.findAllByOrderByNombreAsc()
            .stream()
            .filter(arreglo ->
                arreglo.getNombre().equalsIgnoreCase(
                    pedido.getTipoArreglo()
                )
            )
            .findFirst()
            .ifPresent(arreglo ->
                formulario.setArregloId(arreglo.getId())
            );

        model.addAttribute("pedidoId", pedido.getId());
        model.addAttribute("formulario", formulario);
        model.addAttribute(
            "arreglos",
            arregloRepository.findAllByOrderByNombreAsc()
        );

        return "pedido-editar";
    }

    @ResponseBody
    @PutMapping("/api/pedidos/{id}")
    public ResponseEntity<Map<String, String>> actualizarPedido(
            @PathVariable Long id,
            @RequestBody PedidoFormulario formulario
    ) {
        Optional<Pedido> pedidoEncontrado = pedidoRepository.findById(id);

        if (pedidoEncontrado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "El pedido no existe."));
        }

        String error = validarFormulario(formulario);

        if (error != null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", error));
        }

        Optional<Arreglo> arregloEncontrado =
            arregloRepository.findById(formulario.getArregloId());

        if (arregloEncontrado.isEmpty()) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Selecciona un arreglo disponible.")
            );
        }

        Arreglo arreglo = arregloEncontrado.get();

        if (!arreglo.incluyePrecio(formulario.getPresupuesto())) {
            return ResponseEntity.badRequest().body(
                Map.of(
                    "error",
                    "Selecciona un precio válido para el arreglo."
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
        pedido.setTipoArreglo(arreglo.getNombre());
        pedido.setOcasion(
            formulario.getOcasion().trim()
        );
        pedido.setFechaEntrega(
            formulario.getFechaEntrega()
        );
        pedido.setPresupuesto(
            formulario.getPresupuesto()
        );

        pedidoRepository.save(pedido);

        return ResponseEntity.ok(
            Map.of("mensaje", "Pedido actualizado correctamente.")
        );
    }

    private String validarFormulario(PedidoFormulario formulario) {
        if (!StringUtils.hasText(formulario.getNombreCliente())
                || formulario.getNombreCliente()
                    .trim()
                    .split("\\s+")
                    .length < 2) {
            return "Ingresa nombre y apellido.";
        }

        if (!StringUtils.hasText(formulario.getDireccion())) {
            return "Ingresa la dirección de entrega.";
        }

        if (!StringUtils.hasText(formulario.getContacto())
                || !formulario.getContacto()
                    .trim()
                    .matches("3[0-9]{9}")) {
            return "Ingresa un celular colombiano de 10 dígitos.";
        }

        if (formulario.getArregloId() == null) {
            return "Selecciona un tipo de arreglo.";
        }

        if (!StringUtils.hasText(formulario.getOcasion())) {
            return "Ingresa la ocasión.";
        }

        if (formulario.getFechaEntrega() == null) {
            return "Selecciona la fecha de entrega.";
        }

        if (formulario.getPresupuesto() == null) {
            return "Selecciona una opción de precio.";
        }

        return null;
    }
}