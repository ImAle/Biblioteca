package com.example.demo.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Usuario implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String rol;
    private boolean enabled;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<Prestamo> prestamos;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(rol));
	}

	@Override
	public String getUsername() {
		return this.email;
	}
	
}

