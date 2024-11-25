package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Libro;
import com.example.demo.repository.LibroRepository;

@Service("libroService")
public interface LibroService {
	
	@Autowired
	@Qualifier("libroRepository")
	//private LibroRepository libroRepository;
	
	List<Libro> findAllLibros();
	
}
