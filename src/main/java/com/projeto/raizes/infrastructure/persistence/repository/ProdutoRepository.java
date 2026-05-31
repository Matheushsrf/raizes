package com.projeto.raizes.infrastructure.persistence.repository;

import com.projeto.raizes.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByAtivoTrue();
}