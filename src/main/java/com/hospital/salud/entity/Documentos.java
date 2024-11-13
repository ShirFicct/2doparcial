package com.hospital.salud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documentos")
@Entity
public class Documentos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String url;
    private String nota;
    private LocalDateTime fecha;
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "tratamiento_id", nullable = false)
    @JsonIgnore
    private Tratamiento tratamiento;
}
