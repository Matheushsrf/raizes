package com.projeto.raizes.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private BigDecimal precoBase;

    @Column(nullable = false)
    private Boolean ativo = true;

    private Boolean disponivelJunino = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPrecoBase() { return precoBase; }
    public void setPrecoBase(BigDecimal precoBase) { this.precoBase = precoBase; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Boolean getDisponivelJunino() { return disponivelJunino; }
    public void setDisponivelJunino(Boolean disponivelJunino) { this.disponivelJunino = disponivelJunino; }
}