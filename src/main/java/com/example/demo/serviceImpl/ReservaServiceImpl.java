package com.example.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Reserva;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.ReservaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.ReservaService;

@Service("reservaService")
public class ReservaServiceImpl implements ReservaService{
	
	@Autowired
	@Qualifier("reservaRepository")
	private ReservaRepository reservaRepository;
	
	@Autowired
	@Qualifier("usuarioRepository")
	private UsuarioRepository usuarioRepository;

	@Override
	public void addReserva(Reserva reserva) {
		reservaRepository.save(reserva);
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
	public List<Reserva> getReservasByUserId(Long userId) {
		List<Reserva> reservas = null;
		Optional<Usuario> usuario = usuarioRepository.findById(userId);
		
		if(usuario.isPresent())
			reservas = usuario.get().getReservas();
		
		return reservas;
	}

	@Override
	public void notificar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteReserva(Reserva reserva) {
		reservaRepository.delete(reserva);
	}
	
}
