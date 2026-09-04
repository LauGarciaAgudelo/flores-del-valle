package com.floresdelvalle.floresdelvalle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EntregaController {

    @GetMapping("/entregas")
    public String mostrarEntregas() {
        return "entregas";
    }
}