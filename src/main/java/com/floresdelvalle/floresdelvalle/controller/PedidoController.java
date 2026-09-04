package com.floresdelvalle.floresdelvalle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.floresdelvalle.floresdelvalle.model.EstadoPedido;
import com.floresdelvalle.floresdelvalle.repository.PedidoRepository;

@Controller
public class PedidoController {

    private final PedidoRepository pedidoRepository;

    public PedidoController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping("/pedidos")
    public String mostrarPedidos(Model model) {
        model.addAttribute(
            "pedidos",
            pedidoRepository.findAllByOrderByFechaEntregaAsc()
        );

        model.addAttribute(
            "totalPedidos",
            pedidoRepository.count()
        );

        model.addAttribute(
            "pedidosEnCurso",
            pedidoRepository.countByEstado(EstadoPedido.EN_CURSO)
        );

        model.addAttribute(
            "pedidosCompletados",
            pedidoRepository.countByEstado(EstadoPedido.COMPLETADO)
        );

        model.addAttribute(
            "pedidosEntregados",
            pedidoRepository.countByEstado(EstadoPedido.ENTREGADO)
        );

        return "pedidos";
    }
}