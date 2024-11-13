package com.hospital.salud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDTO {
    private LocalDate fecha;
    private LocalTime HoraInicio;
    private LocalTime HoraFin;
    private String dia;
    private Long doctor_id;

}
