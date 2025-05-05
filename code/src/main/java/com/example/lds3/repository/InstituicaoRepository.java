package com.example.lds3.repository;

import com.example.lds3.model.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstituicaoRepository extends JpaRepository<Instituicao, UUID> {
}
