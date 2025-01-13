package com.example.demo.service;


import com.example.demo.entity.Prestamo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PrestamoService {

    List<Prestamo> getAllPrestamos();
    void addPrestamo(Long idUsuario, Long libroId) throws Exception;
    void devolucion(Long libroId);
    List<Prestamo> getPrestamosByUserId(Long userId);
    Prestamo getPrestamo(Long prestamoId);
    List<Long> getAllPrestamosIdLibro();
    List<Long> getLibrosIdPrestadosDeLosDemas();
}
