package com.example.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Libro;
import com.example.demo.repository.LibroRepository;
import com.example.demo.service.LibroService;

@Service("libroService")
public class LibroServiceImpl implements LibroService {
	
	@Autowired
	@Qualifier("libroRepository")
	public LibroRepository libroRepository;
	
	@Override
	public Optional<Libro> getLibro(Long id) {
		return libroRepository.findById(id);
	}
	
	@Override
	public Page<Libro> getLibros(Pageable pageable){
		return libroRepository.findAll(pageable);
	}

	@Override
	public Page<Libro> getLibrosOrdenados(Pageable pageable, String campo, String direccion){
		Sort sort = direccion.equalsIgnoreCase("asc") ? Sort.by(campo).ascending() : Sort.by(campo).descending();
		Pageable pagSorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return libroRepository.findAll(pagSorted);
	}

	@Override
	public Page<Libro> getLibrosByName(String titulo, Pageable pageable, String campo, String direccion) {
		Sort sort = direccion.equalsIgnoreCase("asc") ? Sort.by(campo).ascending() : Sort.by(campo).descending();
		Pageable pagSorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
	    return libroRepository.findByTituloAllIgnoreCaseContains(titulo, pagSorted);
	}

	@Override
	public void deleteLibro(Long id) {
		libroRepository.deleteById(id);
	}

	@Override
	public Libro createLibro(Libro libro) {
		return libroRepository.save(libro);
	}

	@Override
	public Libro updateLibro(Libro libro) {
		return libroRepository.save(libro);
	}
}
