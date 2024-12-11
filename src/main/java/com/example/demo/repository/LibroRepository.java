package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Libro;

@Repository("libroRepository")
public interface LibroRepository extends JpaRepository<Libro, Long>{
	
	List<Libro> findByTituloAllIgnoreCaseContains(String titulo);
	
}
