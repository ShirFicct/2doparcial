package com.hospital.salud.controller;

import com.hospital.salud.dto.HorarioDTO;
import com.hospital.salud.entity.Horario;
import com.hospital.salud.response.ApiResponse;
import com.hospital.salud.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    // Obtener todos los horarios
    @GetMapping
    public ResponseEntity<ApiResponse<List<Horario>>> obtenerHorarios() {
        List<Horario> horarios = horarioService.findAll();
        return new ResponseEntity<>(
                ApiResponse.<List<Horario>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de horarios")
                        .data(horarios)
                        .build(),
                HttpStatus.OK
        );
    }

    // Obtener un horario específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Horario>> obtenerHorario(@PathVariable Long id) {
        Horario horario = horarioService.obtenerHorario(id);
        return new ResponseEntity<>(
                ApiResponse.<Horario>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Horario encontrado")
                        .data(horario)
                        .build(),
                HttpStatus.OK
        );
    }

    // Crear un nuevo horario
    @PostMapping
    public ResponseEntity<ApiResponse<Horario>> crearHorario(@RequestBody HorarioDTO horarioDTO) {
        Horario nuevoHorario = horarioService.save(horarioDTO);
        return new ResponseEntity<>(
                ApiResponse.<Horario>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Horario creado exitosamente")
                        .data(nuevoHorario)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // Actualizar un horario existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Horario>> actualizarHorario(
            @PathVariable Long id,
            @RequestBody HorarioDTO horarioDTO) {
        Horario horarioActualizado = horarioService.actualizar(id, horarioDTO);
        return new ResponseEntity<>(
                ApiResponse.<Horario>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Horario actualizado exitosamente")
                        .data(horarioActualizado)
                        .build(),
                HttpStatus.OK
        );
    }

    // Eliminar (desactivar) un horario
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Horario>> eliminarHorario(@PathVariable Long id) {
        Horario horarioEliminado = horarioService.eliminar(id);
        return new ResponseEntity<>(
                ApiResponse.<Horario>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Horario eliminado exitosamente")
                        .data(horarioEliminado)
                        .build(),
                HttpStatus.OK
        );
    }
}
