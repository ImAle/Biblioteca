package com.example.demo.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Libro;
import com.example.demo.repository.LibroRepository;
import com.example.demo.service.LibroService;

@Service("libroService")
public class LibroServiceImpl extends LibroService {
	
	@Autowired
	@Qualifier("libroRepository")
	public LibroRepository libroRepository;
	
	public List<Libro> getLibros(){
		return libroRepository.findAll();
	}
	
	public List<Libro> getLibrosByName(String titulo){
		return libroRepository.findByTituloAllIgnoreCaseContains(titulo);
	}
}
