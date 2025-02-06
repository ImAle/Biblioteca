package com.example.demo.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	@Qualifier("jwtService")
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	// Método que envía la respuesta en caso de excepción, evitando asi tener que manejar los errores en los controladores
	// y por lo tanto, centralizando las excepciones realizadas con los tokens
	private void sendErrorResponse(HttpServletResponse response, 
			HttpStatus status, 
			String message) throws IOException {
		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		String jsonResponse = String.format(
				"{ \"error\": \"%s\", \"message\": \"%s\" }", 
				status.getReasonPhrase(), 
				message
				);

		response.getWriter().write(jsonResponse);
		response.getWriter().flush();
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();

		// Excluir todas las rutas que no son /api/... y las rutas de autenticación de la misma api
		if (!path.startsWith("/api/") || path.startsWith("/api/auth/")) {
			return true;
		}

		return false; // Aplicar el filtro al resto de rutas
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
			throws ServletException, IOException {

		try {
			final String authHeader = request.getHeader("Authorization");

			if (authHeader == null) {
				throw new ServletException("Falta el token de autorización");
			}

			if (!authHeader.startsWith("Bearer ")) {
				throw new ServletException("Formato de token inválido");
			}

			String jwt = authHeader.substring(7);

			if (jwt.isBlank()) {
				throw new MalformedJwtException("Token vacío");
			}

			String userEmail = jwtService.extractUsername(jwt);

			if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

				if (jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException eje) {
			sendErrorResponse(response, 
					HttpStatus.UNAUTHORIZED, 
					"Token JWT expirado: " + eje.getMessage()
					);
		}catch (MalformedJwtException | SignatureException mjese) {
			sendErrorResponse(response, 
					HttpStatus.UNAUTHORIZED, 
					"Token JWT inválido: " + mjese.getMessage()
					);
		} catch (ServletException se) {
			sendErrorResponse(response, 
					HttpStatus.BAD_REQUEST, 
					"Error de autenticación: " + se.getMessage()
					);
		}
	}

}
