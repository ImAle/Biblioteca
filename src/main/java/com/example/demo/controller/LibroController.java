package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Libro;
import com.example.demo.service.LibroService;

@Controller
@RequestMapping("/libros")
public class LibroController {
	
	@Autowired
	@Qualifier("libroService")
	private LibroService libroService;

	@GetMapping("")
	public String getLibros(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
		Pageable pageable = PageRequest.of(page, size);
		model.addAttribute("libros", libroService.getLibros(pageable));
		return "libros";
	}
	
	@GetMapping("/{id}")
	public String getLibro(@PathVariable("id") Long id, Model model) {
		Optional<Libro> libro = libroService.getLibro(id);
		if(libro.isPresent())
			model.addAttribute("libro", libro);
		else
			model.addAttribute("error", "No existe este libro");
		
		return "libro";
	}
	
	@GetMapping("/createForm")
	public String showForm(Model model) {
		model.addAttribute("libro", new Libro());
		return "createLibroForm";
	}
	
	@PostMapping("/createForm")
	public String postForm(@ModelAttribute("libro") Libro libro, BindingResult result, Model model, RedirectAttributes redirect) {
		if(result.hasErrors()) {
			model.addAttribute("errores");
			return "createLibroForm";
		}
		
		libroService.createLibro(libro);
		redirect.addFlashAttribute("success", "Libro \"" + libro.getTitulo() + "\" creado con éxito");
		
		return "redirect:/libros";
	}
	
	@GetMapping("/updateForm")
	public String showUpdateForm(@ModelAttribute("libro") Libro libro, Model model) {
		model.addAttribute("libro", libro);
		return "createLibroForm";
	}
	
	@PutMapping("/updateForm")
	public String showUpdateForm(@ModelAttribute("libro") Libro libro, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("errores");
			return "createLibroForm";
		}
		
		model.addAttribute("success", "Libro actualizado con exito");
		
		return "createLibroForm";
	}
	
	@DeleteMapping("/{id}")
	public String deleteLibro(@PathVariable("id") Long id, Model model) {
		libroService.deleteLibro(id);
		model.addAttribute("success", "Libro eliminado con éxito");
		return "libros";
	}
}
