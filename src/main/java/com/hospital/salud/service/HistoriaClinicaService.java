package com.hospital.salud.service;

import com.hospital.salud.dto.HistoriaClinicaDTO;
import com.hospital.salud.entity.HistoriaClinica;
import com.hospital.salud.entity.Paciente;
import com.hospital.salud.repository.HistoriaClinicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriaClinicaService {
    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;
    @Autowired
    private PacienteService pacienteService;

    public List<HistoriaClinica> findAll() {
        return historiaClinicaRepository.findAll();
    }

    public HistoriaClinica obtenerById(Long id) {
        Optional<HistoriaClinica> historiaClinica = historiaClinicaRepository.findById(id);
        if (historiaClinica.isPresent()) {
            return historiaClinica.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Historia Clinica no encontrada");
        }

    }

    public HistoriaClinica save(HistoriaClinicaDTO historiaClinica) {
        Paciente paciente= pacienteService.obtenerPacientePorId(historiaClinica.getPaciente_id());
        HistoriaClinica historiaClinica1= new HistoriaClinica();
        historiaClinica1.setPaciente(paciente);
        historiaClinica1.setTitulo(historiaClinica.getTitulo());
        historiaClinica1.setDescripcion(historiaClinica.getDescripcion());
        historiaClinica1.setFechaCreacion(historiaClinica.getFechaCreacion());
        historiaClinica1.setTipoHistoria(historiaClinica.getTipoHistoria());
        historiaClinica1.setActivo(true);
        return historiaClinicaRepository.save(historiaClinica1);

    }

    public HistoriaClinica actualizar(Long id,HistoriaClinicaDTO historiaClinica) {
        HistoriaClinica historiaClinica1= obtenerById(id);
        Paciente paciente= pacienteService.obtenerPacientePorId(historiaClinica.getPaciente_id());
        historiaClinica1.setPaciente(paciente);
        historiaClinica1.setTitulo(historiaClinica.getTitulo());
        historiaClinica1.setDescripcion(historiaClinica.getDescripcion());
        historiaClinica1.setFechaCreacion(historiaClinica.getFechaCreacion());
        historiaClinica1.setTipoHistoria(historiaClinica.getTipoHistoria());
        return historiaClinicaRepository.save(historiaClinica1);
    }
    public HistoriaClinica eliminarHistoriaClinica(Long id) {
        HistoriaClinica historiaClinica = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Historia clínica no encontrada"));

        historiaClinica.setActivo(false);
        return historiaClinicaRepository.save(historiaClinica);
    }
}


