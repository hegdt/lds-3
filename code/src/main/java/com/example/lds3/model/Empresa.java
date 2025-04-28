package com.example.lds3.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "empresa")
@Data
public class Empresa {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String nomeFantasia;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;
}