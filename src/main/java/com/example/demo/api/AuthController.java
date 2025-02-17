package com.example.demo.api;

import com.example.demo.entity.Usuario;
import com.example.demo.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    @Qualifier("authService")
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registro(@RequestParam String nombre,
									  @RequestParam String apellido,
									  @RequestParam String email,
									  @RequestParam String password,
									  @RequestParam String passwordConfirmation){

		if (!password.equals(passwordConfirmation))
			return ResponseEntity.badRequest().body(Map.of("error", "Las constraseñas no coinciden"));

		Usuario usuario = new Usuario(nombre, apellido, email, password);

		try{
			List<String> respuesta = authService.registro(usuario);

			if (respuesta.size() > 1)
				return ResponseEntity.badRequest().body(respuesta);

			return ResponseEntity.ok(Map.of("token", respuesta.getFirst()));

		}catch (RuntimeException rte){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(rte.getMessage());
		}
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password){
    	try {
    		String token = authService.login(email, password);
    		return ResponseEntity.ok(Map.of("token", token));
    	}catch(BadCredentialsException bcex) {
    		return ResponseEntity.badRequest().body(Map.of("error", "Credenciales inválidas"));
    	}catch(AuthenticationException aex) {
    		return ResponseEntity.badRequest().body(Map.of("error", "Fallo al autenticarse"));
    	}catch(Exception e) {
    		return ResponseEntity.badRequest().body(Map.of("error", "Error inseperado"));
    	}
    	
    }
}
