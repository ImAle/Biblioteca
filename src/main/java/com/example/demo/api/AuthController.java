package com.example.demo.api;

import com.example.demo.entity.Usuario;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
									  @RequestParam String passwordConfirmation,
									  @RequestParam String imagen){
    	System.out.println("Registroooo");
		if (!password.equals(passwordConfirmation))
			return ResponseEntity.badRequest().body("Las constraseñas no coinciden");

        try {
			Usuario usuario = new Usuario();
			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setEmail(email);
			usuario.setImagen(imagen);
			usuario.setPassword(password);

        	String token = authService.registro(usuario);
        	
        	return ResponseEntity.ok(Map.of("token", token));
        	
        }catch(RuntimeException rte) {
        	return ResponseEntity.badRequest().body(rte.getMessage());
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
    		return ResponseEntity.badRequest().body(e.getMessage()); 
    	}
    	
    }
}
