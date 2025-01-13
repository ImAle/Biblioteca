package com.example.demo.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Libro;
import com.example.demo.entity.Reserva;
import com.example.demo.entity.Usuario;
import com.example.demo.service.LibroService;
import com.example.demo.service.ReservaService;

@Controller
@RequestMapping("/reservar")
public class ReservaController {
	
	@Autowired
	@Qualifier("reservaService")
	private ReservaService reservaService;
	
	@Autowired
	@Qualifier("libroService")
	private LibroService libroService;
	
	@PostMapping("/{id}")
	public String reservarLibro(@PathVariable("id") Long id, @AuthenticationPrincipal Usuario usuario) {
		Optional<Libro> libro = libroService.getLibro(id);
		
		if (libro.isPresent()) {
			Reserva reserva = new Reserva();
			reserva.setEstado("pendiente");
			reserva.setUsuario(usuario);
			reserva.setFechaReserva(LocalDate.now());
			reserva.setLibro(libro.get());
			
			reservaService.addReserva(reserva);
		}
		
		return "redirect:/libros";
	}
	
}
