package com.hospital.salud.repository;

import com.hospital.salud.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long>{
	
	//public List<Permiso> findAll();
	
	Optional<Permiso> findByNombre(String nombre);

}
