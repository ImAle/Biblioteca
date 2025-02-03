package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;


@Service("usuarioService")
public class UserService implements UserDetailsService{
	
	@Autowired
	@Qualifier("usuarioRepository")
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(username);
		
		if (usuario == null) {
	        throw new UsernameNotFoundException("Usuario no encontrado");
	    }
	    
	    return usuario;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public boolean registrar(Usuario user) {
		boolean exito = true;
		
		if(findByEmail(user.getEmail()) != null)
			exito = false;
		else 
			save(user);
		
		return exito;
	}
	
	public Usuario save(Usuario usuario) {
		usuario.setPassword(passwordEncoder().encode(usuario.getPassword()));
		usuario.setEnabled(true);
		usuario.setRol("ROLE_USER");
		return usuarioRepository.save(usuario);
	}
	
	public Usuario findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
	
	public Usuario updateUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	public List<Usuario> findAll(){
		return usuarioRepository.findAll();
	}

	public void activarUsuario(Long id){
		Optional<Usuario> usuario =  usuarioRepository.findById(id);
		if(usuario.isPresent()) {
			usuario.get().setEnabled(true);
			usuarioRepository.save(usuario.get());
		}
	}

	public void desactivarUsuario(Long id){
		Optional<Usuario> usuario =  usuarioRepository.findById(id);
		if(usuario.isPresent()) {
			usuario.get().setEnabled(false);
			usuarioRepository.save(usuario.get());
		}
	}
	
	public List<Usuario> getUsuariosNoAdmin(){
		return usuarioRepository.findByRolNot("ROLE_ADMIN");
	}

	public int contarUsuariosNoAdmin(){
		return getUsuariosNoAdmin().size();
	}
	
}
