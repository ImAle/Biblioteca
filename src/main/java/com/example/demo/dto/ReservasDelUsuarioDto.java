package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.entity.Libro;
import com.example.demo.entity.Reserva;

public class ReservasDelUsuarioDto {
	
	private Long id;
	private LocalDate fechaReserva;
    private String estado; // "pendiente", "notificada"
    private long idLibro;
    private String tituloLibro;
	private String imagenLibro;
    
	public ReservasDelUsuarioDto fromEntityToDto(Reserva reserva) {
		ReservasDelUsuarioDto reservaDto = new ReservasDelUsuarioDto();
		Libro libro = reserva.getLibro();
		
		reservaDto.setId(reserva.getId());
		reservaDto.setFechaReserva(reserva.getFechaReserva());
		reservaDto.setEstado(reserva.getEstado());
		reservaDto.setIdLibro(libro.getId());
		reservaDto.setTituloLibro(libro.getTitulo());
		reservaDto.setImagenLibro(libro.getImagen());
		
		return reservaDto;
	}

	public ReservasDelUsuarioDto(){}

	public ReservasDelUsuarioDto(LocalDate fechaReserva, String estado, long idLibro, String tituloLibro, String imagenLibro) {
		this.fechaReserva = fechaReserva;
		this.estado = estado;
		this.idLibro = idLibro;
		this.tituloLibro = tituloLibro;
		this.imagenLibro = imagenLibro;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(LocalDate fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public long getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(long idLibro) {
		this.idLibro = idLibro;
	}

	public String getTituloLibro() {
		return tituloLibro;
	}

	public void setTituloLibro(String tituloLibro) {
		this.tituloLibro = tituloLibro;
	}

	public String getImagenLibro() {return imagenLibro;}

	public void setImagenLibro(String imagenLibro) {this.imagenLibro = imagenLibro;}
}
