package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Libro;

@Repository("libroRepository")
public interface LibroRepository extends JpaRepository<Libro, Long>{
	
	Page<Libro> findByTituloAllIgnoreCaseContains(String titulo, Pageable pageable);
	
}
