package com.example.demo.service;


import com.example.demo.entity.Prestamo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PrestamoService {

    void addPrestamo(Long idUsuario, Long libroId) throws Exception;
    void devolucion(Long libroId);
    List<Prestamo> getPrestamos(Long userId);
    Prestamo getPrestamo(Long prestamoId);
    List<Long> getAllPrestamosId();
}
