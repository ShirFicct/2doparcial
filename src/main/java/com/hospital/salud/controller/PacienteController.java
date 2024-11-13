package com.hospital.salud.controller;

import com.hospital.salud.entity.Paciente;
import com.hospital.salud.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<Paciente>> obtenerTodosLosPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodosLosPacientes();
        return ResponseEntity.ok(pacientes);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPacientePorId(@PathVariable Long id) {
        Paciente paciente = pacienteService.obtenerPacientePorId(id);
        return ResponseEntity.ok(paciente);
    }
}
