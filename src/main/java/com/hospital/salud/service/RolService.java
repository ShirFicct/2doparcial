package com.hospital.salud.service;

import com.hospital.salud.entity.Permiso;
import com.hospital.salud.entity.Rol;
import com.hospital.salud.repository.PermisoRepository;
import com.hospital.salud.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolService {
	
	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private PermisoRepository permisoRepository;
	
	public List<Rol> listarRoles(){
		List<Rol> roles = rolRepository.findAll();
		return roles;
	}
	
	public Rol guardarRol(String nombre, List<String> nombresPermisos) {
		Rol rol = new Rol();
		rol.setNombre(nombre);
		Set<Permiso> permisos = nombresPermisos.stream()
				.map(nombrePermiso -> permisoRepository.findByNombre(nombrePermiso)
						.orElseThrow(() -> new RuntimeException("Permiso no encontrado " + nombrePermiso)))
				.collect(Collectors.toSet());
		rol.setPermiso(permisos);
		return rolRepository.save(rol);
	}
	
	public Rol modificarRol(Long id, String nombre, List<String> nombresPermisos) {
		Rol rol = obtenerRol(id);
	    if (nombre != null && !nombre.isEmpty()) {
	        rol.setNombre(nombre);
	    }
		Set<Permiso> permisos = nombresPermisos.stream()
				.map(nombrePermiso -> permisoRepository.findByNombre(nombrePermiso)
						.orElseThrow(() -> new RuntimeException("Permiso no encontrado " + nombrePermiso)))
				.collect(Collectors.toSet());
		rol.setPermiso(permisos);
		return rolRepository.save(rol);
	}
	
	public Rol obtenerRol(Long id){
		Optional<Rol> rol = rolRepository.findById(id);
		if (rol.isPresent()) {
			return rol.get();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el rol con el id" + id);
		}
	}
	public Rol obtenerRolnnombre(String nombre){
		Optional<Rol> rol = rolRepository.findByNombre(nombre);
		if (rol.isPresent()) {
			return rol.get();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el rol con el id" + nombre);
		}
	}
	public Rol actualizarRol(Long id, Rol rolDto) {
		Rol rol = obtenerRol(id);
		rol.setNombre(rolDto.getNombre());
		return rolRepository.save(rol);
	}
	
}
