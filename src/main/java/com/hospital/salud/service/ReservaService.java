package com.hospital.salud.service;

import com.hospital.salud.entity.Doctor;
import com.hospital.salud.entity.Paciente;
import com.hospital.salud.entity.reserva;
import com.hospital.salud.repository.DoctorRepository;
import com.hospital.salud.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class ReservaService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private DoctorRepository doctorRepository;

  /*  public reserva crearReserva(Long pacienteId, Long doctorId, Date fecha) {
        // Verificar si el paciente está activo
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PacienteController no encontrado"));

        if (!paciente.isActivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El paciente no está habilitado para realizar reservas.");
        }

        // Verificar si el doctor está activo para recibir consultas
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor no encontrado"));

        if (!doctor.isActivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El doctor no está disponible para recibir consultas.");
        }

        // Código para crear y guardar la reserva...
        reserva reserva = new reserva();
        reserva.setPaciente(paciente);
        reserva.setDoctor(doctor);
        reserva.setFechaReserva(fecha);

        return reservaRepository.save(reserva);
    }*/
}
