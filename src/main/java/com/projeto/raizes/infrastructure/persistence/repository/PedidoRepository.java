package com.projeto.raizes.infrastructure.persistence.repository;

import com.projeto.raizes.domain.model.Pedido;
import com.projeto.raizes.domain.enums.CanalPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCanalPedido(CanalPedido canalPedido);
}