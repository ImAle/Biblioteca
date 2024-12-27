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
import java.util.Optional;

@Service("prestamoService")
public class PrestamoServiceImpl implements PrestamoService {

    private static final int MAX_PRESTAMOS = 5;

    @Autowired
    @Qualifier("prestamoRepository")
    public PrestamoRepository prestamoRepository;

    @Autowired
    @Qualifier("usuarioRepository")
    public UsuarioRepository usuarioRepository;

    @Autowired
    @Qualifier("libroRepository")
    public LibroRepository libroRepository;

    public void prestamos(Long idUsuario, Long idLibro) throws Exception {

        // Obtiene la id del usuario
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Id del libro a prestar
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new Exception("Libro no encontrado"));

        // Maximo de prestamos por usuario
        if (maxPrestamos(idUsuario) >= MAX_PRESTAMOS)
            throw new Exception("No se permiten mas de 5 prestamos");

        // Comprueba si el libro esta prestado
        if (prestamoRepository.libroPrestado(idLibro))
            throw new Exception("Libro no disponible");

        // Crea y guarda el prestamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaInicio(LocalDate.now());
        prestamo.setFechaFin(LocalDate.now().plusWeeks(1));
        prestamoRepository.save(prestamo);

    }

    @Override
    public int maxPrestamos(Long idUsuario) {
        return prestamoRepository.prestamosUsuario(idUsuario);
    }

}
