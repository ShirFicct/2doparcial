package com.hospital.salud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reserva")
public class reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estado;
    private Date fechaRegistroReserva;
    private Date horarioInicioAtencion;
    private Date horarioFinAtencion;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
}
