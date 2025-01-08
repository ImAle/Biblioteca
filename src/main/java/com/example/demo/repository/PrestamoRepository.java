package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Prestamo;

import java.util.List;

@Repository("prestamoRepository")
public interface PrestamoRepository extends JpaRepository<Prestamo, Long>{

    // Metodo para contar cuantos libros tiene el usuario prestados
    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.usuario.id = :user AND p.fechaFin >= CURRENT_DATE")
    int prestamosUsuario(@Param("user") Long user);

    // Comprueba si el libro esta prestado
    @Query("SELECT COUNT(p) > 0 FROM Prestamo p WHERE p.libro.id = :idLibro")
    boolean isLibroPrestado(@Param("idLibro") Long idLibro);

    @Query("SELECT p.libro.id FROM Prestamo p WHERE p.libro IS NOT NULL")
    List<Long> getIdLibrosPrestados();
}
