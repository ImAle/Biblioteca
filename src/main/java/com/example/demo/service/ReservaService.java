package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Reserva;

public interface ReservaService {
	
	void addReserva(Reserva reserva);
	Reserva getReservaById(Long id);
	List<Reserva> getAllReservas();
	List<Reserva> getReservasByUserId(Long userId);
	void notificar();
	void deleteReserva(Reserva reserva);
	
}
