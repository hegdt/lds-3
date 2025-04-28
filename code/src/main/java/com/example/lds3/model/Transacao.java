package com.example.lds3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacao")
@Data
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime data = LocalDateTime.now();

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "remetente_id", nullable = false)
    private Usuario remetente;

    @ManyToOne
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;

    @Column(nullable = false)
    private String mensagem;
}