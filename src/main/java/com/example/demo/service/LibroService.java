package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Libro;
import com.example.demo.entity.Reserva;

public interface LibroService {
	
	Libro createLibro(Libro libro);
	Optional<Libro> getLibro(Long id);
	Page<Libro> getAllLibros(Pageable pageable);
	Page<Libro> getLibrosFiltered(String titulo, String genero, String autor, Pageable pageable);
	List<Libro> getLibrosMasPrestados();
	Libro updateLibro(Libro Libro);
	void deleteLibro(Long id);
	List<Reserva> getReservasPedientes(Libro libro);
	Map<String, Integer> getNumeroLibrosPorCategoria();
	
}
