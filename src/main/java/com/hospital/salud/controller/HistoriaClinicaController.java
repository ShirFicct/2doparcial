package com.hospital.salud.controller;

import com.hospital.salud.dto.HistoriaClinicaDTO;
import com.hospital.salud.entity.HistoriaClinica;
import com.hospital.salud.response.ApiResponse;
import com.hospital.salud.service.HistoriaClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historias-clinicas")
public class HistoriaClinicaController {

    @Autowired
    private HistoriaClinicaService historiaClinicaService;

    // Listar todas las historias clínicas
    @GetMapping
    public ResponseEntity<ApiResponse<List<HistoriaClinica>>> obtenerTodasLasHistorias() {
        List<HistoriaClinica> historiasClinicas = historiaClinicaService.findAll();
        return new ResponseEntity<>(
                ApiResponse.<List<HistoriaClinica>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de todas las historias clínicas")
                        .data(historiasClinicas)
                        .build(),
                HttpStatus.OK
        );
    }

    // Obtener una historia clínica por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HistoriaClinica>> obtenerHistoriaPorId(@PathVariable Long id) {
        HistoriaClinica historiaClinica = historiaClinicaService.obtenerById(id);
        return new ResponseEntity<>(
                ApiResponse.<HistoriaClinica>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Historia clínica encontrada")
                        .data(historiaClinica)
                        .build(),
                HttpStatus.OK
        );
    }

    // Crear una nueva historia clínica
    @PostMapping
    public ResponseEntity<ApiResponse<HistoriaClinica>> crearHistoriaClinica(@RequestBody HistoriaClinicaDTO historiaClinicaDTO) {
        HistoriaClinica nuevaHistoria = historiaClinicaService.save(historiaClinicaDTO);
        return new ResponseEntity<>(
                ApiResponse.<HistoriaClinica>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Historia clínica creada exitosamente")
                        .data(nuevaHistoria)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // Actualizar una historia clínica existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HistoriaClinica>> actualizarHistoriaClinica(@PathVariable Long id, @RequestBody HistoriaClinicaDTO historiaClinicaDTO) {
        HistoriaClinica historiaActualizada = historiaClinicaService.actualizar(id, historiaClinicaDTO);
        return new ResponseEntity<>(
                ApiResponse.<HistoriaClinica>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Historia clínica actualizada exitosamente")
                        .data(historiaActualizada)
                        .build(),
                HttpStatus.OK
        );
    }

    // Desactivar una historia clínica (marcar como inactiva)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<HistoriaClinica>> desactivarHistoriaClinica(@PathVariable Long id) {
        HistoriaClinica historiaClinicaDesactivada = historiaClinicaService.eliminarHistoriaClinica(id);
        return new ResponseEntity<>(
                ApiResponse.<HistoriaClinica>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Historia clínica desactivada exitosamente")
                        .data(historiaClinicaDesactivada)
                        .build(),
                HttpStatus.OK
        );
    }
}
