package com.hospital.salud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TratamientoDTO {
    private String titulo;
    private String detalle;
    private String receta;
    private Long historiaClinica_id;
}
