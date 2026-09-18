package com.floresdelvalle.floresdelvalle.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.floresdelvalle.floresdelvalle.model.Arreglo;
import com.floresdelvalle.floresdelvalle.model.EstadoPedido;
import com.floresdelvalle.floresdelvalle.model.Flor;
import com.floresdelvalle.floresdelvalle.model.Pedido;
import com.floresdelvalle.floresdelvalle.repository.ArregloRepository;
import com.floresdelvalle.floresdelvalle.repository.FlorRepository;
import com.floresdelvalle.floresdelvalle.repository.PedidoRepository;

@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner cargarDatos(
            FlorRepository florRepository,
            PedidoRepository pedidoRepository,
            ArregloRepository arregloRepository
    ) {
        return args -> {
            cargarFlores(florRepository);
            cargarPedidos(pedidoRepository);
            cargarArreglos(arregloRepository);
        };
    }

    private void cargarFlores(FlorRepository florRepository) {
        if (florRepository.count() != 0) {
            return;
        }

        florRepository.saveAll(List.of(
            new Flor("Rosa", "Rojo", "Freedom", 45, 2500, 5000),
            new Flor("Rosa", "Blanco", "Mondial", 28, 2800, 5500),
            new Flor("Girasol", "Amarillo", "Sunrich", 18, 3500, 6500),
            new Flor("Lirio", "Blanco", "Oriental", 8, 4200, 8000),
            new Flor("Clavel", "Rosado", "Estándar", 32, 1800, 3800),
            new Flor("Orquídea", "Morado", "Phalaenopsis", 6, 12000, 22000)
        ));
    }

    private void cargarPedidos(PedidoRepository pedidoRepository) {
        if (pedidoRepository.count() != 0) {
            return;
        }

        pedidoRepository.saveAll(List.of(
            new Pedido(
                "Laura Martínez",
                "Carrera 32 # 18-25",
                "300 456 7890",
                "Ramo de rosas",
                "Cumpleaños",
                LocalDate.of(2026, 9, 8),
                95000,
                EstadoPedido.EN_CURSO
            ),
            new Pedido(
                "Andrés Gómez",
                "Calle 14 # 45-30",
                "310 234 5678",
                "Arreglo de girasoles",
                "Aniversario",
                LocalDate.of(2026, 9, 10),
                125000,
                EstadoPedido.COMPLETADO
            ),
            new Pedido(
                "Carolina Ruiz",
                "Transversal 28 # 10-62",
                "315 678 9012",
                "Caja floral",
                "Grado",
                LocalDate.of(2026, 9, 12),
                150000,
                EstadoPedido.EN_CURSO
            ),
            new Pedido(
                "Felipe Torres",
                "Calle 50 # 20-15",
                "301 890 1234",
                "Ramo de lirios",
                "Agradecimiento",
                LocalDate.of(2026, 9, 15),
                110000,
                EstadoPedido.ENTREGADO
            ),
            new Pedido(
                "Mariana López",
                "Carrera 40 # 35-21",
                "320 345 6789",
                "Arreglo de orquídeas",
                "Celebración",
                LocalDate.of(2026, 9, 18),
                220000,
                EstadoPedido.COMPLETADO
            )
        ));
    }

    private void cargarArreglos(ArregloRepository arregloRepository) {
        if (arregloRepository.count() != 0) {
            return;
        }

        arregloRepository.saveAll(List.of(
            new Arreglo("Ramo de rosas", 75000, 95000, 125000),
            new Arreglo("Arreglo de girasoles", 95000, 125000, 160000),
            new Arreglo("Caja floral", 120000, 150000, 185000),
            new Arreglo("Ramo de lirios", 90000, 110000, 145000),
            new Arreglo("Arreglo de orquídeas", 180000, 220000, 270000)
        ));
    }
}