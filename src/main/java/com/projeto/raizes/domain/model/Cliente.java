package com.projeto.raizes.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefone;

    private Integer idade;

    @Column(nullable = false)
    private Boolean consentimentoLgpd = false;

    private LocalDateTime dataConsentimento;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }

    public Boolean getConsentimentoLgpd() { return consentimentoLgpd; }
    public void setConsentimentoLgpd(Boolean consentimentoLgpd) { this.consentimentoLgpd = consentimentoLgpd; }

    public LocalDateTime getDataConsentimento() { return dataConsentimento; }
    public void setDataConsentimento(LocalDateTime dataConsentimento) { this.dataConsentimento = dataConsentimento; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}