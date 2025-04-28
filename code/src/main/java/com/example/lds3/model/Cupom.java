package com.example.lds3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cupom")
@Data
public class Cupom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "vantagem_id", nullable = false)
    private Vantagem vantagem;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(nullable = false)
    private LocalDateTime dataGeracao = LocalDateTime.now();
}