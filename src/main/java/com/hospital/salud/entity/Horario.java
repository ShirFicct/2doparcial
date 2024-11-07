package com.hospital.salud.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "horario")
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date HoraInicio;
    private Date HoraFin;
    private String dia;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
