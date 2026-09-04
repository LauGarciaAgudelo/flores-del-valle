package com.floresdelvalle.floresdelvalle.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.floresdelvalle.floresdelvalle.model.Flor;
import com.floresdelvalle.floresdelvalle.repository.FlorRepository;

@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner cargarInventario(
            FlorRepository florRepository
    ) {
        return args -> {
            if (florRepository.count() == 0) {
                List<Flor> flores = List.of(
                    new Flor(
                        "Rosa",
                        "Rojo",
                        "Freedom",
                        45,
                        2500,
                        5000
                    ),
                    new Flor(
                        "Rosa",
                        "Blanco",
                        "Mondial",
                        28,
                        2800,
                        5500
                    ),
                    new Flor(
                        "Girasol",
                        "Amarillo",
                        "Sunrich",
                        18,
                        3500,
                        6500
                    ),
                    new Flor(
                        "Lirio",
                        "Blanco",
                        "Oriental",
                        8,
                        4200,
                        8000
                    ),
                    new Flor(
                        "Clavel",
                        "Rosado",
                        "Estándar",
                        32,
                        1800,
                        3800
                    ),
                    new Flor(
                        "Orquídea",
                        "Morado",
                        "Phalaenopsis",
                        6,
                        12000,
                        22000
                    )
                );

                florRepository.saveAll(flores);
            }
        };
    }
}