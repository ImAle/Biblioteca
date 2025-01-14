package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Libro;
import com.example.demo.entity.Reserva;
import com.example.demo.entity.Usuario;
import com.example.demo.service.LibroService;
import com.example.demo.service.ReservaService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/reservar")
public class ReservaController {
	
	@Autowired
	@Qualifier("reservaService")
	private ReservaService reservaService;
	
	@Autowired
	@Qualifier("libroService")
	private LibroService libroService;
	
	@GetMapping("")
	public String verReservas(@AuthenticationPrincipal Usuario usuario, Model model) {
		String pagina = "reservasUser";
		List<Reserva> reservas = reservaService.getReservasByUserId(usuario.getId());
		
		if(usuario != null && usuario.getRol().equals("ROLE_ADMIN")) {
			pagina = "reservasAdmin";
			reservas = reservaService.getAllReservasPendientes();
		}
		
		System.out.println(reservas);
		model.addAttribute("reservas", reservas);
			
		return pagina;
	}
	
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
	
	@DeleteMapping("/{id}")
	public String cancelarReserva(@PathVariable("id") Long id, HttpServletRequest request, @AuthenticationPrincipal Usuario usuario) {
		Optional<Libro> libro = libroService.getLibro(id);
		
		// Obtener la URL previa
	    String referer = request.getHeader("Referer");
		
		if (libro.isPresent()) {
			Reserva reserva = reservaService.getReservasByUserId(usuario.getId()).stream().filter(r -> r.getLibro().equals(libro.get()))	
			.findFirst()
		    .orElse(null); // Obtenemos el primer libro en estado 'pendiente' del usuario que guarda relacion con el libro en cuestión
			
			if (reserva != null)
				reservaService.cancelarReserva(reserva);
		}
		
		return "redirect:" + referer; // Lo envíamos de vuelta a la página donde se realizó la acción
	}
	
}
