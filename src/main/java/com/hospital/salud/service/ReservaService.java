package com.hospital.salud.service;

import com.hospital.salud.dto.ReservaDTO;
import com.hospital.salud.entity.Reserva;
import com.hospital.salud.entity.Paciente;
import com.hospital.salud.entity.Horario;
import com.hospital.salud.entity.Doctor;

import com.hospital.salud.repository.ReservaRepository;
import com.hospital.salud.repository.PacienteRepository;
import com.hospital.salud.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class ReservaService {

    private static final int MAX_RESERVAS_POR_DIA = 30;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private PacienteService pacienteService;

    public Reserva crearReserva(ReservaDTO reservaDTO) {
        Paciente paciente = pacienteRepository.findById(reservaDTO.getPacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));

        Horario horario = horarioRepository.findById(reservaDTO.getHorarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Horario no encontrado"));
        Doctor doctor = horario.getDoctor();

        // Verificar si la fecha de la reserva está dentro de la semana actual
        if (!esFechaDentroDeLaSemanaActual(reservaDTO.getFechaReserva())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La reserva solo se puede hacer dentro de la semana actual");
        }

        // Validar si ya hay 30 reservas para este horario y día
        List<Reserva> reservasDelDia = reservaRepository.findByHorarioAndFechaRegistroReserva(horario, reservaDTO.getFechaReserva());
        if (reservasDelDia.size() >= MAX_RESERVAS_POR_DIA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El doctor ya alcanzó el límite de reservas para el día");
        }

        // Validar si el paciente ya tiene una reserva para el mismo día
        boolean tieneReservaHoy = reservaRepository.existsByPacienteAndFechaRegistroReserva(paciente, reservaDTO.getFechaReserva());
        if (tieneReservaHoy) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El paciente ya tiene una reserva para el día de hoy");
        }

        // Crear y guardar la nueva reserva
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setPaciente(paciente);
        nuevaReserva.setHorario(horario);
        nuevaReserva.setFechaRegistroReserva(reservaDTO.getFechaReserva());
        nuevaReserva.setNroFicha(reservasDelDia.size() + 1); // Número de ficha secuencial
        nuevaReserva.setEstado("Pendiente");
        return reservaRepository.save(nuevaReserva);
    }

    // Método para actualizar el estado de la reserva, solo el doctor puede hacerlo
    public Reserva actualizarEstadoReserva(Long reservaId, String nuevoEstado, LocalTime horaInicio, LocalTime horaFin) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

        reserva.setEstado(nuevoEstado);
        reserva.setHorarioInicioAtencion(horaInicio);
        reserva.setHorarioFinAtencion(horaFin);
        return reservaRepository.save(reserva);
    }

    // Verifica si la fecha de la reserva está dentro de la semana actual
    private boolean esFechaDentroDeLaSemanaActual(LocalDate fechaReserva) {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.with(DayOfWeek.MONDAY);
        LocalDate finSemana = hoy.with(DayOfWeek.SUNDAY);

        System.out.println("Fecha de la reserva: " + fechaReserva);
        System.out.println("Inicio de la semana: " + inicioSemana);
        System.out.println("Fin de la semana: " + finSemana);

        return !fechaReserva.isBefore(inicioSemana) && !fechaReserva.isAfter(finSemana);
    }




    // Listar todas las reservas
    public List<Reserva> listarTodasLasReservas() {
        return reservaRepository.findAll();
    }

    // Obtener una reserva por su ID
    public Reserva obtenerReservaPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));
    }

    public List<Reserva>findAllByPaciente(Long pacienteId) {
        Paciente paciente= pacienteService.obtenerPacientePorId(pacienteId);

        if (paciente==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado");
        }
        return reservaRepository.findByPacienteId(pacienteId);
    }

}
