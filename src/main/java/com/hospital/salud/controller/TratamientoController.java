package com.hospital.salud.controller;

import com.hospital.salud.dto.TratamientoDTO;
import com.hospital.salud.entity.Tratamiento;
import com.hospital.salud.response.ApiResponse;
import com.hospital.salud.service.TratamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tratamientos")
public class TratamientoController {

    @Autowired
    private TratamientoService tratamientoService;

    // Listar todos los tratamientos
    @GetMapping
    public ResponseEntity<ApiResponse<List<Tratamiento>>> listarTratamientos() {
        List<Tratamiento> tratamientos = tratamientoService.listarTratamientos();
        return new ResponseEntity<>(
                ApiResponse.<List<Tratamiento>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de tratamientos")
                        .data(tratamientos)
                        .build(),
                HttpStatus.OK
        );
    }

    // Obtener un tratamiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Tratamiento>> obtenerTratamientoPorId(@PathVariable Long id) {
        Tratamiento tratamiento = tratamientoService.obtenerTratamientoPorId(id);
        return new ResponseEntity<>(
                ApiResponse.<Tratamiento>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Tratamiento encontrado")
                        .data(tratamiento)
                        .build(),
                HttpStatus.OK
        );
    }

    // Crear un nuevo tratamiento
    @PostMapping
    public ResponseEntity<ApiResponse<Tratamiento>> crearTratamiento(@RequestBody TratamientoDTO tratamientoDTO) {
        Tratamiento nuevoTratamiento = tratamientoService.save(tratamientoDTO);
        return new ResponseEntity<>(
                ApiResponse.<Tratamiento>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Tratamiento creado exitosamente")
                        .data(nuevoTratamiento)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // Actualizar un tratamiento
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Tratamiento>> actualizarTratamiento(@PathVariable Long id, @RequestBody TratamientoDTO tratamientoDTO) {
        Tratamiento tratamientoActualizado = tratamientoService.actualizarTratamiento(id, tratamientoDTO);
        return new ResponseEntity<>(
                ApiResponse.<Tratamiento>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Tratamiento actualizado exitosamente")
                        .data(tratamientoActualizado)
                        .build(),
                HttpStatus.OK
        );
    }

    // Eliminar un tratamiento
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarTratamiento(@PathVariable Long id) {
        tratamientoService.eliminarTratamiento(id);
        return new ResponseEntity<>(
                ApiResponse.<Void>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Tratamiento eliminado exitosamente")
                        .build(),
                HttpStatus.OK
        );
    }
}
