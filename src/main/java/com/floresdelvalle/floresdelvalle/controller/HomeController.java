package com.floresdelvalle.floresdelvalle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.floresdelvalle.floresdelvalle.model.EstadoPedido;
import com.floresdelvalle.floresdelvalle.repository.FlorRepository;
import com.floresdelvalle.floresdelvalle.repository.PedidoRepository;

@Controller
public class HomeController {

    private final FlorRepository florRepository;
    private final PedidoRepository pedidoRepository;

    public HomeController(
            FlorRepository florRepository,
            PedidoRepository pedidoRepository
    ) {
        this.florRepository = florRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping("/")
    public String mostrarInicio(Model model) {
        model.addAttribute(
            "nombreFloristeria",
            "Flores del Valle"
        );

        model.addAttribute(
            "mensajePrincipal",
            "Detalles que florecen para cada ocasión"
        );

        model.addAttribute(
            "referenciasFlores",
            florRepository.count()
        );

        model.addAttribute(
            "floresStockBajo",
            florRepository.countByCantidadDisponibleLessThanEqual(10)
        );

        model.addAttribute(
            "totalPedidos",
            pedidoRepository.count()
        );

        model.addAttribute(
            "pedidosEnCurso",
            pedidoRepository.countByEstado(EstadoPedido.EN_CURSO)
        );

        return "index";
    }
}