package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Reserva;

@Repository("reservaRepository")
public interface ReservaRepository extends JpaRepository<Reserva, Long>{
	
	Page<Reserva> findAllByFechaReservaBetween(LocalDate desde, LocalDate hasta, Pageable pageable);
	List<Reserva> findAllByFechaReservaBetween(LocalDate desde, LocalDate hasta);
}
