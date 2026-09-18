package com.floresdelvalle.floresdelvalle.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.floresdelvalle.floresdelvalle.model.Arreglo;
import com.floresdelvalle.floresdelvalle.model.EstadoPedido;
import com.floresdelvalle.floresdelvalle.model.Pedido;
import com.floresdelvalle.floresdelvalle.model.PedidoFormulario;
import com.floresdelvalle.floresdelvalle.repository.ArregloRepository;
import com.floresdelvalle.floresdelvalle.repository.PedidoRepository;

@Controller
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ArregloRepository arregloRepository;

    public PedidoController(
            PedidoRepository pedidoRepository,
            ArregloRepository arregloRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.arregloRepository = arregloRepository;
    }

    @GetMapping("/pedidos")
    public String mostrarPedidos(Model model) {
        model.addAttribute(
            "pedidos",
            pedidoRepository.findAllByOrderByFechaEntregaAsc()
        );

        model.addAttribute("totalPedidos", pedidoRepository.count());

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

    @GetMapping("/pedidos/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("formulario", new PedidoFormulario());
        model.addAttribute(
            "arreglos",
            arregloRepository.findAllByOrderByNombreAsc()
        );

        return "pedido-formulario";
    }

    @PostMapping("/pedidos")
    public String crearPedido(
            @ModelAttribute("formulario") PedidoFormulario formulario,
            BindingResult resultado,
            Model model
    ) {
        validarFormulario(formulario, resultado);

        Optional<Arreglo> arreglo = Optional.empty();

        if (formulario.getArregloId() != null) {
            arreglo = arregloRepository.findById(
                formulario.getArregloId()
            );

            if (arreglo.isEmpty()) {
                resultado.rejectValue(
                    "arregloId",
                    "arreglo.invalido",
                    "Selecciona un arreglo disponible."
                );
            }
        }

        if (arreglo.isPresent()
                && formulario.getPresupuesto() != null
                && !arreglo.get().incluyePrecio(
                    formulario.getPresupuesto()
                )) {
            resultado.rejectValue(
                "presupuesto",
                "precio.invalido",
                "Selecciona un precio válido para este arreglo."
            );
        }

        if (resultado.hasErrors()) {
            model.addAttribute(
                "arreglos",
                arregloRepository.findAllByOrderByNombreAsc()
            );

            return "pedido-formulario";
        }

        Pedido pedido = new Pedido(
            formulario.getNombreCliente().trim(),
            formulario.getDireccion().trim(),
            formulario.getContacto().trim(),
            arreglo.orElseThrow().getNombre(),
            formulario.getOcasion().trim(),
            formulario.getFechaEntrega(),
            formulario.getPresupuesto(),
            EstadoPedido.EN_CURSO
        );

        pedidoRepository.save(pedido);

        return "redirect:/pedidos";
    }

    private void validarFormulario(
            PedidoFormulario formulario,
            BindingResult resultado
    ) {
        if (!StringUtils.hasText(formulario.getNombreCliente())
                || formulario.getNombreCliente().trim().split("\\s+").length < 2) {
            resultado.rejectValue(
                "nombreCliente",
                "nombre.invalido",
                "Ingresa nombre y apellido."
            );
        }

        if (!StringUtils.hasText(formulario.getDireccion())) {
            resultado.rejectValue(
                "direccion",
                "campo.obligatorio",
                "Ingresa la dirección de entrega."
            );
        }

        if (!StringUtils.hasText(formulario.getContacto())
                || !formulario.getContacto().trim().matches("3[0-9]{9}")) {
            resultado.rejectValue(
                "contacto",
                "celular.invalido",
                "Ingresa un celular colombiano de 10 dígitos, sin espacios."
            );
        }

        if (formulario.getArregloId() == null) {
            resultado.rejectValue(
                "arregloId",
                "campo.obligatorio",
                "Selecciona un arreglo."
            );
        }

        if (!StringUtils.hasText(formulario.getOcasion())) {
            resultado.rejectValue(
                "ocasion",
                "campo.obligatorio",
                "Ingresa la ocasión."
            );
        }

        if (formulario.getFechaEntrega() == null) {
            resultado.rejectValue(
                "fechaEntrega",
                "campo.obligatorio",
                "Selecciona la fecha de entrega."
            );
        }

        if (formulario.getPresupuesto() == null) {
            resultado.rejectValue(
                "presupuesto",
                "campo.obligatorio",
                "Selecciona una opción de precio."
            );
        }
    }
}