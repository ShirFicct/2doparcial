package com.hospital.salud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
    private int NroFicha;
    private String estado;
    private LocalDate fechaRegistroReserva;
    private LocalTime horarioInicioAtencion;
    private LocalTime horarioFinAtencion;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    @JsonIgnore
    private Paciente paciente;

    // Relación con Horario
    @ManyToOne
    @JoinColumn(name = "horario_id")
    private Horario horario;
}
