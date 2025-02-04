package com.example.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.LibroService;
import com.example.demo.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
	
	@Autowired
	@Qualifier("reservaService")
	private ReservaService reservaService;
	
	@PostMapping("/reservar")
	public ResponseEntity<?> reservar() {
		return null;
	}
	
	@GetMapping("/consultar")
	public ResponseEntity<?> consultar() {
		return null;
	}
	
	@GetMapping("/historico")
	public ResponseEntity<?> historial() {
		return null;
	}
	
	
}
