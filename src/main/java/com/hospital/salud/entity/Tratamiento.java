package com.hospital.salud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.Doc;
import java.util.List;
import com.hospital.salud.entity.Documents;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tratamiento")
@Entity
public class Tratamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String detalle;
    private String receta;

    @ManyToOne
    @JoinColumn(name = "historiaClinica_id")
    private HistoriaClinica historiaClinica;

    @OneToMany(mappedBy = "tratamiento")
    private List<Documents> documentos;
}
