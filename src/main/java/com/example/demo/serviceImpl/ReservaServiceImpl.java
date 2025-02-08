package com.example.demo.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Reserva;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.ReservaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.ReservaService;

@Service("reservaService")
public class ReservaServiceImpl implements ReservaService{
	
	@Autowired
	@Qualifier("reservaRepository")
	private ReservaRepository reservaRepository;
	
	@Autowired
	@Qualifier("usuarioRepository")
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	@Qualifier("emailService")
	private EmailService emailService;

	@Override
	public void addReserva(Reserva reserva) {
		reservaRepository.save(reserva);
		notificar(reserva.getUsuario().getEmail(), reserva.getLibro().getTitulo());
	}
	
	@Override
	public Reserva getReservaById(Long id) {
		Optional<Reserva> reserva = reservaRepository.findById(id);
		return reserva.isPresent() ? reserva.get() : null;
	}

	@Override
	public List<Reserva> getAllReservas() {
		return reservaRepository.findAll();
	}
	
	@Override
	public List<Reserva> getAllReservasPendientes() {
		return getAllReservas().stream().filter(r -> r.getEstado().equalsIgnoreCase("pendiente")).toList();
	}


	@Override
	@Transactional
	public List<Reserva> getReservasByUserId(Long userId) {
		Optional<Usuario> usuario = usuarioRepository.findById(userId);
		return (usuario.isPresent()) ? usuario.get().getReservas() : null;
	}
	
	@Override
	public List<Reserva> getReservasPendientesByUserId(Long userId) {
		Optional<Usuario> usuario = usuarioRepository.findById(userId);
		return (usuario.isPresent()) ? usuario.get().getReservas().stream().filter(r -> "pendiente".equalsIgnoreCase(r.getEstado())).toList() : null;
	}
	
	@Override
	public List<Long> getLibrosIdReservasPendientesByUserId(Long userId) {
		List<Long> libros = null;
		Optional<Usuario> usuario = usuarioRepository.findById(userId);
		
		if(usuario.isPresent()) {
			List<Reserva> reservas = usuario.get().getReservas().stream().filter(r -> "pendiente".equalsIgnoreCase(r.getEstado())).toList();
			libros = reservas.stream().map(r -> r.getLibro()).map(l -> l.getId()).toList();
		}
		
		return libros;
	}

	@Override
	public void notificar(String email, String titulo) {
		emailService.sendSimpleEmail(email, 
				"El libro \"" + titulo + "\" ya vuelve a estar disponible!", 
				"Accede a la plataforma y pidelo prestado antes de que otro lo haga!");
	}

	@Override
	public void cancelarReserva(Reserva reserva) {
		reservaRepository.delete(reserva);
	}
	
	@Override
	public void updateReserva(Reserva reserva) {
		reservaRepository.save(reserva);
	}
	
	@Override
	public Page<Reserva> getReservasFiltered(LocalDate desde, LocalDate hasta, Pageable pageable) {
        return reservaRepository.findAllByFechaReservaBetween(desde, hasta, pageable);
    }
	
}
