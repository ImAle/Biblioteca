package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
@NoArgsConstructor
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private MultipartFile imagen;
    private String autor;
    private String genero;
    private int anioPublicacion;

    @OneToMany(mappedBy = "libro")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "libro")
    private List<Prestamo> prestamos;

}

