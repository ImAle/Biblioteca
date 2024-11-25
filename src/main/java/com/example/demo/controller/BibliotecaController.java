package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.entity.Usuario;
import com.example.demo.service.LibroService;

@Controller
@RequestMapping("/")
public class BibliotecaController {
	
	private static final String INDEX_VIEW = "index";
	private static final String LIBROS_VIEW = "libros";
	
	@GetMapping("/index")
	public String indexPage() {
		return "index";
	}
	
	@GetMapping("/libros")
	public String libros(Model model) {
		model.addAttribute("libros");
		return "libros";
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
