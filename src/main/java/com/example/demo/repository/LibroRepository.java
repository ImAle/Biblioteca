package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Libro;

@Repository("libroRepository")
public interface LibroRepository extends JpaRepository<Libro, Long>{
	
	Page<Libro> findByTituloAllIgnoreCaseContains(String titulo, Pageable pageable);

	@Query("SELECT l FROM Libro l WHERE " +
			"(:titulo IS NOT NULL AND LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) OR " +
			"(:genero IS NOT NULL AND LOWER(l.genero) LIKE LOWER(CONCAT('%', :genero, '%'))) OR " +
			"(:autor IS NOT NULL AND LOWER(l.autor) LIKE LOWER(CONCAT('%', :autor, '%')))")
	Page<Libro> findByFiltros(@Param("titulo") String titulo,@Param("genero") String genero,@Param("autor") String autor, Pageable pageable);
}
