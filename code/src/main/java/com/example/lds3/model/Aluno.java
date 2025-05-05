package com.example.lds3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "aluno")
@Data
public class Aluno {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String rg;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String curso;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "instituicao_id", nullable = false)
    private Instituicao instituicao;

    @Column(nullable = false)
    private Integer saldo = 0;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;
}