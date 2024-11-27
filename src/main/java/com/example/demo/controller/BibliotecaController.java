package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.LibroRepository;
import com.example.demo.service.LibroService;

@Controller
public class BibliotecaController {
	
	@GetMapping("/")
	public String redirectIndex() {
		return "redirect:/index";
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
