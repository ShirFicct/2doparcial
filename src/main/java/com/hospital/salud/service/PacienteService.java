package com.hospital.salud.service;

import com.hospital.salud.dto.PacienteDTO;
import com.hospital.salud.entity.Paciente;
import com.hospital.salud.entity.Persona;
import com.hospital.salud.repository.PacienteRepository;
import com.hospital.salud.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    /**
     * Obtener todos los pacientes.
     */
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    /**
     * Obtener un paciente por ID.
     */
    public Paciente obtenerPaciente(Long id) {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        if (!paciente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el paciente con el id " + id);
        }
        return paciente.get();
    }

    /**
     * Guardar un nuevo paciente.
     */
    public Paciente save(PacienteDTO pacienteDTO) {
        // Crear o encontrar Persona asociada al paciente
        Persona persona = new Persona();
        persona.setCi(pacienteDTO.getCi());
        persona.setNombre(pacienteDTO.getNombre());
        persona.setApellido(pacienteDTO.getApellido());
        persona.setEmail(pacienteDTO.getEmail());
        persona.setTelefono(pacienteDTO.getTelefono());
        persona.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
        persona.setSexo(pacienteDTO.getSexo());

        // Guardar persona en la base de datos (en caso de nueva persona)
        persona = personaRepository.save(persona);

        // Crear y guardar el paciente
        Paciente paciente = new Paciente();
        paciente.setNroSeguro(pacienteDTO.getNroSeguro());
        paciente.setActivo(pacienteDTO.isActivo()); // Asignar el estado activo/inactivo
        paciente.setPersona(persona);
        return pacienteRepository.save(paciente);
    }

    /**
     * Actualizar un paciente existente.
     */
    public Paciente actualizarPaciente(Long id, PacienteDTO pacienteDTO) {
        Paciente paciente = obtenerPaciente(id);

        // Actualizar datos de Persona asociada
        Persona persona = paciente.getPersona();
        persona.setNombre(pacienteDTO.getNombre());
        persona.setApellido(pacienteDTO.getApellido());
        persona.setEmail(pacienteDTO.getEmail());
        persona.setTelefono(pacienteDTO.getTelefono());
        persona.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
        persona.setSexo(pacienteDTO.getSexo());
        personaRepository.save(persona);

        // Actualizar datos de PacienteController
        paciente.setNroSeguro(pacienteDTO.getNroSeguro());
        paciente.setActivo(pacienteDTO.isActivo()); // Actualizar el estado activo/inactivo
        return pacienteRepository.save(paciente);
    }

    /**
     * Inhabilitar (desactivar) un paciente.
     */
    public Paciente inhabilitarPaciente(Long id) {
        Paciente paciente = obtenerPaciente(id);
        paciente.setActivo(false); // Cambiar el estado activo a falso
        return pacienteRepository.save(paciente);
    }

    /**
     * Habilitar un paciente.
     */
    public Paciente habilitarPaciente(Long id) {
        Paciente paciente = obtenerPaciente(id);
        paciente.setActivo(true); // Cambiar el estado activo a verdadero
        return pacienteRepository.save(paciente);
    }
}
