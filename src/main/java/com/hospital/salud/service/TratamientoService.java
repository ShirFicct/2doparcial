package com.hospital.salud.service;

import com.hospital.salud.dto.TratamientoDTO;
import com.hospital.salud.entity.HistoriaClinica;
import com.hospital.salud.entity.Tratamiento;
import com.hospital.salud.repository.TratamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TratamientoService {

    @Autowired
    TratamientoRepository tratamientoRepository;
    @Autowired
    HistoriaClinicaService historiaClinicaService;
    public List<Tratamiento> listarTratamientos() {
        return tratamientoRepository.findAll();
    }

    public Tratamiento obtenerTratamientoPorId(Long id) {
        Optional<Tratamiento> tratamiento = tratamientoRepository.findById(id);
        if (tratamiento.isPresent()) {
            return tratamiento.get();
        } else {
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Tratamiento no encontrado");
        }
    }

    public Tratamiento save(TratamientoDTO tratamientoDTO) {
        HistoriaClinica historiaClinica = historiaClinicaService.obtenerById(tratamientoDTO.getHistoriaClinica_id());

        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setTitulo(tratamientoDTO.getTitulo());
        tratamiento.setDetalle(tratamientoDTO.getDetalle());
        tratamiento.setReceta(tratamientoDTO.getReceta());
        tratamiento.setActivo(true);
        tratamiento.setHistoriaClinica(historiaClinica);

        return tratamientoRepository.save(tratamiento);
    }

    // Actualizar un tratamiento
    public Tratamiento actualizarTratamiento(Long id, TratamientoDTO tratamientoDTO) {
        Tratamiento tratamientoExistente = obtenerTratamientoPorId(id);
        HistoriaClinica historiaClinica = historiaClinicaService.obtenerById(tratamientoDTO.getHistoriaClinica_id());

        tratamientoExistente.setTitulo(tratamientoDTO.getTitulo());
        tratamientoExistente.setDetalle(tratamientoDTO.getDetalle());
        tratamientoExistente.setReceta(tratamientoDTO.getReceta());
        tratamientoExistente.setHistoriaClinica(historiaClinica);

        return tratamientoRepository.save(tratamientoExistente);
    }

    // Eliminar un tratamiento
    public Tratamiento eliminarTratamiento(Long id) {
        Tratamiento tratamiento = obtenerTratamientoPorId(id);
        tratamiento.setActivo(false);
        return tratamientoRepository.save(tratamiento);

    }
}

