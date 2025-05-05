package com.example.lds3.repository;

import com.example.lds3.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {}
