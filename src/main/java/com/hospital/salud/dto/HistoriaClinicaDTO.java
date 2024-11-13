package com.hospital.salud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoriaClinicaDTO {
    private LocalDateTime fechaCreacion;
    private String titulo;
    private String descripcion;
    private String tipoHistoria;
    private Long paciente_id;
}
