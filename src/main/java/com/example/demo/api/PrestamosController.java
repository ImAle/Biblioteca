package com.example.demo.api;

import com.example.demo.dto.LibrosParaReservarDto;
import com.example.demo.entity.Libro;
import com.example.demo.entity.Prestamo;
import com.example.demo.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamosController {

    @Autowired
    @Qualifier("prestamoService")
    private PrestamoService prestamoService;

    @GetMapping("")
    public ResponseEntity<?> verLibrosActivosParaReservar(){
        List<Libro> libros = prestamoService.getAllPrestamosActivos().stream()
                .map(Prestamo::getLibro).toList();

        LibrosParaReservarDto dto = new LibrosParaReservarDto();

        List<LibrosParaReservarDto> librosDto = libros.stream().map(dto::fromEntityToDto).toList();

        if(libros.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("libros", librosDto));

    }
}
