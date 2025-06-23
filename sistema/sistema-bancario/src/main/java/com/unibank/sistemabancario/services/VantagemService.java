package com.unibank.sistemabancario.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.unibank.sistemabancario.models.Vantagem;
import com.unibank.sistemabancario.repositories.EmpresaRepository;
import com.unibank.sistemabancario.repositories.VantagemRepository;

@Service
public class VantagemService {

    private final VantagemRepository vantagemRepository;
    private final EmpresaRepository empresaRepository;

    public VantagemService(VantagemRepository vantagemRepository, EmpresaRepository empresaRepository) {
        this.vantagemRepository = vantagemRepository;
        this.empresaRepository = empresaRepository;
    }

    public List<Vantagem> findAll() {
        return vantagemRepository.findAll();
    }

    @Transactional
    public Vantagem buscarVantagemPorId(Long id) {
        return vantagemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vantagem não encontrada com ID: " + id));
    }

    @Transactional
    public Vantagem salvarVantagem(Vantagem vantagem) {
        validarVantagem(vantagem);
        return vantagemRepository.save(vantagem);
    }

    @Transactional
    public void deletarVantagem(Long id) {
        Vantagem vantagem = buscarVantagemPorId(id);
        vantagemRepository.delete(vantagem);
    }

    private void validarVantagem(Vantagem vantagem) {
        if (vantagem.getDescricao() == null || vantagem.getDescricao().isBlank()) {
            throw new IllegalArgumentException("Descrição da vantagem não pode estar vazia");
        }

        if (vantagem.getCustoEmMoedas() <= 0) {
            throw new IllegalArgumentException("O custo em moedas deve ser maior que zero");
        }

        if (vantagem.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade da vantagem não pode ser negativa");
        }
    }
}
