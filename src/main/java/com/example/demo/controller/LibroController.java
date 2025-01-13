package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import com.example.demo.service.PrestamoService;
import com.example.demo.upload.FileSystemStorageService;
import com.example.demo.upload.FileUploadController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
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

	@Autowired
	@Qualifier("fileService")
	private FileSystemStorageService fileService;

	@Autowired
	@Qualifier("prestamoService")
	private PrestamoService prestamoService;

	@GetMapping("")
	public String getLibros(@AuthenticationPrincipal Usuario usuario,
							@RequestParam(defaultValue = "0") int page,
							@RequestParam(defaultValue = "12") int size,
							@RequestParam(defaultValue = "titulo") String campo,
							@RequestParam(defaultValue = "asc") String direccion,
							@RequestParam(required = false, name = "busqueda") String busqueda,
							@RequestParam(defaultValue = "titulo", name = "filtro") String filtro,
							Model model){

		String pagina = "libros";
		direccion = direccion.equalsIgnoreCase("desc") ? "desc" : "asc"; // Controlar solo valores válidos.
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direccion), campo));

		// Determinar parámetros según el filtro
		String titulo = filtro.equals("titulo") ? busqueda : null;
		String genero = filtro.equals("genero") ? busqueda : null;
		String autor = filtro.equals("autor") ? busqueda : null;

		Page<Libro> libros = libroService.getLibrosFiltered(titulo, genero, autor, pageable);
		libros.forEach(libro -> libro.getPrestamos());

	    model.addAttribute("libros", libros);
		model.addAttribute("titulo", titulo);
		model.addAttribute("genero", genero);
		model.addAttribute("autor", autor);
		model.addAttribute("campo", campo);
		model.addAttribute("direccion", direccion);

		if(usuario != null && usuario.getRol().equals("ROLE_ADMIN")) {
			pagina = "listaLibros";
		} else if (usuario != null && usuario.getRol().equals("ROLE_USER")) {
			List<Long> misPrestamos = prestamoService.getPrestamosByUserId(usuario.getId()).stream().map(prestamo -> prestamo.getLibro().getId()).toList();
			List<Long> prestamos = prestamoService.getLibrosIdPrestadosDeLosDemas();
			model.addAttribute("miPrestamos", misPrestamos);
			model.addAttribute("prestamos", prestamos);
			return "prestamoLibros";
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
	public String postForm(@Valid @ModelAttribute("libro") Libro libro, BindingResult result,
						   RedirectAttributes redirect, @RequestParam("file") MultipartFile file) {
		if(result.hasErrors()) {
			return "libroForm";
		}

		libroService.createLibro(libro);

		if(!file.isEmpty()){
			String imagen = fileService.store(file, libro.getId());
			libro.setImagen(MvcUriComponentsBuilder.
					fromMethodName(FileUploadController.class, "serveFile", imagen).build().toUriString());
		}

		libroService.updateLibro(libro);

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
	public String showUpdateForm(@Valid @ModelAttribute("libro") Libro libro, BindingResult result,
								 Model model, @RequestParam("file") MultipartFile file) {

		if (result.hasErrors()) {
			return "libroForm";
		}

		if(!file.isEmpty()){
			String imagen = fileService.store(file, libro.getId());
			libro.setImagen(MvcUriComponentsBuilder.
					fromMethodName(FileUploadController.class, "serveFile", imagen).build().toUriString());
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
