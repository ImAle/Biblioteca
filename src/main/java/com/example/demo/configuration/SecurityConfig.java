package com.example.demo.configuration;

import java.util.Arrays;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	@Qualifier("jwtAuthFilter")
	private JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
	    	.csrf(csrf -> csrf
	    		.ignoringRequestMatchers("/api/**") // Deshabilita CSRF para APIs
	    	    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
	        .authorizeHttpRequests(auth -> auth
	        	// Web
	            .requestMatchers("/", "/register", "/css/**", "/js/**", "/images/**", "/public/**", "/index", "/libros" ,"/contacto", "/fotos/**", "/api/auth/**", "/api/reservas/**").permitAll()
	            .requestMatchers("/user/usuarios", "/user/usuarios/**", "/libros/createForm", "/libros/updateForm", "/libros/graficas", "/reservar/admin/cancelar", "/user/informes").hasRole("ADMIN")
	            .requestMatchers(HttpMethod.GET, "/prestamo/{id}").hasRole("ADMIN")
	            // API
	            .requestMatchers("/api/reservas/reservar", "/api/reservas/misReservas", "/api/reservas/historico").hasRole("USER")
				.requestMatchers("/api/reservas/consultar", "/api/reservas/borrar").hasRole("ADMIN")
	            .anyRequest().authenticated() // Todo lo demás requiere autenticación
	        ).exceptionHandling(exceptions -> exceptions
	                .accessDeniedPage("/index") // Redirigir a /index en caso de error 403
	        ).formLogin(form -> form
	                .loginPage("/login") // Ruta personalizada para la vista de login
	                .defaultSuccessUrl("/index", true) 
	                .failureUrl("/login?error=true") 
	                .permitAll() 
	            )
	        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
	        .userDetailsService(userDetailsService)
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
    
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5500", "http://127.0.0.1:5500"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // 1 hora de caché para preflight

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
	
}

