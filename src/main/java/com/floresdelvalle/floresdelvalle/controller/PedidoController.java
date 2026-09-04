package com.floresdelvalle.floresdelvalle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PedidoController {

    @GetMapping("/pedidos")
    public String mostrarPedidos() {
        return "pedidos";
    }
}