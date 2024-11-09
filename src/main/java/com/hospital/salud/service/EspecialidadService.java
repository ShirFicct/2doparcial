package com.hospital.salud.service;

import com.hospital.salud.entity.Especialida;
import com.hospital.salud.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadService {
    @Autowired
    private EspecialidadRepository especialidadRepository;

    public List<Especialida> findAll() {
        return especialidadRepository.findAll();
    }

    public Especialida obtenerEspecialidad(Long id) {
        Optional<Especialida> especialidad = especialidadRepository.findById(id);
        if (especialidad.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Especialidad no encontrada");
        }
        return especialidad.get();
    }

    public Especialida save (Especialida especialidad) {
        especialidad.setActivo(true);
        return especialidadRepository.save(especialidad);
    }

    public Especialida actualizar (Long id, Especialida especialidad) {
        Especialida especialidadActual = obtenerEspecialidad(id);
        especialidadActual.setNombre(especialidad.getNombre());
        especialidadActual.setDescripcion(especialidad.getDescripcion());
        return especialidadRepository.save(especialidadActual);
    }

    public Especialida eliminar(Long id) {
        Especialida especialidad = obtenerEspecialidad(id);
        especialidad.setActivo(false);
        return especialidadRepository.save(especialidad);
    }
}
