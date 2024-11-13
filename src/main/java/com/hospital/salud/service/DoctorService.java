package com.hospital.salud.service;

import com.hospital.salud.dto.UsuarioDTO;
import com.hospital.salud.entity.Doctor;
import com.hospital.salud.entity.Persona;
import com.hospital.salud.repository.DoctorRepository;
import com.hospital.salud.repository.PersonaRepository;
import com.hospital.salud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    EspecialidadService especialidadService;
    @Autowired
    private PersonaRepository personaRepository;

    public List<Doctor> listDoctores(){return doctorRepository.findAll();}

    public Doctor obtenerDoctorById(Long id){
        Optional<Doctor> doctor= doctorRepository.findById(id);
        if (doctor.isPresent()){
            return doctor.get();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor  no encontrad");
        }
    }

    public List<Doctor> listDoctorByEspecialidadId(Long especialidadId) {
        return doctorRepository.findByEspecialidadId(especialidadId);
    }

    public Doctor actualizarDoctor(Long doctorId, UsuarioDTO userDTO) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        // Actualizar información de la entidad Persona asociada
        Persona persona = doctor.getPersona();
        persona.setNombre(userDTO.getNombre());
        persona.setApellido(userDTO.getApellido());
        persona.setFechaNacimiento(userDTO.getFechaNacimiento());
        persona.setTelefono(userDTO.getTelefono());
        persona.setSexo(userDTO.getSexo());
        personaRepository.save(persona);

        // Actualizar especialidad del doctor
        doctor.setEspecialidad(especialidadService.obtenerEspecialidad(userDTO.getEspecialidad_id()));

        return doctorRepository.save(doctor);
    }

    /*public Doctor desactivarDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        doctor.setActivo(false);
        return doctorRepository.save(doctor);
    }

    // Método para activar un doctor
    public Doctor activarDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        doctor.setActivo(true);
        return doctorRepository.save(doctor);
    }*/
}
