package com.hospital.salud.service;

import com.hospital.salud.dto.DoctorDTO;
import com.hospital.salud.entity.Doctor;
import com.hospital.salud.entity.Especialida;
import com.hospital.salud.entity.Persona;
import com.hospital.salud.repository.DoctorRepository;
import com.hospital.salud.repository.EspecialidadRepository;
import com.hospital.salud.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private PersonaRepository personaRepository;

    /**
     * Obtener todos los doctores.
     */
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    /**
     * Obtener un doctor por ID.
     */
    public Doctor obtenerDoctor(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (!doctor.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el doctor con el id " + id);
        }
        return doctor.get();
    }

    /**
     * Guardar un nuevo doctor.
     */
    public Doctor save(DoctorDTO doctorDTO) {
        // Crear o encontrar la Persona asociada al doctor
        Persona persona = new Persona();
        persona.setCi(doctorDTO.getCi());
        persona.setNombre(doctorDTO.getNombre());
        persona.setApellido(doctorDTO.getApellido());
        persona.setEmail(doctorDTO.getEmail());
        persona.setTelefono(doctorDTO.getTelefono());
        persona.setSexo(doctorDTO.getSexo());

        // Guardar Persona en la base de datos
        persona = personaRepository.save(persona);

        // Buscar la Especialidad por ID
        Especialida especialidad = especialidadRepository.findById(doctorDTO.getEspecialidad_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Especialidad no encontrada"));

        // Crear y guardar el Doctor
        Doctor doctor = new Doctor();
        doctor.setActivo(doctorDTO.isActivo()); // Asignar el estado activo/inactivo
        doctor.setEspecialidad(especialidad);
        doctor.setPersona(persona);
        return doctorRepository.save(doctor);
    }

    /**
     * Actualizar un doctor existente.
     */
    public Doctor actualizarDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor doctor = obtenerDoctor(id);

        // Actualizar datos de Persona asociada
        Persona persona = doctor.getPersona();
        persona.setNombre(doctorDTO.getNombre());
        persona.setApellido(doctorDTO.getApellido());
        persona.setEmail(doctorDTO.getEmail());
        persona.setTelefono(doctorDTO.getTelefono());
        persona.setSexo(doctorDTO.getSexo());
        personaRepository.save(persona);

        // Buscar la Especialidad por ID
        Especialida especialidad = especialidadRepository.findById(doctorDTO.getEspecialidad_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Especialidad no encontrada"));

        // Actualizar datos de Doctor
        doctor.setActivo(doctorDTO.isActivo()); // Actualizar el estado activo/inactivo
        doctor.setEspecialidad(especialidad);
        return doctorRepository.save(doctor);
    }

    /**
     * Inhabilitar (desactivar) un doctor.
     */
    public Doctor inhabilitarDoctor(Long id) {
        Doctor doctor = obtenerDoctor(id);
        doctor.setActivo(false); // Cambiar el estado activo a falso
        return doctorRepository.save(doctor);
    }

    /**
     * Habilitar un doctor.
     */
    public Doctor habilitarDoctor(Long id) {
        Doctor doctor = obtenerDoctor(id);
        doctor.setActivo(true); // Cambiar el estado activo a verdadero
        return doctorRepository.save(doctor);
    }
}
