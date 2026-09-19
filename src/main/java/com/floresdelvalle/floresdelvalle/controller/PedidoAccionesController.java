package com.floresdelvalle.floresdelvalle.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.floresdelvalle.floresdelvalle.model.EstadoPedido;
import com.floresdelvalle.floresdelvalle.model.Pedido;
import com.floresdelvalle.floresdelvalle.repository.PedidoRepository;

@RestController
public class PedidoAccionesController {

    private final PedidoRepository pedidoRepository;

    public PedidoAccionesController(
            PedidoRepository pedidoRepository
    ) {
        this.pedidoRepository = pedidoRepository;
    }

    /*
     * GET: consulta todos los pedidos.
     */
    @GetMapping("/api/pedidos")
    public List<Pedido> consultarPedidos() {
        return pedidoRepository.findAllByOrderByFechaEntregaAsc();
    }

    /*
     * GET: consulta un pedido por su ID.
     */
    @GetMapping("/api/pedidos/{id}")
    public ResponseEntity<?> consultarPedido(
            @PathVariable Long id
    ) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (pedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "error",
                    "El pedido solicitado no existe."
                ));
        }

        return ResponseEntity.ok(pedido.get());
    }

    /*
     * PATCH: modifica únicamente el estado del pedido.
     *
     * Ejemplo del cuerpo recibido:
     * {
     *     "estado": "COMPLETADO"
     * }
     */
    @PatchMapping("/api/pedidos/{id}/estado")
    public ResponseEntity<Map<String, String>> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> datos
    ) {
        Optional<Pedido> pedidoEncontrado =
            pedidoRepository.findById(id);

        if (pedidoEncontrado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "error",
                    "El pedido no existe."
                ));
        }

        String nuevoEstado = datos.get("estado");

        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "error",
                    "Debes seleccionar un estado."
                ));
        }

        EstadoPedido estado;

        try {
            estado = EstadoPedido.valueOf(
                nuevoEstado.trim().toUpperCase()
            );
        } catch (IllegalArgumentException excepcion) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "error",
                    "El estado seleccionado no es válido."
                ));
        }

        Pedido pedido = pedidoEncontrado.get();
        pedido.setEstado(estado);

        pedidoRepository.save(pedido);

        return ResponseEntity.ok(
            Map.of(
                "mensaje",
                "Estado actualizado correctamente."
            )
        );
    }

    /*
     * DELETE: elimina un pedido según su ID.
     */
    @DeleteMapping("/api/pedidos/{id}")
    public ResponseEntity<Map<String, String>> eliminarPedido(
            @PathVariable Long id
    ) {
        Optional<Pedido> pedidoEncontrado =
            pedidoRepository.findById(id);

        if (pedidoEncontrado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "error",
                    "El pedido no existe."
                ));
        }

        pedidoRepository.delete(pedidoEncontrado.get());

        return ResponseEntity.ok(
            Map.of(
                "mensaje",
                "Pedido eliminado correctamente."
            )
        );
    }
}