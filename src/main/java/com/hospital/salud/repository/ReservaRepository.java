package com.hospital.salud.repository;

import com.hospital.salud.entity.Horario;
import com.hospital.salud.entity.Paciente;
import com.hospital.salud.entity.Reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // Actualizar el método para que acepte LocalDate
    List<Reserva> findByHorarioAndFechaRegistroReserva(Horario horario, LocalDate fechaRegistroReserva);

    // Actualizar el método para que acepte LocalDate
    boolean existsByPacienteAndFechaRegistroReserva(Paciente paciente, LocalDate fechaRegistroReserva);

    List<Reserva> findByPacienteId(Long pacienteId);


}
