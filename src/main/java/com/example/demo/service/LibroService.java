package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Libro;

public interface LibroService {
	
	public abstract Libro createLibro(Libro libro);
	public abstract Optional<Libro> getLibro(Long id);
	public abstract Page<Libro> getLibros(Pageable pageable);
	public abstract Libro updateLibro(Long id, Libro newLibro);
	public abstract void deleteLibro(Long id);
}
