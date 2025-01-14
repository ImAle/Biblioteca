package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Prestamo;

import java.util.List;
import java.util.Optional;

@Repository("prestamoRepository")
public interface PrestamoRepository extends JpaRepository<Prestamo, Long>{
	
    List<Prestamo> findByUsuario_IdNot(Long idUser);
    
    @Query("SELECT p FROM Prestamo p WHERE p.usuario.id = :usuarioId AND (p.fechaFin IS NULL OR p.fechaFin > CURRENT_DATE)")
    List<Prestamo> findPrestamosActivosByUsuarioId(@Param("usuarioId") Long usuarioId);
}
