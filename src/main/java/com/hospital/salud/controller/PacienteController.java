package com.hospital.salud.controller;

import com.hospital.salud.entity.Paciente;
import com.hospital.salud.response.ApiResponse;
import com.hospital.salud.service.PacienteService;
import com.hospital.salud.util.HttpStatusMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

	@Autowired
    private PacienteService pacienteService;

    @PostMapping("/importar")
    public ResponseEntity<ApiResponse<Void>> importarPacientesDesdeExcel(@RequestParam("file") MultipartFile file) {
        try {
            pacienteService.importarPacientesDesdeExcel(file);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Pacientes importados con éxito")
                            .build(),
                    HttpStatus.OK
            );
        } catch (IOException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error al procesar el archivo")
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Paciente>>> listarTodosLosPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodosLosPacientes();
        return new ResponseEntity<>(
                ApiResponse.<List<Paciente>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                        .data(pacientes)
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Paciente>> obtenerPacientePorId(@PathVariable Long id) {
        try {
            Paciente paciente = pacienteService.obtenerPacientePorId(id);
            return new ResponseEntity<>(
                    ApiResponse.<Paciente>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                            .data(paciente)
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Paciente>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }
}
