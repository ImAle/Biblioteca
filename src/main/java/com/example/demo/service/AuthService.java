package com.example.demo.service;

import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Usuario;

@Service("authService")
public class AuthService {
    
	@Autowired
    private AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("usuarioService")
    private UserService userService;
	
	@Autowired
	@Qualifier("jwtService")
    private jwtService jwtService;

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
    

	public String registro(Usuario user) throws RuntimeException{
		if (userService.findByEmail(user.getEmail()) != null)
			throw new RuntimeException("Usuario ya registrado");
		
		String token = jwtService.generateToken(user);
		user.setToken(token);
		userService.save(user);
		
		return token;
	}
}
