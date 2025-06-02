package com.unibank.sistemabancario.services;

import com.unibank.sistemabancario.models.Aluno;
import com.unibank.sistemabancario.models.Extrato;
import com.unibank.sistemabancario.models.Professor;
import com.unibank.sistemabancario.models.Transacao;
import com.unibank.sistemabancario.repositories.AlunoRepository;
import com.unibank.sistemabancario.repositories.ProfessorRepository;
import com.unibank.sistemabancario.repositories.TransacaoRepository;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class TransacaoService {
    
    private final TransacaoRepository transacaoRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final ExtratoService extratoService;
    
    public TransacaoService(TransacaoRepository transacaoRepository, ProfessorRepository professorRepository, AlunoRepository alunoRepository, ExtratoService extratoService) {
        this.transacaoRepository = transacaoRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.extratoService = extratoService;
    }

@Transactional
public void realizarTransacao(Long professorId, Long alunoId, int quantidade, String mensagem) {
    Professor professor = professorRepository.findById(professorId)
        .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

    Aluno aluno = alunoRepository.findById(alunoId)
        .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

    if (quantidade <= 0) {
        throw new RuntimeException("Quantidade inválida");
    }

    if (professor.getSaldoDeMoedas() < quantidade) {
        throw new RuntimeException("Saldo insuficiente");
    }

    professor.setSaldoDeMoedas(professor.getSaldoDeMoedas() - quantidade);
    aluno.setSaldoDeMoedas(aluno.getSaldoDeMoedas() + quantidade);

    Transacao transacao = new Transacao();
    transacao.setProfessor(professor);
    transacao.setAluno(aluno);
    transacao.setQuantidade(quantidade);
    transacao.setMensagem(mensagem);
    transacao.setData(LocalDate.now());

    transacaoRepository.save(transacao);
    professorRepository.save(professor);
    alunoRepository.save(aluno);

    extratoService.criarRegistro(transacao);
}



    private Transacao criarTransacao(int quantidade, String mensagem, Extrato extrato) {
        Transacao transacao = new Transacao();
        transacao.setData(LocalDate.now());
        transacao.setQuantidade(quantidade);
        transacao.setMensagem(mensagem);
        transacao.setExtrato(extrato);
        transacaoRepository.save(transacao);
        
        extrato.getListaDeTransacoes().add(transacao);
        return transacao;
    }
}
