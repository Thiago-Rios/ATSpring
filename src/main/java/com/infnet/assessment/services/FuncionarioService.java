package com.infnet.assessment.services;

import com.infnet.assessment.models.Funcionario;
import com.infnet.assessment.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<Funcionario> getAllFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> getFuncionarioById(Long id) {
        return funcionarioRepository.findById(id);
    }

    public Funcionario createFuncionario(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario updateFuncionario(Long id, Funcionario funcionarioDetails) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario not found"));
        funcionario.setNome(funcionarioDetails.getNome());
        funcionario.setEndereco(funcionarioDetails.getEndereco());
        funcionario.setTelefone(funcionarioDetails.getTelefone());
        funcionario.setEmail(funcionarioDetails.getEmail());
        funcionario.setDataNascimento(funcionarioDetails.getDataNascimento());
        funcionario.setDepartamento(funcionarioDetails.getDepartamento());
        return funcionarioRepository.save(funcionario);
    }

    public void deleteFuncionario(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario not found"));
        funcionarioRepository.delete(funcionario);
    }
}
