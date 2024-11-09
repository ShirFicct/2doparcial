package com.hospital.salud.controller;

import com.hospital.salud.dto.PacienteDTO;
import com.hospital.salud.entity.Paciente;
import com.hospital.salud.response.ApiResponse;
import com.hospital.salud.service.PacienteService;
import com.hospital.salud.util.HttpStatusMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;


    @GetMapping
    //@PreAuthorize("hasAuthority('VER_PACIENTES') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<Paciente>>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.findAll();
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
   // @PreAuthorize("hasAuthority('VER_PACIENTES') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Paciente>> obtenerPaciente(@PathVariable Long id) {
        try {
            Paciente paciente = pacienteService.obtenerPaciente(id);
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


    @PostMapping
   // @PreAuthorize("hasAuthority('ADMINISTRAR_PACIENTES')")
    public ResponseEntity<ApiResponse<Paciente>> guardarPaciente(@Valid @RequestBody PacienteDTO pacienteDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(
                    ApiResponse.<Paciente>builder()
                            .errors(errors)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
        Paciente paciente = pacienteService.save(pacienteDTO);
        return new ResponseEntity<>(
                ApiResponse.<Paciente>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message(HttpStatusMessage.getMessage(HttpStatus.CREATED))
                        .data(paciente)
                        .build(),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/{id}")
   // @PreAuthorize("hasAuthority('ADMINISTRAR_PACIENTES') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Paciente>> actualizarPaciente(@PathVariable Long id, @Valid @RequestBody PacienteDTO pacienteDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(
                    ApiResponse.<Paciente>builder()
                            .errors(errors)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
        Paciente paciente = pacienteService.actualizarPaciente(id, pacienteDTO);
        return new ResponseEntity<>(
                ApiResponse.<Paciente>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                        .data(paciente)
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
   // @PreAuthorize("hasAuthority('ADMINISTRAR_PACIENTES')")
    public ResponseEntity<ApiResponse<Void>> inhabilitarPaciente(@PathVariable Long id) {
        try {
            pacienteService.inhabilitarPaciente(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.NO_CONTENT))
                            .build(),
                    HttpStatus.NO_CONTENT
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }


    @PostMapping("/{id}/habilitar")
   // @PreAuthorize("hasAuthority('ADMINISTRAR_PACIENTES')")
    public ResponseEntity<ApiResponse<Void>> habilitarPaciente(@PathVariable Long id) {
        try {
            pacienteService.habilitarPaciente(id);
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(HttpStatusMessage.getMessage(HttpStatus.OK))
                            .build(),
                    HttpStatus.OK
            );
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(
                    ApiResponse.<Void>builder()
                            .statusCode(e.getStatusCode().value())
                            .message(e.getReason())
                            .build(),
                    e.getStatusCode()
            );
        }
    }
}
