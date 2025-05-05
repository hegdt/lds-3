package com.example.lds3.controller;

import com.example.lds3.model.Aluno;
import com.example.lds3.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @PostMapping
    public Aluno criarAluno(@RequestBody Aluno aluno) {
        return alunoService.salvar(aluno);
    }

    @GetMapping
    public List<Aluno> listarAlunos() {
        return alunoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Aluno buscarAluno(@PathVariable UUID id) {
        return alunoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Aluno atualizarAluno(@PathVariable UUID id, @RequestBody Aluno aluno) {
        return alunoService.atualizar(id, aluno);
    }

    @DeleteMapping("/{id}")
    public void deletarAluno(@PathVariable UUID id) {
        alunoService.deletar(id);
    }
}
