package com.example.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Libro;

public interface LibroService {
	
	public abstract Libro createLibro(Libro libro);
	public abstract Optional<Libro> getLibro(Long id);
	public abstract Page<Libro> getLibros(Pageable pageable);
	Page<Libro> getLibrosOrdenados(Pageable pageable, String campo, String direccion);
	public abstract Libro updateLibro(Libro Libro);
	public abstract void deleteLibro(Long id);
	public abstract Page<Libro> getLibrosByName(String titulo, Pageable pageable, String campo, String direccion);
}
