package com.example.demo.dto;

import com.example.demo.entity.Libro;

public class LibrosParaReservarDto {

    private Long id;
    private String titulo;
    private String imagen;
    private String autor;
    private String genero;

    public LibrosParaReservarDto fromEntityToDto(Libro libro) {
        LibrosParaReservarDto dto = new LibrosParaReservarDto();
        dto.id = libro.getId();
        dto.titulo = libro.getTitulo();
        dto.imagen = libro.getImagen();
        dto.autor = libro.getAutor();
        dto.genero = libro.getGenero();

        return dto;
    }

    public LibrosParaReservarDto(){}

    public LibrosParaReservarDto(Long id, String titulo, String imagen, String autor, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.imagen = imagen;
        this.autor = autor;
        this.genero = genero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
