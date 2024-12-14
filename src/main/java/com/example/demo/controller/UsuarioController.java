package com.example.demo.controller;

import com.example.demo.dto.UsuarioDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Usuario;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UsuarioController {

	@Autowired
	@Qualifier("usuarioService")
	UserService userService;

	@GetMapping("/perfil")
	public String perfilPage(@AuthenticationPrincipal Usuario usuario, Model model) {
		ModelMapper modelMapper = new ModelMapper();
		model.addAttribute("usuario", modelMapper.map(usuario, UsuarioDto.class));
		return "perfil";
	}
	
	@PostMapping("/perfil")
	public String updatePerfilPage(@Valid @ModelAttribute("usuario") UsuarioDto usuarioDto,
								   BindingResult result, @AuthenticationPrincipal Usuario usuario) {
		if (result.hasErrors()) {
	        return "perfil";
	    }
		
		usuario.setNombre(usuarioDto.getNombre());
		usuario.setApellido(usuarioDto.getApellido());
		usuario.setEmail(usuarioDto.getEmail());
		userService.updateUsuario(usuario);
		return "redirect:/user/perfil";
	}
	
	@PreAuthorize("ROLE_ADMIN")
	@GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", userService.findAll());
        return "listaUsuarios";
    }

	@PostMapping("/usuarios/activar/{id}")
	public String activarUsuario(@PathVariable("id") Long userId) {
		userService.activarUsuario(userId);
		return "redirect:/user/usuarios";
	}

	@PostMapping("/usuarios/desactivar/{id}")
	public String desactivarUsuario(@PathVariable("id") Long userId) {
		userService.desactivarUsuario(userId);
		return "redirect:/user/usuarios";
	}
	
}
