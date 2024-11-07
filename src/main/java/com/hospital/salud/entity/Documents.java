package com.hospital.salud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documentos")
@Entity
public class Documents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String nota;

    @ManyToOne
    @JoinColumn(name = "tratamiento_id")
    private Tratamiento tratamiento;
}
