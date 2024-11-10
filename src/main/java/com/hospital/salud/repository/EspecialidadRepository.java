package com.hospital.salud.repository;

import com.hospital.salud.entity.Especialida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialida,Long> {
}
