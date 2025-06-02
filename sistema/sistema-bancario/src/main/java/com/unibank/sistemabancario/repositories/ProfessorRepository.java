package com.unibank.sistemabancario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unibank.sistemabancario.models.Professor;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProfessorRepository extends JpaRepository<Professor, Long>{
    
}
