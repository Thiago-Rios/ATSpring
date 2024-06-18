package com.infnet.assessment.controllers;

import com.infnet.assessment.models.Departamento;
import com.infnet.assessment.services.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public List<Departamento> getAllDepartamentos() {
        return departamentoService.getAllDepartamentos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamento> getDepartamentoById(@PathVariable Long id) {
        return departamentoService.getDepartamentoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Departamento createDepartamento(@RequestBody Departamento departamento) {
        return departamentoService.createDepartamento(departamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> updateDepartamento(@PathVariable Long id, @RequestBody Departamento departamentoDetails) {
        Departamento updatedDepartamento = departamentoService.updateDepartamento(id, departamentoDetails);
        return ResponseEntity.ok(updatedDepartamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable Long id) {
        departamentoService.deleteDepartamento(id);
        return ResponseEntity.noContent().build();
    }
}
