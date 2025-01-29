package com.example.demo.entity;

import java.util.LinkedList;
import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
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
    private List<Reserva> reservas = new LinkedList<Reserva>();

    @OneToMany(mappedBy = "libro")
    private List<Prestamo> prestamos;

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Nullable
    public String getImagen() {
        return imagen;
    }

    public void setImagen(@Nullable String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Libro(){

    }

    public Libro(Long id, String titulo, @Nullable String imagen, String autor, String genero, int anioPublicacion, List<Reserva> reservas, List<Prestamo> prestamos) {
        this.id = id;
        this.titulo = titulo;
        this.imagen = imagen;
        this.autor = autor;
        this.genero = genero;
        this.anioPublicacion = anioPublicacion;
        this.reservas = reservas;
        this.prestamos = prestamos;
    }
}

