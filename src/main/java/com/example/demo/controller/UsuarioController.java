package com.example.demo.controller;

import com.example.demo.dto.UsuarioDto;
import com.example.demo.entity.Prestamo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Usuario;
import com.example.demo.service.LibroService;
import com.example.demo.service.PrestamoService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UsuarioController {

	@Autowired
	@Qualifier("usuarioService")
	private UserService userService;
	
	@Autowired
	@Qualifier("prestamoService")
	private PrestamoService prestamoService;
	
	@Autowired
	@Qualifier("libroService")
	private LibroService libroService;

	@GetMapping("/perfil")
	public String perfilPage(@AuthenticationPrincipal Usuario usuario, Model model) {
		ModelMapper modelMapper = new ModelMapper();
		model.addAttribute("usuario", modelMapper.map(usuario, UsuarioDto.class));
		return "/usuario/perfil";
	}
	
	@PostMapping("/perfil")
	public String updatePerfilPage(@Valid @ModelAttribute("usuario") UsuarioDto usuarioDto,
								   BindingResult result, @AuthenticationPrincipal Usuario usuario) {
		if (result.hasErrors()) {
	        return "/usuario/perfil";
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
        return "/admin/listaUsuarios";
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

	@PreAuthorize("ROLE_ADMIN")
	@GetMapping("/informes")
	public String informeUsuario(Model model){
		getHistorialDatos(model);
		return "/admin/informes";
	}
	
	@PreAuthorize("ROLE_ADMIN")
	@GetMapping("/informes/{id}")
	public String getHistorialInforme(@PathVariable("id") Long userId, Model model){
		getHistorialDatos(model);
		List<Prestamo> prestamos = prestamoService.getPrestamosByUserId(userId);
		if (prestamos.isEmpty()) {
			System.out.println(prestamos);
			model.addAttribute("error", "Este usuario no tiene prestamos");
		}

		model.addAttribute("historialPrestamo", prestamos);

		return "/admin/informes";
	}
	
	private void getHistorialDatos(Model model) {
	    model.addAttribute("usuarios", userService.getUsuariosNoAdmin());
	    model.addAttribute("librosPrestados", libroService.getLibrosMasPrestados());
	    model.addAttribute("numUsuario", userService.contarUsuariosNoAdmin());
	}
		
}
