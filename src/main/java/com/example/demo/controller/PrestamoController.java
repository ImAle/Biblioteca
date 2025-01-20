package com.example.demo.controller;

import com.example.demo.entity.Libro;

import com.example.demo.entity.Prestamo;
import com.example.demo.entity.Usuario;
import com.example.demo.service.LibroService;
import com.example.demo.service.PrestamoService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController {

	@Autowired
    @Qualifier("usuarioService")
    private UserService userService;
	
    @Autowired
    @Qualifier("prestamoService")
    private PrestamoService prestamoService;
    
    @Autowired
    @Qualifier("libroService")
    private LibroService libroService;
    
    @GetMapping("")
    public String verPrestamos(@AuthenticationPrincipal Usuario usuario, Model model) {
    	model.addAttribute("prestamos", prestamoService.getPrestamosActivosByUserId(usuario.getId()));
    	return "/usuario/prestamos";
    }
    
    @PreAuthorize("ROLE_ADMIN")
    @GetMapping("/{id}")
    public String showPrestamosByLibroId(@PathVariable("id") Long id, 
    									@RequestParam(required = false) Long usuarioId, 
    									@RequestParam(required = false) String fechaInicio,
    									@RequestParam(required = false) String fechaFin,
    									Model model) {
    	Optional<Libro> libro = libroService.getLibro(id);
    	
    	// Si no existe tal libro
    	if(libro.isEmpty()) {
    		return "/error/404";
    	}
    	
    	LocalDate inicio = (fechaInicio != null) ? LocalDate.parse(fechaInicio) : null;
    	LocalDate fin = (fechaFin != null) ? LocalDate.parse(fechaFin) : null;
    	
    	List<Prestamo> prestamos = prestamoService.getPrestamosByFilter(id, usuarioId, inicio, fin);    	
    	model.addAttribute("prestamos", prestamos);
    	
    	return "/libros/historialLibro";
    }

    @PostMapping("/{id}")
    public String prestamoLibro(@PathVariable("id") Long id, @AuthenticationPrincipal Usuario usuario, RedirectAttributes redirect){
        Optional<Libro> libro = libroService.getLibro(id);
        Libro libroPresente = (libro.isPresent()) ? libro.get() : null;
        
        if (libroPresente != null && prestamoService.getPrestamosActivosFromList(libroPresente.getPrestamos()).isEmpty()) {
        		Prestamo prestamo = new Prestamo(usuario, libroPresente, LocalDate.now(),LocalDate.now().plusWeeks(1));
                prestamoService.addPrestamo(prestamo);
                redirect.addFlashAttribute("success", "Tu periodo de prestamo ha comenzado");
        }else {
        	redirect.addFlashAttribute("error", "Error al procesar el préstamo.");
        }
        
        return "redirect:/libros";
    }

    @PostMapping("/devolver/{id}")
    public String devolucion(@PathVariable("id")Long id, HttpServletRequest request , RedirectAttributes redirectAttributes){
    	// Obtener la URL previa
	    String referer = request.getHeader("Referer");
    	
    	prestamoService.devolucion(id);
        redirectAttributes.addFlashAttribute("success", "Devolución completada con exito");

        return "redirect:" + referer;
    }
}
