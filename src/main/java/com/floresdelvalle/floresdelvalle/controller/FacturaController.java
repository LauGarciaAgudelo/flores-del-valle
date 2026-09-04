package com.floresdelvalle.floresdelvalle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FacturaController {

    @GetMapping("/facturacion")
    public String mostrarFacturacion() {
        return "facturacion";
    }
}