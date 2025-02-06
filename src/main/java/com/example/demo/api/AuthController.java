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
    	
    	List<String> errores = new ArrayList<>();	
    	
		if (!password.equals(passwordConfirmation))
			return ResponseEntity.badRequest().body("Las constraseñas no coinciden");

        try {
			Usuario usuario = new Usuario();
			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setEmail(email);
			usuario.setImagen(null);
			usuario.setPassword(password);
			
			// Checkeo con las validaciones que hay en la entidad usuario
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<Usuario>> violaciones = validator.validate(usuario);

			for (ConstraintViolation<Usuario> violacion : violaciones) {
				errores.add(violacion.getMessage());
				}

			 if (!errores.isEmpty()) {
				 return ResponseEntity.badRequest().body(errores);
			    }

        	String token = authService.registro(usuario);
        	
        	return ResponseEntity.ok(Map.of("token", token));
        	
        }catch(Exception e) {
        	return ResponseEntity.badRequest().body("Error inesperado");
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
