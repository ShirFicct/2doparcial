package com.hospital.salud.controller;

import com.hospital.salud.dto.EspecialidadDTO;
import com.hospital.salud.entity.Especialida;
import com.hospital.salud.response.ApiResponse;
import com.hospital.salud.service.EspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidad")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<Especialida>>> obtenerEspecialidades() {
        List<Especialida> especialidades = especialidadService.findAll();
        return new ResponseEntity<>(
                ApiResponse.<List<Especialida>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de todas las especialidades")
                        .data(especialidades)
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Especialida>> obtenerEspecialidadPorId(@PathVariable Long id) {
        Especialida especialidad = especialidadService.obtenerEspecialidad(id);
        return new ResponseEntity<>(
                ApiResponse.<Especialida>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Especialidad encontrada")
                        .data(especialidad)
                        .build(),
                HttpStatus.OK
        );
    }


    @PostMapping
    public ResponseEntity<ApiResponse<Especialida>> crearEspecialidad(@RequestBody EspecialidadDTO especialidad) {
        Especialida nuevaEspecialidad = especialidadService.save(especialidad);
        return new ResponseEntity<>(
                ApiResponse.<Especialida>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Especialidad creada correctamente")
                        .data(nuevaEspecialidad)
                        .build(),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Especialida>> actualizarEspecialidad(
            @PathVariable Long id, @RequestBody EspecialidadDTO especialidad) {
        Especialida especialidadActualizada = especialidadService.actualizar(id, especialidad);
        return new ResponseEntity<>(
                ApiResponse.<Especialida>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Especialidad actualizada correctamente")
                        .data(especialidadActualizada)
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Especialida>> eliminarEspecialidad(@PathVariable Long id) {
        Especialida especialidadDesactivada = especialidadService.eliminar(id);
        return new ResponseEntity<>(
                ApiResponse.<Especialida>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Especialidad desactivada")
                        .data(especialidadDesactivada)
                        .build(),
                HttpStatus.OK
        );
    }
}
