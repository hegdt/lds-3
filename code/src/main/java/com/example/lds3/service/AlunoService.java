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
        // Buscar o Usuario existente
        UUID usuarioId = aluno.getUsuario().getId();
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        // Buscar a Instituicao existente
        UUID instituicaoId = aluno.getInstituicao().getId();
        Instituicao instituicao = instituicaoRepository.findById(instituicaoId)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada com ID: " + instituicaoId));

        // Associar os objetos existentes ao Aluno
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");
    }
}
