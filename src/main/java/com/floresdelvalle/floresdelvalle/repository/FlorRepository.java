package com.floresdelvalle.floresdelvalle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.floresdelvalle.floresdelvalle.model.Flor;

public interface FlorRepository extends JpaRepository<Flor, Long> {

    List<Flor> findAllByOrderByTipoAsc();

    long countByCantidadDisponibleLessThanEqual(Integer cantidad);
}