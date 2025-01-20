package com.example.demo.service;


import com.example.demo.entity.Prestamo;
import com.example.demo.entity.Usuario;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface PrestamoService {

    List<Prestamo> getAllPrestamos();
    void addPrestamo(Prestamo prestamo);
    void devolucion(Long libroId);
    List<Prestamo> getPrestamosByUserId(Long userId);
    Prestamo getPrestamo(Long prestamoId);
    List<Prestamo> getAllPrestamosActivos();
    List<Prestamo> getPrestamosActivosFromList(List<Prestamo> prestamos);
    List<Long> getLibrosIdPrestadosPorLosDemas(Long idUsuarioLogged);
    List<Prestamo> getPrestamosActivosByUserId(Long userId);
    Map<String, Integer> getNumeroPrestamosPorUsuario();
    List<Prestamo> getPrestamosPorMes(int mes);
    Map<String, Integer> getCantidadPrestamosPorMes();
    void notificarNuevoPrestamo(String email, String titulo, LocalDate fechainicio, LocalDate fechaFin);
    void notificarDevolucion(String email, String titulo, LocalDate fechaFin);
    List<Prestamo> getPrestamosByFilter(Long libroId, Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin);
}
