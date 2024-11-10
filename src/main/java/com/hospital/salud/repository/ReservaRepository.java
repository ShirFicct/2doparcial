package com.hospital.salud.repository;

import com.hospital.salud.entity.reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<reserva, Long> {
}
