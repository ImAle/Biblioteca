package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.entity.Libro;
import com.example.demo.entity.Reserva;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservasDelUsuarioDto {
	
	private Long id;
	private LocalDate fechaReserva;
    private String estado; // "pendiente", "notificada"
    private long idLibro;
    private String tituloLibro;
    
	public ReservasDelUsuarioDto fromEntityToDto(Reserva reserva) {
		ReservasDelUsuarioDto reservaDto = new ReservasDelUsuarioDto();
		Libro libro = reserva.getLibro();
		
		reservaDto.setId(reserva.getId());
		reservaDto.setFechaReserva(reserva.getFechaReserva());
		reservaDto.setEstado(reserva.getEstado());
		reservaDto.setIdLibro(libro.getId());
		reservaDto.setTituloLibro(libro.getTitulo());
		
		return reservaDto;
	}

}
