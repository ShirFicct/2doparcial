package com.hospital.salud.repository;

import com.hospital.salud.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, String> {

    // Buscar una persona por su correo electrónico
    Optional<Persona> findByEmail(String email);

    // Buscar una persona por su CI (clave primaria)
    Optional<Persona> findByCi(String ci);

    // Otras consultas personalizadas que puedas necesitar
    Optional<Persona> findByNombreAndApellido(String nombre, String apellido);
}
