package com.example.demo.entity;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
public class Usuario implements UserDetails{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Length(min = 3, max = 20, message = "Tú nombre debe estar entre los 3 caracteres y los 20 caracteres")
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacío")
    @Length(min = 2, max = 20, message = "Tú apellido debe estar entre los 2 caracteres y los 20 caracteres")
    private String apellido;
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser un email válido")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Debes proporcionar una contraseña")
    @Length(min = 8, message = "La longitud no puede ser menor a 8 caracteres")
    private String password;
    private String rol;
    private boolean enabled;
    @Nullable
    private String imagen;
    private String token;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    @ToString.Exclude // Evitar StackoverFlowException
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Prestamo> prestamos = new LinkedList<Prestamo>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(rol));
	}

	@Override
	public String getUsername() {
		return this.email;
	}


    public Usuario(){}

    public Usuario(String nombre, String apellido, String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
    }

    public Usuario(Long id, String nombre, String apellido, String email, String password, String rol, boolean enabled, @Nullable String imagen, List<Reserva> reservas, List<Prestamo> prestamos) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.enabled = enabled;
        this.imagen = imagen;
        this.reservas = reservas;
        this.prestamos = prestamos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Nullable
    public String getImagen() {
        return imagen;
    }

    public void setImagen(@Nullable String imagen) {
        this.imagen = imagen;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
}

