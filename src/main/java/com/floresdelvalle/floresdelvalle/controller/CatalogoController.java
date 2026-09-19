package com.floresdelvalle.floresdelvalle.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.floresdelvalle.floresdelvalle.model.Arreglo;
import com.floresdelvalle.floresdelvalle.model.CatalogoItem;
import com.floresdelvalle.floresdelvalle.repository.ArregloRepository;

@Controller
public class CatalogoController {

    private final ArregloRepository arregloRepository;

    public CatalogoController(
            ArregloRepository arregloRepository
    ) {
        this.arregloRepository = arregloRepository;
    }

    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model) {
        List<CatalogoItem> catalogo = arregloRepository
            .findAllByOrderByNombreAsc()
            .stream()
            .map(this::crearCatalogoItem)
            .toList();

        model.addAttribute("catalogo", catalogo);

        return "catalogo";
    }

    private CatalogoItem crearCatalogoItem(Arreglo arreglo) {
        String descripcion = obtenerDescripcion(
            arreglo.getNombre()
        );

        String imagen = obtenerImagen(
            arreglo.getNombre()
        );

        return new CatalogoItem(
            arreglo.getId(),
            arreglo.getNombre(),
            descripcion,
            imagen,
            arreglo.getPrecioBasico(),
            arreglo.getPrecioEspecial(),
            arreglo.getPrecioPremium()
        );
    }

    private String obtenerDescripcion(String nombre) {
        return switch (nombre) {
            case "Ramo de rosas" ->
                "Una combinación elegante de rosas rojas y rosadas, "
                + "acompañada de follaje natural y envoltura artesanal.";

            case "Arreglo de girasoles" ->
                "Una propuesta alegre con girasoles frescos, eucalipto "
                + "y delicadas flores blancas de acompañamiento.";

            case "Caja floral" ->
                "Rosas y claveles en tonos suaves, presentados en una "
                + "caja floral elegante para ocasiones especiales.";

            case "Ramo de lirios" ->
                "Lirios blancos y follaje fresco en una presentación "
                + "sobria, delicada y llena de elegancia.";

            case "Arreglo de orquídeas" ->
                "Orquídeas blancas y lavanda en una base de cerámica, "
                + "ideales como regalo o elemento decorativo.";

            default ->
                "Arreglo elaborado con flores frescas y seleccionado "
                + "especialmente para cada ocasión.";
        };
    }

    private String obtenerImagen(String nombre) {
        return switch (nombre) {
            case "Ramo de rosas" ->
                "/images/catalogo/ramo-rosas.png";

            case "Arreglo de girasoles" ->
                "/images/catalogo/arreglo-girasoles.png";

            case "Caja floral" ->
                "/images/catalogo/caja-floral.png";

            case "Ramo de lirios" ->
                "/images/catalogo/ramo-lirios.png";

            case "Arreglo de orquídeas" ->
                "/images/catalogo/arreglo-orquideas.png";

            default ->
                "/images/home-floristeria.png";
        };
    }
}