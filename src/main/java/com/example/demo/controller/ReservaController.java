package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Libro;
import com.example.demo.entity.Reserva;
import com.example.demo.entity.Usuario;
import com.example.demo.service.LibroService;
import com.example.demo.service.ReservaService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservar")
public class ReservaController {
	
	@Autowired
	@Qualifier("reservaService")
	private ReservaService reservaService;
	
	@Autowired
	@Qualifier("libroService")
	private LibroService libroService;
	
	@Autowired
	@Qualifier("usuarioService")
	private UserService userService;

	@GetMapping("")
	public String verReservas(@AuthenticationPrincipal Usuario usuario,
							  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
							  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
							  Model model) {
		String pagina = "/usuario/reservas";
		List<Reserva> reservas = reservaService.getReservasPendientesByUserId(usuario.getId());

		if(usuario != null && usuario.getRol().equals("ROLE_ADMIN")) {
			pagina = "/admin/reservas";

			if (fechaInicio != null && fechaFin != null) {
				Pageable pageable = PageRequest.of(0, 12);
				reservas = reservaService.getReservasFiltered(fechaInicio, fechaFin, pageable).getContent();
			} else {
				reservas = reservaService.getAllReservasPendientes();
			}
		}

		model.addAttribute("reservas", reservas);

		return pagina;
	}
	
	@PostMapping("/{id}")
	public String reservarLibro(@PathVariable("id") Long id, @AuthenticationPrincipal Usuario usuario, RedirectAttributes redirect) {
		Optional<Libro> libro = libroService.getLibro(id);
		
		if (libro.isPresent()) {
			Reserva reserva = new Reserva(libro.get(), usuario);
			redirect.addFlashAttribute("success", libro.get().getTitulo() + " ha sido reservado");
			reservaService.addReserva(reserva);
		}else{
			redirect.addFlashAttribute("error", "Ha sucedido un error, inténtelo más tarde");
		}
		
		return "redirect:/libros";
	}
	
	@PostMapping("/cancelar/{id}")
	public String cancelarReserva(@PathVariable("id") Long id, HttpServletRequest request, @AuthenticationPrincipal Usuario usuario, RedirectAttributes redirect) {
		Optional<Libro> libro = libroService.getLibro(id);
		
		// Obtener la URL previa
	    String referer = request.getHeader("Referer");
		
		if (libro.isPresent()) {
			Reserva reserva = reservaService.getReservasByUserId(usuario.getId()).stream().filter(r -> r.getLibro().equals(libro.get()))	
			.findFirst()
		    .orElse(null); // Obtenemos el primer libro en estado 'pendiente' del usuario que guarda relacion con el libro en cuestión
			
			if (reserva != null) {
				reservaService.cancelarReserva(reserva);
				redirect.addFlashAttribute("success", "Cancelación aplicada con exito");
			}else {
				redirect.addFlashAttribute("error", "Ha ocurrido un error, inténtelo mas tarde");
			}
		}
		
		return "redirect:" + referer; // Lo envíamos de vuelta a la página donde se realizó la acción
	}
	
	@PostMapping("/admin/cancelar/{usuario}/{id}")
	public String cancelarReservaAdmin(@PathVariable("id") Long id, @PathVariable("usuario") String email, HttpServletRequest request, RedirectAttributes redirect) {
		Optional<Libro> libro = libroService.getLibro(id);
		Usuario usuario = userService.findByEmail(email);
		
		// Obtener la URL previa
	    String referer = request.getHeader("Referer");
		
		if (libro.isPresent()) {
			Reserva reserva = reservaService.getReservasByUserId(usuario.getId()).stream().filter(r -> r.getLibro().equals(libro.get()))
			.findFirst()
		    .orElse(null); // Obtenemos el primer libro en estado 'pendiente' del usuario que guarda relacion con el libro en cuestión

			if (reserva != null) {
				reservaService.cancelarReserva(reserva);
				redirect.addFlashAttribute("success", "Cancelación aplicada con exito");
			}else {
				redirect.addFlashAttribute("error", "Ha ocurrido un error, inténtelo mas tarde");
			}
		}
		
		return "redirect:" + referer; // Lo envíamos de vuelta a la página donde se realizó la acción
	}
	
}
