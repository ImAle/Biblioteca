package com.example.demo.controller;

import com.example.demo.dto.UsuarioDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	
	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
		String pagina = "redirect:/user/register";
		
		if(userService.registrar(usuario)) {
			redirectAttributes.addFlashAttribute("success", "Registro exitoso. Ahora puedes iniciar sesión.");
			pagina = "redirect:/user/login";
		}
		else
	        redirectAttributes.addFlashAttribute("error", "El correo ya está registrado. Intenta con otro.");
	        
	    
	    return pagina;
	}

	@GetMapping("/perfil")
	public String perfilPage(@AuthenticationPrincipal Usuario usuario, Model model) {
		ModelMapper modelMapper = new ModelMapper();
		model.addAttribute("usuario", modelMapper.map(usuario, UsuarioDto.class));
		return "perfil";
	}
	
	@PostMapping("/perfil")
	public String updatePerfilPage(@Valid @ModelAttribute("usuario") UsuarioDto usuarioDto, BindingResult result, Model model, @AuthenticationPrincipal Usuario usuario) {
		if (result.hasErrors()) {
	        model.addAttribute("usuario", usuarioDto);
	        return "perfil"; // Vuelve al formulario con los errores
	    }
		
		usuario.setNombre(usuarioDto.getNombre());
		usuario.setApellido(usuarioDto.getApellido());
		usuario.setEmail(usuarioDto.getEmail());
		userService.updateUsuario(usuario);
		return "redirect:/user/perfil";
	}
}
