package com.unibank.sistemabancario.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.unibank.sistemabancario.models.Aluno;
import com.unibank.sistemabancario.models.Cupom;
import com.unibank.sistemabancario.models.Vantagem;
import com.unibank.sistemabancario.models.dtos.CreateAlunoDTO;
import com.unibank.sistemabancario.models.dtos.ResgateDeVantagemDTO;
import com.unibank.sistemabancario.repositories.AlunoRepository;
import com.unibank.sistemabancario.repositories.CupomRepository;
import com.unibank.sistemabancario.repositories.ExtratoRepository;
import com.unibank.sistemabancario.repositories.VantagemRepository;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final VantagemRepository vantagemRepository;
    private final CupomRepository cupomRepository;
    private final PessoaService pessoaService;
    private final ExtratoRepository extratoRepository;
    private final EmailService emailService;

    public AlunoService(AlunoRepository alunoRepository, VantagemRepository vantagemRepository, CupomRepository cupomRepository, ExtratoRepository extratoRepository, PessoaService pessoaService, EmailService emailService){
        this.alunoRepository = alunoRepository;
        this.vantagemRepository = vantagemRepository;
        this.cupomRepository = cupomRepository;
        this.extratoRepository = extratoRepository;
        this.pessoaService = pessoaService;
        this.emailService = emailService;
    }

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> findById(Long id) {
        return alunoRepository.findById(id);
    }

    public Aluno save(CreateAlunoDTO createAlunoDTO) {

            Aluno aluno = Aluno.builder()
                .nome(createAlunoDTO.getNome())
                .email(createAlunoDTO.getEmail())
                .password(createAlunoDTO.getPassword())
                .cpf(createAlunoDTO.getCpf())
                .rg(createAlunoDTO.getRg())
                .endereco(createAlunoDTO.getEndereco())
                .curso(createAlunoDTO.getCurso())
                .saldoDeMoedas(createAlunoDTO.getSaldoDeMoedas())
                .instituicao(createAlunoDTO.getInstituicao())
                .tipoUser(createAlunoDTO.getTipoUser())
                .build();
    
            aluno = alunoRepository.save(aluno);
    
            return alunoRepository.save(aluno);
    }

    public Aluno updateAluno(Long id, Aluno aluno) {
        Aluno optionalAluno = this.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        aluno.setId(optionalAluno.getId());
        return this.update(aluno);
    }
    
    public Aluno update(Aluno aluno){
        return alunoRepository.save(aluno);
    }

    public void deleteById(Long id) {
        alunoRepository.deleteById(id);
    }
    
    public ResgateDeVantagemDTO resgatarVantagem(Long alunoId, Long vantagemId) {
        Aluno aluno = alunoRepository.findById(alunoId).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Vantagem vantagem = vantagemRepository.findById(vantagemId).orElseThrow(() -> new RuntimeException("Vantagem não encontrada"));

        if (aluno.getSaldoDeMoedas() < vantagem.getCustoEmMoedas()) {
            throw new RuntimeException("Saldo insuficiente");
        }

        if (vantagem.getQuantidade() <= 0) {
            throw new RuntimeException("Vantagem esgotada");
        }

        aluno.setSaldoDeMoedas(aluno.getSaldoDeMoedas() - vantagem.getCustoEmMoedas());

        String mensagem = "Resgate de vantagem: " + vantagem.getDescricao();
        pessoaService.registrarTransacao(aluno, -vantagem.getCustoEmMoedas(), mensagem);


        vantagem.setQuantidade(vantagem.getQuantidade() - 1);
        vantagemRepository.save(vantagem);

        Cupom cupom = new Cupom();
        cupom.setCodigo(UUID.randomUUID().toString());
        cupom.setVantagem(vantagem);
        cupom.setAluno(aluno);

        aluno.getCupons().add(cupom);

        ResgateDeVantagemDTO resgateDeVantagemDTO = new ResgateDeVantagemDTO();
        resgateDeVantagemDTO.setCupom(cupom.getCodigo());
        resgateDeVantagemDTO.setEmpresaId(cupom.getVantagem().getEmpresa().getId());

        cupomRepository.save(cupom);
        alunoRepository.save(aluno);

        String emailAluno = aluno.getEmail();
        String mensagemAluno = "Você resgatou a vantagem: " + vantagem.getDescricao() +
            "\nCódigo do cupom: " + cupom.getCodigo() +
            "\nApresente este código para utilizar sua vantagem.";

        emailService.enviarEmail(emailAluno, "Resgate de Vantagem", mensagemAluno);

        String emailEmpresa = vantagem.getEmpresa().getEmail();
        String mensagemEmpresa = "Um aluno resgatou a vantagem: " + vantagem.getDescricao() +
            "\nCódigo do cupom: " + cupom.getCodigo() +
            "\nVerifique este código na troca presencial.";

        emailService.enviarEmail(emailEmpresa, "Resgate de Vantagem por Aluno", mensagemEmpresa);

        return resgateDeVantagemDTO;
    }

    @Transactional
    public void receberMoedas(Long alunoId, int quantidade, String mensagem) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        aluno.setSaldoDeMoedas(aluno.getSaldoDeMoedas() + quantidade);
        pessoaService.registrarTransacao(aluno, quantidade, mensagem);
        alunoRepository.save(aluno);
    }

    
}
