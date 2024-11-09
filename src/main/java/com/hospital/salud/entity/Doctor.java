package com.hospital.salud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean activo;

    // Relación con Especialidad
    @ManyToOne
    @JoinColumn(name = "especialidad_id", referencedColumnName = "id")
    @JsonIgnore
    private Especialida especialidad;
    // Relación con Persona
    @OneToOne
    @JoinColumn(name = "persona_ci", referencedColumnName = "ci")
    private Persona persona;
}
