package com.hospital.salud.controller;

import com.hospital.salud.dto.UsuarioDTO;
import com.hospital.salud.entity.Doctor;
import com.hospital.salud.response.ApiResponse;
import com.hospital.salud.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctores")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Endpoint para obtener todos los doctores
    @GetMapping
    public ResponseEntity<ApiResponse<List<Doctor>>> obtenerTodosLosDoctores() {
        List<Doctor> doctores = doctorService.listDoctores();
        return new ResponseEntity<>(
                ApiResponse.<List<Doctor>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de todos los doctores")
                        .data(doctores)
                        .build(),
                HttpStatus.OK
        );
    }

    // Endpoint para obtener doctores por especialidad
    @GetMapping("/especialidad/{especialidadId}")
    public ResponseEntity<ApiResponse<List<Doctor>>> obtenerByEspecialidad(@PathVariable Long especialidadId) {
        List<Doctor> doctores = doctorService.listDoctorByEspecialidadId(especialidadId);
        return new ResponseEntity<>(
                ApiResponse.<List<Doctor>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de doctores por especialidad")
                        .data(doctores)
                        .build(),
                HttpStatus.OK
        );
    }

    // Endpoint para obtener un doctor por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Doctor>> obtenerDoctorPorId(@PathVariable Long id) {
        Doctor doctor = doctorService.obtenerDoctorById(id);
        return new ResponseEntity<>(
                ApiResponse.<Doctor>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Doctor encontrado")
                        .data(doctor)
                        .build(),
                HttpStatus.OK
        );
    }

    // Endpoint para actualizar un doctor
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Doctor>> actualizarDoctor(
            @PathVariable Long id,
            @RequestBody UsuarioDTO userDTO) {

        Doctor doctorActualizado = doctorService.actualizarDoctor(id, userDTO);
        return new ResponseEntity<>(
                ApiResponse.<Doctor>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Doctor actualizado correctamente")
                        .data(doctorActualizado)
                        .build(),
                HttpStatus.OK
        );
    }


}
