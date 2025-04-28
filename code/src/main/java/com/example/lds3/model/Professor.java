package com.example.lds3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "professor")
@Data
public class Professor {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String departamento;

    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
    private Instituicao instituicao;

    @Column(nullable = false)
    private Integer saldo = 0;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;
}