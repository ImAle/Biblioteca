package com.example.demo.api;

import com.example.demo.entity.Usuario;
import com.example.demo.service.AuthService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
			return ResponseEntity.badRequest().body("Las constraseñas no coinciden");

		Usuario usuario = new Usuario(nombre, apellido, email, password);

		try{
			List<String> respuesta = authService.registro(usuario);

			if (respuesta.size() > 1)
				return ResponseEntity.badRequest().body(respuesta);

			return ResponseEntity.ok(Map.of("token", respuesta.getFirst()));

		}catch (RuntimeException rte){
			return ResponseEntity.badRequest().body(rte.getMessage());
		}
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password){
    	try {
    		String token = authService.login(email, password);
    		return ResponseEntity.ok(Map.of("token", token));
    	}catch(BadCredentialsException bcex) {
    		return ResponseEntity.badRequest().body("Credenciales inválidas");
    	}catch(AuthenticationException aex) {
    		return ResponseEntity.badRequest().body("Fallo al autenticarse"); 
    	}catch(Exception e) {
    		return ResponseEntity.badRequest().body("Error inseperado"); 
    	}
    	
    }
}
