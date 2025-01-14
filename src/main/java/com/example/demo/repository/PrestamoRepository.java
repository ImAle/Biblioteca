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


}
