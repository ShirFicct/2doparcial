package com.hospital.salud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoDTO {
    private String url;
    private String nota;
    private LocalDateTime fecha;
private Long tratamiento_id;
}
