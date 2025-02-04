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

		try {
            if (!jwtService.isAdmin(token))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes autorización");
            
            Page<Reserva> reservas = reservaService.getReservasFiltered(desde, hasta, pageable);
            
    		if (reservas.isEmpty())
    			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay reservas en estas fechas");
    		
			ReservasDelUsuarioDto reservasDelUsuarioDto = new ReservasDelUsuarioDto();
			
    		List<ReservasDelUsuarioDto> reservasDto = reservas.getContent().stream().map(reserva -> reservasDelUsuarioDto.fromEntityToDto(reserva)).toList();
    		
    		return ResponseEntity.ok().body(reservasDto);
            
            
        } catch (MalformedJwtException mje) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido: Formato incorrecto");
        } catch (SignatureException se) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido: Firma no coincide");
        }
		
	}

	@DeleteMapping("/borrar")
	public ResponseEntity<?> borrar(@RequestHeader("Authorization") String token,
									@RequestParam("id") long id){

		try {
            if (!jwtService.isAdmin(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes autorización");
            }
        } catch (MalformedJwtException mje) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido: Formato incorrecto");
        } catch (SignatureException se) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido: Firma no coincide");
        }

		Reserva reserva = reservaService.getReservaById(id);
		if (reserva == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe esta reserva");

		reservaService.cancelarReserva(reserva);
		return ResponseEntity.ok().body("Reserva cancelada");

	}
	
	@PostMapping("/reservar")
	public ResponseEntity<?> reservar(@RequestHeader("Authorization") String token, @RequestParam("id") long id) {
		Usuario usuario = jwtService.getUser(token);
		if(usuario == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Este usuario no existe");
		
		Optional<Libro> libroOno = libroService.getLibro(id);
		
		if(libroOno.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe tal libro");
		
		Libro libro = libroOno.get();
		
		if (prestamoService.isLibroPrestado(libro) == false)
			return ResponseEntity.badRequest().body("Este libro no está disponible para ser reservado");
		
		Reserva reserva = new Reserva();
		reserva.setLibro(libro);
		reserva.setUsuario(usuario);
		reserva.setEstado("pendiente");
		reserva.setFechaReserva(LocalDate.now());
		
		reservaService.addReserva(reserva);
		
		reservaService.notificar(usuario.getEmail(), libro.getTitulo());
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Libro reservado correctamente");
	}
	
	@GetMapping("/misReservas")
	public ResponseEntity<?> misReservas(@RequestHeader("Authorization") String token){
		
		try {
			Usuario usuario = jwtService.getUser(token);
			
			if(usuario == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Este usuario no existe");
			
			ReservasDelUsuarioDto reservasDelUsuarioDto = new ReservasDelUsuarioDto();
			
			List<ReservasDelUsuarioDto> reservasDto = reservaService.getReservasPendientesByUserId(usuario.getId()).stream()
					.map(reserva -> reservasDelUsuarioDto.fromEntityToDto(reserva))
					.toList();
			
			return ResponseEntity.ok().body(reservasDto);
			
        } catch (MalformedJwtException mje) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido: Formato incorrecto");
            
        } catch (SignatureException se) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido: Firma no coincide");
        }
		
	}


	@GetMapping("/historico")
	public ResponseEntity<?> historial(@RequestHeader("Authorization") String token) {
		
		try {
			Usuario usuario = jwtService.getUser(token);
			
			if(usuario == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Este usuario no existe");
			
			ReservasDelUsuarioDto reservasDelUsuarioDto = new ReservasDelUsuarioDto();
			
			List<ReservasDelUsuarioDto> reservasDto = usuario.getReservas().stream()
					.map(reserva -> reservasDelUsuarioDto.fromEntityToDto(reserva))
					.toList();
			
			return ResponseEntity.ok().body(reservasDto);
			
        } catch (MalformedJwtException mje) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido: Formato incorrecto");
            
        } catch (SignatureException se) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido: Firma no coincide");
        }	
	}
	
}
