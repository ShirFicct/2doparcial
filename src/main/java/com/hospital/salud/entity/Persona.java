package com.hospital.salud.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@JsonIgnore
	private Doctor doctor;

	@OneToOne(mappedBy = "persona")
	@JsonIgnore
	private Paciente paciente;
	
}
