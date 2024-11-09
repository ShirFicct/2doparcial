package com.hospital.salud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persona", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Persona  {
    @Id
     private String ci;
     private String nombre;
     private String apellido;
     private String email;
     private String telefono;
     private Date fechaNacimiento;
     private String sexo;


 @OneToOne(mappedBy = "persona")
 private Doctor doctor;

 @OneToOne(mappedBy = "persona")
 private Paciente paciente;
}
