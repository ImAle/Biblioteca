package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Libro;
import com.example.demo.entity.Usuario;
import com.example.demo.service.LibroService;

@Controller
@RequestMapping("/libros")
public class LibroController {
	
	@Autowired
	@Qualifier("libroService")
	private LibroService libroService;

	@GetMapping("")
	public String getLibros(@AuthenticationPrincipal Usuario usuario,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size, 
			@RequestParam(required = false, name = "titulo") String titulo, Model model) {
		String pagina = "libros";
		Pageable pageable = PageRequest.of(page, size);
		Page<Libro> libros;
		
	    if (titulo == null || titulo.isEmpty()) {
	        libros = libroService.getLibros(pageable);
	    } else {
	        libros = libroService.getLibrosByName(titulo, pageable);
	    }

	    model.addAttribute("libros", libros);
	    model.addAttribute("titulo", titulo);
	    
	    if(usuario != null && usuario.getRol().equals("ROLE_ADMIN")) {
	    	pagina = "librosAdmin";
	    }
	    
		return pagina;
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
		return "LibroForm";
	}
	
	@PostMapping("/createForm")
	public String postForm(@ModelAttribute("libro") Libro libro, BindingResult result, Model model, RedirectAttributes redirect) {
		/*if(result.hasErrors()) {
			model.addAttribute("errores");
			return "libroForm";
		}*/
		
		libroService.createLibro(libro);
		redirect.addFlashAttribute("success", "Libro \"" + libro.getTitulo() + "\" creado con éxito");
		
		return "redirect:/libros";
	}
	
	@GetMapping("/updateForm")
	public String showUpdateForm(@RequestParam Long id, Model model) {
		Optional<Libro> libro = libroService.getLibro(id);
		if (libro.isPresent())
			model.addAttribute("libro", libro.get());

		return "libroForm";
	}
	
	@PostMapping("/updateForm")
	public String showUpdateForm(@ModelAttribute("libro") Libro libro, BindingResult result, Model model) {
		/*
		if (result.hasErrors()) {
			System.out.println("AHHHHHHHHHHHHH");
			model.addAttribute("errores");
			return "libroForm";
		}*/

		if(libro.getImagen() == null){
			Optional<Libro> oldLibro = libroService.getLibro(libro.getId());
            oldLibro.ifPresent(old -> libro.setImagen(old.getImagen()));
		}

		libroService.updateLibro(libro);
		model.addAttribute("success", "Libro actualizado con exito");
		
		return "libroForm";
	}

	@PreAuthorize("ROLE_ADMIN")
	@PostMapping("/{id}")
	public String deleteLibro(@PathVariable("id") Long id, RedirectAttributes redirect) {
		libroService.deleteLibro(id);
		redirect.addFlashAttribute("success", "Libro eliminado con éxito");
		return "redirect:/libros";
	}
}
