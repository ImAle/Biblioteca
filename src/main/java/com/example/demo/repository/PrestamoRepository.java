package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Prestamo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("prestamoRepository")
public interface PrestamoRepository extends JpaRepository<Prestamo, Long>{
	
    List<Prestamo> findByUsuario_IdNot(Long idUser);
    List<Prestamo> findByUsuarioIdAndFechaFinAfterOrFechaFinIsNull(Long usuarioId, LocalDate fecha);
    @Query("SELECT p FROM Prestamo p WHERE p.libro.id = :libroId " +
            "AND (:usuarioId IS NULL OR p.usuario.id = :usuarioId) " +
            "AND (:fechaInicio IS NULL OR p.fechaInicio >= :fechaInicio) " +
            "AND (:fechaFin IS NULL OR p.fechaFin <= :fechaFin)")
     List<Prestamo> findPrestamosByFilters(@Param("libroId") Long libroId, 
    		 								@Param("usuarioId") Long usuarioId, 
    		 								@Param("fechaInicio") LocalDate fechaInicio, 
    		 								@Param("fechaFin") LocalDate fechaFin);

}
