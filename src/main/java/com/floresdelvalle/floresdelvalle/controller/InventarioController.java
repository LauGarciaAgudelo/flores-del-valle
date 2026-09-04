package com.floresdelvalle.floresdelvalle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventarioController {

    @GetMapping("/inventario")
    public String mostrarInventario() {
        return "inventario";
    }
}