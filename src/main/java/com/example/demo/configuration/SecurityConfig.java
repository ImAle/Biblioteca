package com.example.demo.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.entity.Usuario;
import com.example.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private PasswordEncoder passwordEncoder;
	@Autowired
	@Qualifier("usuarioService")
	private UserService usuarioService;
	
	@Autowired
	public void passwordEncoderr(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF si no es necesario
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/css/**", "/js/**", "/images/**", "/public/**").permitAll()
	            .anyRequest().authenticated() // Todo lo demás requiere autenticación
	        )
	        .formLogin(form -> form
	            .defaultSuccessUrl("/index", true) // Redirigir tras login exitoso
	        )
	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/login?logout") // Redirigir tras logout
	            .permitAll()
	        );

	    return http.build();
	}

    
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user").password(passwordEncoder.encode("password")).roles("USER");
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService); // Usar tu UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder); // Usar codificación de contraseñas
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                   .authenticationProvider(authenticationProvider()) // Registrar el proveedor
                   .build();
    }
    
}

