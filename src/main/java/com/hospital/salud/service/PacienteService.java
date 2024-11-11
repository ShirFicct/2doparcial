package com.hospital.salud.service;

import com.hospital.salud.entity.Paciente;
import com.hospital.salud.entity.Persona;
import com.hospital.salud.entity.Usuario;
import com.hospital.salud.repository.PacienteRepository;
import com.hospital.salud.repository.PersonaRepository;
import com.hospital.salud.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PersonaRepository personaRepository;
    
    @Autowired
	private UsuarioRepository usuarioRepository;
    
    @Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Transactional
    public void importarPacientesDesdeExcel(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); 

        Set<String> emailsEnArchivo = new HashSet<>();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            
            String ci = row.getCell(0).getStringCellValue();
            String nombre = row.getCell(1).getStringCellValue();
            String apellido = row.getCell(2).getStringCellValue();
            String telefono = row.getCell(3).getStringCellValue();
            Date fechaNacimiento = row.getCell(4).getDateCellValue();
            String sexo = row.getCell(5).getStringCellValue();
            String email = row.getCell(6).getStringCellValue();
            String password = row.getCell(7).getStringCellValue();
            String nroSeguro = row.getCell(8).getStringCellValue();

            emailsEnArchivo.add(email); 

            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);

            if (usuarioExistente.isPresent()) {
                
                Paciente pacienteExistente = usuarioExistente.get().getPersona().getPaciente();
                
                if (pacienteExistente != null) {
                    pacienteExistente.setNroSeguro(nroSeguro);
                    pacienteRepository.save(pacienteExistente);
                }
                
            } else {
                
                Persona persona = new Persona();
                persona.setCi(ci);
                persona.setNombre(nombre);
                persona.setApellido(apellido);
                persona.setTelefono(telefono);
                persona.setFechaNacimiento(fechaNacimiento);
                persona.setSexo(sexo);
                personaRepository.save(persona);

                Usuario usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setPassword(passwordEncoder.encode(password));
                usuario.setPersona(persona);
                usuarioRepository.save(usuario);

                Paciente paciente = new Paciente();
                paciente.setPersona(persona);
                paciente.setNroSeguro(nroSeguro);
                pacienteRepository.save(paciente);
            }
        }

        List<Usuario> pacientesExistentes = usuarioRepository.findAll();
        
        for (Usuario paciente : pacientesExistentes) {
            if (!emailsEnArchivo.contains(paciente.getEmail())) {
            	paciente.setActivo(true);
            	paciente.setCredencialesNoExpiradas(true);
            	paciente.setCuentaNoBloqueada(true);
            	paciente.setCuentaNoBloqueada(true);
                usuarioRepository.save(paciente);
            }
        }

        workbook.close();
    }
    

}
