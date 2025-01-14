package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Reserva;

public interface ReservaService {
	
	void addReserva(Reserva reserva);
	Reserva getReservaById(Long id);
	List<Reserva> getAllReservas();
	List<Reserva> getReservasByUserId(Long userId);
	void notificar(String to, String subject, String text);
	void deleteReserva(Reserva reserva);
	Page<Reserva> getReservasFiltered(LocalDate desde, LocalDate hasta, Pageable pageable);
        
	
}
