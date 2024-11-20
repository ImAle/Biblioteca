package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/principal")
public class BibliotecaController {
	
	private static final String INDEX_VIEW = "index";
	
	@GetMapping("/index")
	public String indexPage() {
		return "index";
	}
	
}
