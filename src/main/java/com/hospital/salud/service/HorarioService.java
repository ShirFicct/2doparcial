package com.hospital.salud.service;

import com.hospital.salud.dto.HorarioDTO;
import com.hospital.salud.entity.Doctor;
import com.hospital.salud.entity.Horario;
import com.hospital.salud.repository.DoctorRepository;
import com.hospital.salud.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioService {
    @Autowired
    private HorarioRepository horarioRepository;
@Autowired
private DoctorRepository doctorRepository;
@Autowired
private DoctorService doctorService;
    public List<Horario> findAll(){return horarioRepository.findAll(); }

    public Horario obtenerHorario(Long id) {
        Optional<Horario> horario = horarioRepository.findById(id);
        if (horario.isPresent()) {
            return horario.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Horario no encontrada");
        }
    }

    public Horario save(HorarioDTO horario) {
        Doctor doctor = doctorService.obtenerDoctorById(horario.getDoctor_id());
        Horario horarionuevo = new Horario();
        horarionuevo.setDoctor(doctor);
        horarionuevo.setDia(horario.getDia());
        horarionuevo.setFecha(horario.getFecha());
        horarionuevo.setHoraInicio(horario.getHoraInicio());
        horarionuevo.setHoraFin(horario.getHoraFin());
        horarionuevo.setActivo(true);
        return horarioRepository.save(horarionuevo);
    }

    public Horario actualizar(Long id, HorarioDTO horario) {
        Horario horarioActualizado = obtenerHorario(id);
        Doctor doctor = doctorService.obtenerDoctorById(horario.getDoctor_id());
        horarioActualizado.setDia(horario.getDia());
        horarioActualizado.setFecha(horario.getFecha());
        horarioActualizado.setHoraInicio(horario.getHoraInicio());
        horarioActualizado.setHoraFin(horario.getHoraFin());
       horarioActualizado.setDoctor(doctor);
       return horarioRepository.save(horarioActualizado);
    }

    public Horario eliminar(Long id) {
        Horario horario = obtenerHorario(id);
        horario.setActivo(false);
        return horarioRepository.save(horario);
    }
}
