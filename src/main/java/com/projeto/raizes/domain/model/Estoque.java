package com.projeto.raizes.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estoques")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @Column(nullable = false)
    private Integer quantidade = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Unidade getUnidade() { return unidade; }
    public void setUnidade(Unidade unidade) { this.unidade = unidade; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}