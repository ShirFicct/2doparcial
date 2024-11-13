package com.hospital.salud.controller;

import com.hospital.salud.dto.ReservaDTO;
import com.hospital.salud.entity.Reserva;
import com.hospital.salud.response.ApiResponse;
import com.hospital.salud.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Endpoint para listar todas las reservas
    @GetMapping
    public ResponseEntity<ApiResponse<List<Reserva>>> listarTodasLasReservas() {
        List<Reserva> reservas = reservaService.listarTodasLasReservas();
        return new ResponseEntity<>(
                ApiResponse.<List<Reserva>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de todas las reservas")
                        .data(reservas)
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<ApiResponse<List<Reserva>>> listarTodasLasReservasPaciente(@PathVariable Long id) {
        List<Reserva> reservas = reservaService.findAllByPaciente(id);
        return new ResponseEntity<>(
                ApiResponse.<List<Reserva>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de todas las reservas"+ id)
                        .data(reservas)
                        .build(),
                HttpStatus.OK
        );
    }

    // Endpoint para obtener una reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Reserva>> obtenerReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.obtenerReservaPorId(id);
        return new ResponseEntity<>(
                ApiResponse.<Reserva>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Reserva encontrada")
                        .data(reserva)
                        .build(),
                HttpStatus.OK
        );
    }
    // Endpoint para crear una nueva reserva
    @PostMapping
    public ResponseEntity<ApiResponse<Reserva>> crearReserva(@RequestBody ReservaDTO reservaDTO) {
        Reserva reservaCreada = reservaService.crearReserva(reservaDTO);
        return new ResponseEntity<>(
                ApiResponse.<Reserva>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Reserva creada exitosamente")
                        .data(reservaCreada)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // Endpoint para actualizar el estado de una reserva
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<Reserva>> actualizarEstadoReserva(
            @PathVariable Long id,
            @RequestParam String nuevoEstado,
            @RequestParam(required = false) LocalTime horaInicio,
            @RequestParam(required = false) LocalTime horaFin
    ) {
        Reserva reservaActualizada = reservaService.actualizarEstadoReserva(id, nuevoEstado, horaInicio, horaFin);
        return new ResponseEntity<>(
                ApiResponse.<Reserva>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Estado de la reserva actualizado exitosamente")
                        .data(reservaActualizada)
                        .build(),
                HttpStatus.OK
        );
    }
}
