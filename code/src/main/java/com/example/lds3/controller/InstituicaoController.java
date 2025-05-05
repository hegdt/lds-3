package com.example.lds3.controller;

import com.example.lds3.model.Instituicao;
import com.example.lds3.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoController {

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @PostMapping
    public Instituicao criarInstituicao(@RequestBody Instituicao instituicao) {
        return instituicaoRepository.save(instituicao);
    }

    @GetMapping
    public List<Instituicao> listarInstituicoes() {
        return instituicaoRepository.findAll();
    }
}
