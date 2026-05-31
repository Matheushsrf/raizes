package com.projeto.raizes.infrastructure.persistence.repository;

import com.projeto.raizes.domain.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProdutoIdAndUnidadeId(Long produtoId, Long unidadeId);
}