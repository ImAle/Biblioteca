package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController {

    @Autowired
    @Qualifier("prestamoService")
    private PrestamoService prestamoService;

    @PostMapping("/{id}")
    public String prestamoLibro(@PathVariable("id") Long id, @AuthenticationPrincipal Usuario usuario, RedirectAttributes redirect){
        try {
            prestamoService.addPrestamo(usuario.getId(), id);
            redirect.addFlashAttribute("success", "Libro prestado correctamente");

        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/libros";
    }

    @PostMapping("/devolver/{id}")
    public String devolucion(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){
        System.out.println("id recibida " + id);
        prestamoService.devolucion(id);
        redirectAttributes.addFlashAttribute("mensaje", "Libro devuelto correctamente.");

        return "redirect:/libros";
    }
}
