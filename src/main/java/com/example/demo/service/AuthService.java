package com.example.demo.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("authService")
public class AuthService {
    
	@Autowired
    private AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("usuarioService")
    private UserService userService;
	
	@Autowired
	@Qualifier("jwtService")
    private JwtService jwtService;

    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtService.generateToken(usuario);
        usuario.setToken(token);
        userService.updateUsuario(usuario);
        
        return token;
    }
    
	private List<String> buscarErroresRegistro(Usuario usuario){
		List<String> errores = new ArrayList<>();

		// Checkeo con las validaciones que hay en la entidad usuario
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Usuario>> violaciones = validator.validate(usuario);

		for(ConstraintViolation<Usuario> violacion : violaciones){
			errores.add(violacion.getMessage());
		}

		if (!errores.isEmpty())
			return errores;

		return null;
	}
	public List<String> registro(Usuario user) throws RuntimeException{
		if (userService.findByEmail(user.getEmail()) != null)
			throw new RuntimeException("Usuario ya registrado");

		List<String> errores = buscarErroresRegistro(user);

		if(errores != null)
			return errores;

		String token = jwtService.generateToken(user);
		user.setToken(token);
		userService.save(user);
		
		return List.of(token);
	}
}
