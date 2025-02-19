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
	List<Reserva> getAllReservasPendientes();
	List<Reserva> getReservasByUserId(Long userId);
	List<Reserva> getReservasPendientesByUserId(Long userId);
	List<Long> getLibrosIdReservasPendientesByUserId(Long userId);
	void notificar(String email, String titulo);
	void cancelarReserva(Reserva reserva);
	void updateReserva(Reserva reserva);
	Page<Reserva> getReservasFiltered(LocalDate desde, LocalDate hasta, Pageable pageable);
	List<Reserva> getReservasFiltered(LocalDate desde, LocalDate hasta);
	List<Reserva> getReservasActivasFrom(List<Reserva> reservas);

}
