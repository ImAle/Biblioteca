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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

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
        List<Prestamo> prestamo = prestamoRepository.findLibroId(libroId);
        logger.warning("Valor de prestamo " + prestamo);
        if (!prestamo.isEmpty())
            prestamoRepository.deleteById(prestamo.getFirst().getId());
    }

    @Override
    public List<Prestamo> getPrestamos(Long userId) {
        List<Prestamo> prestamos = null;
        Optional<Usuario> usuario = usuarioRepository.findById(userId);

        if (usuario.isPresent())
            prestamos = usuario.get().getPrestamos();

        return prestamos;
    }

    public List<Long> getAllPrestamosId(){
        return prestamoRepository.getIdLibrosPrestados();
    }

    @Override
    public Prestamo getPrestamo(Long prestamoId) {
        Optional<Prestamo> prestamo = prestamoRepository.findById(prestamoId);
        return (prestamo.isPresent()) ? prestamo.get() : null;
    }

}
