package com.example.demo.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.csrf(Customizer.withDefaults())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/", "/register", "/css/**", "/js/**", "/images/**", "/public/**", "/index", "/libros" ,"/contacto", "/fotos/**").permitAll()
	            .requestMatchers("/user/usuarios", "/user/usuarios/**", "/libros/createForm", "/libros/updateForm", "/gestionReservas").hasRole("ADMIN")
	            .anyRequest().authenticated() // Todo lo demás requiere autenticación
	        ).exceptionHandling(exceptions -> exceptions
	                .accessDeniedPage("/index") // Redirigir a /index en caso de error 403
	        ).formLogin(form -> form
	                .loginPage("/login") // Ruta personalizada para la vista de login
	                .defaultSuccessUrl("/index", true) 
	                .failureUrl("/login?error=true") 
	                .permitAll() 
	            )
	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/login?logout=true") // Redirigir tras logout
	            .permitAll()
	        );

	    return http.build();
	}

    
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
}

