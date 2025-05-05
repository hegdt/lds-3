package com.example.lds3.service;

import com.example.lds3.model.Empresa;
import com.example.lds3.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public Empresa salvar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public Empresa buscarPorId(UUID id) {
        return empresaRepository.findById(id).orElse(null);
    }

    public void deletar(UUID id) {
        empresaRepository.deleteById(id);
    }
}
