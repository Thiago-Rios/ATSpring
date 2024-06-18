package com.infnet.assessment.controllers;

import com.infnet.assessment.models.Funcionario;
import com.infnet.assessment.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public List<Funcionario> getAllFuncionarios() {
        return funcionarioService.getAllFuncionarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> getFuncionarioById(@PathVariable Long id) {
        return funcionarioService.getFuncionarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Funcionario createFuncionario(@RequestBody Funcionario funcionario) {
        return funcionarioService.createFuncionario(funcionario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> updateFuncionario(@PathVariable Long id, @RequestBody Funcionario funcionarioDetails) {
        Funcionario updatedFuncionario = funcionarioService.updateFuncionario(id, funcionarioDetails);
        return ResponseEntity.ok(updatedFuncionario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuncionario(@PathVariable Long id) {
        funcionarioService.deleteFuncionario(id);
        return ResponseEntity.noContent().build();
    }
}
