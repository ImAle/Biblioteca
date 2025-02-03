package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.service.UserService;
import com.example.demo.service.jwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    @Qualifier("usuarioService")
    private UserService userService;

    @Autowired
    @Qualifier("token")
    private jwtService jwtService;

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        System.out.println("Metodo de prueba llamado");
        return ResponseEntity.ok("Funciona");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registro(@RequestBody Usuario usuario){
        System.out.println("Se usa el metodo");
        boolean registrado = userService.registrar(usuario);
        if (!registrado) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }
        return ResponseEntity.ok("Usuario registrado");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("email") String email, @RequestParam("password") String password){

        UserDetails user = userService.loadUserByUsername(email);
        if (user != null && Objects.equals(userService.passwordEncoder().encode(password), user.getPassword())){
            String token = jwtService.generateToken(user);
            Usuario usuario = userService.findByEmail(user.getUsername());
            usuario.setToken(token);
            userService.save(usuario);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.badRequest().body("Credenciales incorrectas");
    }
}
