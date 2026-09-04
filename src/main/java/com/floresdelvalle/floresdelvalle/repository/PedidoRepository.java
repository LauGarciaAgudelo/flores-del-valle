package com.floresdelvalle.floresdelvalle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.floresdelvalle.floresdelvalle.model.EstadoPedido;
import com.floresdelvalle.floresdelvalle.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByOrderByFechaEntregaAsc();

    long countByEstado(EstadoPedido estado);
}