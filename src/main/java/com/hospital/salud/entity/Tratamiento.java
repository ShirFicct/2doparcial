package com.hospital.salud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tratamiento")
@Entity
public class Tratamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titulo;
    private String detalle;
    private String receta;
private boolean activo;
    @ManyToOne
    @JoinColumn(name = "historiaClinica_id", nullable = false)
    @JsonIgnore
    private HistoriaClinica historiaClinica;

    @OneToMany(mappedBy = "tratamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Documentos> documentos;
}
