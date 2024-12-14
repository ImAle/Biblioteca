package com.example.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.LibroRepository;
import com.example.demo.service.LibroService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("")
public class HomeController {
	
	@Autowired
	@Qualifier("usuarioService")
	UserService userService;
	
	@GetMapping("/")
	public String redirectIndex() {
		return "redirect:/index";
	}
	
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
	public String registerUser(@Valid @ModelAttribute Usuario usuario, BindingResult result,
							   RedirectAttributes redirectAttributes, Model model) {
		if(result.hasErrors()){
			return "/register";
		}

		String pagina = "/register";
		
		if(userService.registrar(usuario)) {
			redirectAttributes.addFlashAttribute("success", "Registro exitoso. Ahora puedes iniciar sesión.");
			pagina = "redirect:/login";
		}
		else
	        model.addAttribute("error", "El correo ya está registrado. Intenta con otro.");
	    
	    return pagina;
	}

	
	@GetMapping("/index")
	public String indexPage() {
		return "index";
	}
	
	@GetMapping("/contacto")
	public String contactoPage() {
		return "contacto";
	}	
	
	// ====== Para probar el error 500 ======
	/*
	@GetMapping("/prueba")
	public String prueba(Model model) {
		 model.addAttribute("usuario", new Usuario());
		 int i = 6/0;
		 return INDEX_VIEW;
	 }
	 */
}
