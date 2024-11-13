package com.hospital.salud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private Long pacienteId;
    private Long especialidadId;
    private Long horarioId; // Id del horario seleccionado para la cita
    private LocalDate fechaReserva;    // Fecha de la reserva
}
