package com.floresdelvalle.floresdelvalle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.floresdelvalle.floresdelvalle.repository.FlorRepository;

@Controller
public class InventarioController {

    private final FlorRepository florRepository;

    public InventarioController(FlorRepository florRepository) {
        this.florRepository = florRepository;
    }

    @GetMapping("/inventario")
    public String mostrarInventario(Model model) {
        model.addAttribute(
            "flores",
            florRepository.findAllByOrderByTipoAsc()
        );

        return "inventario";
    }
}