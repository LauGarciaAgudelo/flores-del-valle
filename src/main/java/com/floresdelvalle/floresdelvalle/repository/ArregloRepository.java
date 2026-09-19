package com.floresdelvalle.floresdelvalle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.floresdelvalle.floresdelvalle.model.Arreglo;

public interface ArregloRepository extends JpaRepository<Arreglo, Long> {

    List<Arreglo> findAllByOrderByNombreAsc();
}