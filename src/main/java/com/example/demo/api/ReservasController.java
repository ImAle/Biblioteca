package com.example.demo.api;

import com.example.demo.dto.ReservasDelUsuarioDto;
import com.example.demo.entity.Libro;
import com.example.demo.entity.Reserva;
import com.example.demo.entity.Usuario;
import com.example.demo.service.JwtService;
import com.example.demo.service.LibroService;
import com.example.demo.service.PrestamoService;
import com.example.demo.service.UserService;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

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
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservasController {

	@Autowired
	@Qualifier("reservaService")
	private ReservaService reservaService;

	@Autowired
	@Qualifier("libroService")
	private LibroService libroService;

	@Autowired
	@Qualifier("prestamoService")
	private PrestamoService prestamoService;

	@Autowired
	@Qualifier("jwtService")
	private JwtService jwtService;

	@Autowired
	@Qualifier("usuarioService")
	private UserService userService;

	@GetMapping("/consultar")
	public ResponseEntity<?> consultar(
			@RequestHeader("Authorization") String token,
			@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
			@PageableDefault(size = 10) Pageable pageable) {

		if (!jwtService.isAdmin(token))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes autorización"));

		Page<Reserva> reservas = reservaService.getReservasFiltered(desde, hasta, pageable);

		if (reservas.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("mensaje","No hay reservas en estas fechas"));

		ReservasDelUsuarioDto reservasDelUsuarioDto = new ReservasDelUsuarioDto();

		List<ReservasDelUsuarioDto> reservasDto = reservas.getContent().stream().map(reserva -> reservasDelUsuarioDto.fromEntityToDto(reserva)).toList();

		return ResponseEntity.ok().body(Map.of("reservas", reservasDto));
	}

	@DeleteMapping("/borrar")
	public ResponseEntity<?> borrar(@RequestHeader("Authorization") String token,
			@RequestParam("id") long id) {

		if (!jwtService.isAdmin(token))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes autorización"));


			Reserva reserva = reservaService.getReservaById(id);
			if (reserva == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","No existe esta reserva"));

			reservaService.cancelarReserva(reserva);
			return ResponseEntity.ok().body(Map.of("mensaje","Reserva cancelada"));

	}

		@PostMapping("/reservar")
		public ResponseEntity<?> reservar(@RequestHeader("Authorization") String token, @RequestParam("id") long id) {

			if (!jwtService.isUser(token)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes autorización"));
			}

			Usuario usuario = jwtService.getUser(token);
			if(usuario == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Este usuario no existe"));

			Optional<Libro> libroOno = libroService.getLibro(id);

			if(libroOno.isEmpty())
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","No existe tal libro"));

			Libro libro = libroOno.get();

			if (!prestamoService.isLibroPrestado(libro))
				return ResponseEntity.badRequest().body(Map.of("mensaje","Este libro no está disponible para ser reservado"));

			Reserva reserva = new Reserva(libro, usuario);

			reservaService.addReserva(reserva);

			return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje","Libro reservado correctamente"));
		}

		@GetMapping("/misReservas")
		public ResponseEntity<?> misReservas(@RequestHeader("Authorization") String token){

			if (!jwtService.isUser(token)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes autorización"));
			}

			Usuario usuario = jwtService.getUser(token);

			if(usuario == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Este usuario no existe"));

			ReservasDelUsuarioDto reservasDelUsuarioDto = new ReservasDelUsuarioDto();

			// Mapeando las reservas del usuario a un formato adecuado para el frontend
			List<ReservasDelUsuarioDto> reservasDto = reservaService.getReservasPendientesByUserId(usuario.getId()).stream()
					.map(reservasDelUsuarioDto::fromEntityToDto)
					.toList();

			return ResponseEntity.ok().body(Map.of("reservas", reservasDto));

		}


		@GetMapping("/historico")
		public ResponseEntity<?> historial(@RequestHeader("Authorization") String token) {

			if (!jwtService.isUser(token)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes autorización"));
			}

			Usuario usuario = jwtService.getUser(token);

			if(usuario == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Este usuario no existe"));

			ReservasDelUsuarioDto reservasDelUsuarioDto = new ReservasDelUsuarioDto();

			// Mapeando las reservas del usuario a un formato adecuado para el frontend
			List<ReservasDelUsuarioDto> reservasDto = usuario.getReservas().stream()
					.map(reservasDelUsuarioDto::fromEntityToDto)
					.toList();

			return ResponseEntity.ok().body(Map.of("reservas", reservasDto));

		}

	}
