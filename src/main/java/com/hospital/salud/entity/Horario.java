package com.hospital.salud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "horario")
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate fecha;
    private LocalTime HoraInicio;
    private LocalTime HoraFin;
    private String dia;
private boolean activo;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    private Doctor doctor;

    // Relación opcional para acceder a todas las reservas en este horario
    @OneToMany(mappedBy = "horario")
    @JsonIgnore
    private List<Reserva> reservas;

}
