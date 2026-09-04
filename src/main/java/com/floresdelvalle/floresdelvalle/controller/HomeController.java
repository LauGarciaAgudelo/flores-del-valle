package com.floresdelvalle.floresdelvalle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String mostrarInicio(Model model) {
        model.addAttribute("nombreFloristeria", "Flores del Valle");
        model.addAttribute(
            "mensajePrincipal",
            "Detalles que florecen para cada ocasión"
        );

        return "index";
    }
}