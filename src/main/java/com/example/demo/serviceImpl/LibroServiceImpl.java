package com.example.demo.serviceImpl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Libro;
import com.example.demo.entity.Prestamo;
import com.example.demo.entity.Reserva;
import com.example.demo.repository.LibroRepository;
import com.example.demo.service.LibroService;
import com.example.demo.service.PrestamoService;

@Service("libroService")
public class LibroServiceImpl implements LibroService {
	
	@Autowired
	@Qualifier("libroRepository")
	public LibroRepository libroRepository;
	
	@Autowired
	@Qualifier("prestamoService")
	public PrestamoService prestamoService;
	
	@Override
	public Optional<Libro> getLibro(Long id) {
		return libroRepository.findById(id);
	}
	
	@Override
	public Page<Libro> getAllLibros(Pageable pageable){
		return libroRepository.findAll(pageable);
	}

	@Override
	public Page<Libro> getLibrosFiltered(String titulo, String genero, String autor, Pageable pageable){
		titulo = (titulo != null && !titulo.isEmpty()) ? titulo : null;
		genero = (genero != null && !genero.isEmpty()) ? genero : null;
		autor = (autor != null && !autor.isEmpty()) ? autor : null;
		if(titulo == null && genero == null && autor == null)
			return getAllLibros(pageable);

		return libroRepository.findByFiltros(titulo, genero, autor, pageable);
	}
	
	@Override
	public List<Reserva> getReservasPedientes(Libro libro){
		return libro.getReservas().stream().filter(r -> "pendiente".equalsIgnoreCase(r.getEstado())).toList();
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

	@Override
	public List<Libro> getLibrosMasPrestados() {
		Map<Long, Integer> libroApariciones = new HashMap<>();

	    // Recorrer todos los préstamos y contar cuántas veces aparece cada libro
	    for (Prestamo prestamo : prestamoService.getAllPrestamos()) {
	        Long libroId = prestamo.getLibro().getId();
	        libroApariciones.put(libroId, libroApariciones.getOrDefault(libroId, 0) + 1);
	    }
	   
		// Me devuelve los libros en el orden descendente y sacando los posibles null de la lista
		return libroApariciones.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))  // Orden descendente
				.map(entry -> getLibro(entry.getKey()).orElse(null))  // Obtener el objeto Libro
				.filter(Objects::nonNull)  // Eliminar posibles null
				.toList();
	}

	@Override
	public Map<String, Integer> getNumeroLibrosPorCategoria() {
		Map<String, Integer> generosApariciones = new HashMap<>();
		List<String> generos = libroRepository.findAllUniqueGeneros();
		
		for (String genero : generos) {
			generosApariciones.put(genero, libroRepository.findByGenero(genero).size());
		}
		
		return generosApariciones;
	}
}
