package com.example.demo.api;

import com.example.demo.entity.Reserva;
import com.example.demo.entity.Usuario;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.ReservaService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservasController {
	
	@Autowired
	@Qualifier("reservaService")
	private ReservaService reservaService;

	@Autowired
	@Qualifier("jwtService")
	private JwtService jwtService;

	@Autowired
	@Qualifier("usuarioService")
	private UserService userService;
	
	@PostMapping("/reservar")
	public ResponseEntity<?> reservar() {
		return null;
	}

	@GetMapping("/consultar")
	public ResponseEntity<?> consultar(
			@RequestHeader("Authorization") String token,
			@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
			@PageableDefault(size = 10) Pageable pageable) {

		if(!jwtService.isAdmin(token))
			return ResponseEntity.badRequest().body("No tienes autorizacion");

		Page<Reserva> reservas = reservaService.getReservasFiltered(desde, hasta, pageable);
		return (reservas.isEmpty()) ?
				ResponseEntity.badRequest().body("No hay reservas en estas fechas") : ResponseEntity.ok(reservas.getContent());
	}

	@DeleteMapping("/borrar")
	public ResponseEntity<?> borrar(@RequestHeader("Authorization") String token,
									@RequestParam("id") long id){

		if(!jwtService.isAdmin(token))
			return ResponseEntity.badRequest().body("No tienes autorizacion");

		Reserva reserva = reservaService.getReservaById(id);
		if (reserva == null)
			return ResponseEntity.badRequest().body("No existe esta reserva");

		reservaService.cancelarReserva(reserva);
		return ResponseEntity.ok().body("Reserva cancelada");

	}


	@GetMapping("/historico")
	public ResponseEntity<?> historial() {
		return null;
	}
	
	
}
