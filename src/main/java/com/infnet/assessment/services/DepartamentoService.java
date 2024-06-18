package com.infnet.assessment.services;


import com.infnet.assessment.models.Departamento;
import com.infnet.assessment.repositories.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Departamento> getAllDepartamentos() {
        return departamentoRepository.findAll();
    }

    public Optional<Departamento> getDepartamentoById(Long id) {
        return departamentoRepository.findById(id);
    }

    public Departamento createDepartamento(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    public Departamento updateDepartamento(Long id, Departamento departamentoDetails) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento not found"));
        departamento.setNome(departamentoDetails.getNome());
        departamento.setLocal(departamentoDetails.getLocal());
        return departamentoRepository.save(departamento);
    }

    public void deleteDepartamento(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento not found"));
        departamentoRepository.delete(departamento);
    }
}
