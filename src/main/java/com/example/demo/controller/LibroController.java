package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/libros")
public class LibroController {
	
	private static final String LIBROS_VIEW = "libros";

	@GetMapping("/get")
	public String libros(Model model) {
		model.addAttribute("libros");
		return "libros";
	}
}
