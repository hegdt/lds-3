package com.example.lds3.service;

import com.example.lds3.model.Aluno;
import com.example.lds3.model.Instituicao;
import com.example.lds3.model.Usuario;
import com.example.lds3.repository.AlunoRepository;
import com.example.lds3.repository.InstituicaoRepository;
import com.example.lds3.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Aluno salvar(Aluno aluno) {
        Usuario usuario = usuarioRepository.findById(aluno.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Instituicao instituicao = instituicaoRepository.findById(aluno.getInstituicao().getId())
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada."));

        aluno.setUsuario(usuario);
        aluno.setInstituicao(instituicao);

        return alunoRepository.save(aluno);
    }

    public Aluno buscarPorId(UUID id) {
        return alunoRepository.findById(id).orElse(null);
    }

    public void deletar(UUID id) {
        alunoRepository.deleteById(id);
    }

    public Aluno atualizar(UUID id, Aluno aluno) {
        Aluno existente = buscarPorId(id);
        if (existente == null) throw new RuntimeException("Aluno não encontrado.");

        existente.setCpf(aluno.getCpf());
        existente.setRg(aluno.getRg());
        existente.setEndereco(aluno.getEndereco());
        existente.setCurso(aluno.getCurso());
        existente.setSaldo(aluno.getSaldo());

        return alunoRepository.save(existente);
    }
}