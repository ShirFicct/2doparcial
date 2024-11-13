package com.hospital.salud.service;

import com.hospital.salud.dto.DocumentoDTO;
import com.hospital.salud.entity.Documentos;
import com.hospital.salud.entity.Tratamiento;
import com.hospital.salud.repository.DocumentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentosService {
    @Autowired
    private DocumentosRepository documentosRepository;
    @Autowired
    private TratamientoService tratamientoService;


    public List<Documentos> ListarDocumentos() {
        return documentosRepository.findAll();
    }

    public Documentos obtenerById(Long id){
        Optional<Documentos> documentos=documentosRepository.findById(id);
    if (documentos.isPresent()){
        return documentos.get();
    }else{
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Docuemtno no encontrado");
    }
    }

    public Documentos save (DocumentoDTO documento){
        Documentos documentos=new Documentos();
        Tratamiento tratamiento=tratamientoService.obtenerTratamientoPorId(documento.getTratamiento_id());
        documentos.setTratamiento(tratamiento);
        documentos.setUrl(documento.getUrl());
        documentos.setNota(documento.getNota());
        documentos.setFecha(documento.getFecha());
        documentos.setActivo(true);
         return documentosRepository.save(documentos);
    }

    public Documentos actualizar (Long id,DocumentoDTO documento){
        Documentos documentos=obtenerById(id);
        Tratamiento tratamiento=tratamientoService.obtenerTratamientoPorId(documento.getTratamiento_id());
        documentos.setTratamiento(tratamiento);
        documentos.setUrl(documento.getUrl());
        documentos.setNota(documento.getNota());
        documentos.setFecha(documento.getFecha());
        return documentosRepository.save(documentos);
    }
    public Documentos eliminar(Long id){
        Documentos documentos=obtenerById(id);
        documentos.setActivo(false);
        return documentosRepository.save(documentos);
    }
}
