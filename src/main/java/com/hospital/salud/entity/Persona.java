package com.hospital.salud.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persona", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Persona {

	@Id
	private String ci;
	
	private String nombre;
	
	private String apellido;
	
	private String telefono;
	
	private Date fechaNacimiento;
	
	private String sexo;

	@OneToOne(mappedBy = "persona")
	private Doctor doctor;

	@OneToOne(mappedBy = "persona")
	private Paciente paciente;
	
}
