package com.example.demo.service;


import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PrestamoService {

    void prestamos(Long idUsuario, Long libroId) throws Exception;
    int maxPrestamos(Long idUsuario);
}
