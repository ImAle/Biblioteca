package com.example.demo.serviceImpl;

import com.example.demo.entity.Libro;
import com.example.demo.entity.Prestamo;
import com.example.demo.entity.Reserva;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.LibroRepository;
import com.example.demo.repository.PrestamoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.PrestamoService;
import com.example.demo.service.ReservaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service("prestamoService")
public class PrestamoServiceImpl implements PrestamoService {;

    @Autowired
    @Qualifier("prestamoRepository")
    private PrestamoRepository prestamoRepository;

    @Autowired
    @Qualifier("usuarioRepository")
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Qualifier("libroRepository")
    private LibroRepository libroRepository;
    
    @Autowired
	@Qualifier("emailService")
	private EmailService emailService;
    
    @Autowired
    @Qualifier("reservaService")
    private ReservaService reservaService;

    @Override
    public List<Prestamo> getAllPrestamos(){
        return prestamoRepository.findAll();
    }
    
    @Override                                     
    public void addPrestamo(Prestamo prestamo) {
    	if(getPrestamosActivosByUserId(prestamo.getUsuario().getId()).size() >= Prestamo.PRESTAMOS_LIMITES_NUM)
    		throw new IllegalStateException("Has alcanzado tu límite de prestamos");
    	
    	notificarNuevoPrestamo(prestamo.getUsuario().getEmail(), prestamo.getLibro().getTitulo(), prestamo.getFechaInicio(), prestamo.getFechaFin());
        prestamoRepository.save(prestamo);
    }

    @Override
    public void devolucion(Long PrestamoId) { // Me llega el id del prestamo ahora
    	Prestamo prestamo = getPrestamo(PrestamoId);
    	
    	if(prestamo != null) {
    		Libro libro = prestamo.getLibro();
    		
    		// Filtramos las reservas para obtener solo aquellas que no han sido notificados aún
    		// Lo ponemos en un LinkedList para asegurar el orden de cola (FIFO)
    		List<Reserva> reservas = new LinkedList<>(libro.getReservas().stream().filter(r -> "pendiente".equalsIgnoreCase(r.getEstado())).toList());
    		
    		if (!reservas.isEmpty()) {
    			Reserva reserva = reservas.getFirst();
    			// Notificar al usuario en reserva
        		reservaService.notificar(reserva.getUsuario().getEmail() , libro.getTitulo());
        		
        		reserva.setEstado("notificado");
        		reservaService.updateReserva(reserva);
    		}
    		
    		prestamo.setFechaFin(LocalDate.now());
    		notificarDevolucion(prestamo.getUsuario().getEmail(), libro.getTitulo(), prestamo.getFechaFin());
    		
    		prestamoRepository.save(prestamo);
    	}
    }

    @Override
    public List<Prestamo> getPrestamosByUserId(Long userId) {
        Optional<Usuario> usuario = usuarioRepository.findById(userId);
        return (usuario.isPresent()) ? usuario.get().getPrestamos() : null;
    }
    
    @Override
    public List<Prestamo> getPrestamosActivosByUserId(Long userId) {
        Optional<Usuario> usuario = usuarioRepository.findById(userId);
        return (usuario.isPresent()) ? prestamoRepository.findByUsuarioIdAndFechaFinAfterOrFechaFinIsNull(userId, LocalDate.now().plusDays(1)) : null;
    }
    
    @Override
    public List<Long> getLibrosIdPrestadosPorLosDemas(Long idUsuarioLogged){
    	List<Prestamo> prestamos = prestamoRepository.findByUsuario_IdNot(idUsuarioLogged);
        return getPrestamosActivosFromList(prestamos).stream().map(prestamo -> prestamo.getLibro().getId()).toList();
    }
    
    @Override
    public List<Prestamo> getPrestamosActivosFromList(List<Prestamo> prestamos){
    	return prestamos.stream().filter(r -> LocalDate.now().isBefore(r.getFechaFin())).toList();
    }
    
    @Override
    public List<Prestamo> getAllPrestamosActivos(){
    	return getAllPrestamos().stream().filter(r -> LocalDate.now().isBefore(r.getFechaFin())).toList();
    }

    @Override
    public Prestamo getPrestamo(Long prestamoId) {
        Optional<Prestamo> prestamo = prestamoRepository.findById(prestamoId);
        return prestamo.orElse(null);
    }

	@Override
	public Map<String, Integer> getNumeroPrestamosPorUsuario() {
		return usuarioRepository.findAll().stream().filter(u -> u.getRol().equals("ROLE_USER")).collect(Collectors.toMap(u -> u.getEmail(), u -> u.getPrestamos().size()));
	}

	@Override
	public List<Prestamo> getPrestamosPorMes(int mes) {
		return getAllPrestamos().stream().filter(p -> p.getFechaInicio().getMonth().getValue() == mes).toList();
	}
	
	@Override
	public Map<String, Integer> getCantidadPrestamosPorMes(){
		Map<String, Integer> mesCantidad = new LinkedHashMap<>();
		for(int i = 1; i<=12; i++) {
			String mes = Month.of(i).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"));
			mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);
			int cantidad = getPrestamosPorMes(i).size();
			mesCantidad.put(mes, cantidad);
		}
		
		return mesCantidad;
	}

	@Override
	public void notificarNuevoPrestamo(String email, String titulo, LocalDate fechainicio, LocalDate fechaFin) {
		emailService.sendSimpleEmail(email, "Nuevo libro prestado", "Su periodo de prestamo para el libro \"" + titulo 
    			+ "\" ha comenzado a fecha de " + fechainicio + " y con fecha de devolución " + fechaFin);
	}
	
	@Override
	public void notificarDevolucion(String email, String titulo, LocalDate fechaFin) {
		emailService.sendSimpleEmail(email, "Se ha completado su devolución", "Su devolución del libro \"" 
	    		+ titulo + "\" se ha realizado correctamente a fecha de " + fechaFin);
	}

	@Override
	public List<Prestamo> getPrestamosByFilter(Long libroId, Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
		return prestamoRepository.findPrestamosByFilters(libroId, usuarioId, fechaInicio, fechaFin);
	}
	
	@Override
	public boolean isLibroPrestado(Libro libro) {
		boolean respuesta = false;
		if (!getPrestamosActivosFromList(libro.getPrestamos()).isEmpty())
			respuesta = true;
		
		return respuesta;
	}
    
}
