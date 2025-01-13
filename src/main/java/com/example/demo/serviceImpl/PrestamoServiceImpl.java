package com.example.demo.serviceImpl;

import com.example.demo.entity.Libro;
import com.example.demo.entity.Prestamo;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.LibroRepository;
import com.example.demo.repository.PrestamoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service("prestamoService")
public class PrestamoServiceImpl implements PrestamoService {
    Logger logger = Logger.getLogger(PrestamoServiceImpl.class.getName());

    @Autowired
    @Qualifier("prestamoRepository")
    public PrestamoRepository prestamoRepository;

    @Autowired
    @Qualifier("usuarioRepository")
    public UsuarioRepository usuarioRepository;

    @Autowired
    @Qualifier("libroRepository")
    public LibroRepository libroRepository;

    @Override
    public List<Prestamo> getAllPrestamos(){
        return prestamoRepository.findAll();
    }

    public void addPrestamo(Long idUsuario, Long idLibro) throws Exception {
        // Obtiene la id del usuario
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Id del libro a prestar
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new Exception("Libro no encontrado"));


        // Comprueba si el libro esta prestado
        if (prestamoRepository.isLibroPrestado(idLibro))
            throw new Exception("Libro no disponible");

        // Crea y guarda el prestamo
        Prestamo prestamo = new Prestamo(usuario, libro, LocalDate.now(), LocalDate.now().plusWeeks(1));
        prestamoRepository.save(prestamo);

    }

    @Override
    public void devolucion(Long libroId) {
        List<Prestamo> prestamo = prestamoRepository.findPrestamosByLibroId(libroId);
        logger.warning("Valor de prestamo " + prestamo);
        if (!prestamo.isEmpty())
            prestamoRepository.deleteById(prestamo.getFirst().getId());
    }

    @Override
    public List<Prestamo> getPrestamosByUserId(Long userId) {
        List<Prestamo> prestamos = null;
        Optional<Usuario> usuario = usuarioRepository.findById(userId);

        if (usuario.isPresent())
            prestamos = usuario.get().getPrestamos();

        return prestamos;
    }

    public List<Long> getAllPrestamosIdLibro(){
        return prestamoRepository.getIdLibrosPrestados();
    }

    public List<Long> getLibrosIdPrestadosDeLosDemas(){
        List<Long> librosIdPrestados = null;
        List<Prestamo> misPrestamos = getPrestamosByUserId(usuarioLogeado());
        List<Prestamo> prestamos = getAllPrestamos();
        List<Long> librosPrestadosDeLosDemas = null;

        if (!misPrestamos.isEmpty()) {
            librosIdPrestados = prestamos.stream().map(prestamo -> prestamo.getLibro().getId()).toList();
            List<Long> misPrestamosId = misPrestamos.stream().map(prestamo -> prestamo.getLibro().getId()).toList();
            librosPrestadosDeLosDemas = librosIdPrestados.stream().filter(id -> !misPrestamosId.contains(id)).toList();
        }

        return librosPrestadosDeLosDemas;
    }

    @Override
    public Prestamo getPrestamo(Long prestamoId) {
        Optional<Prestamo> prestamo = prestamoRepository.findById(prestamoId);
        return prestamo.orElse(null);
    }

    // Obtiene el email(que es unico) del usuario logueado
    public Long usuarioLogeado(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario userLogin = usuarioRepository.findByEmail(email);

        return userLogin.getId();
    }
}
