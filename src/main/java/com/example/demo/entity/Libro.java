package com.example.demo.entity;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "El titulo no puede estar vacío")
    private String titulo;
    @Nullable
    private String imagen;
    @NotBlank(message = "El autor no puede estar vacío")
    private String autor;
    @NotBlank(message = "El género no puede estar vacío")
    private String genero;
    @NotNull(message = "El año no puede estar vacío")
    private int anioPublicacion;

    @OneToMany(mappedBy = "libro")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "libro")
    private List<Prestamo> prestamos;

}

