package com.hospital.salud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nroSeguro;

    // Relación con Persona
    @OneToOne
    @JoinColumn(name = "persona_ci", referencedColumnName = "ci")
    private Persona persona;

    // Relación con Reserva (citas)
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<reserva> reservas;
}
