package com.hospital.salud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SegurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable()) // Desactivar CSRF para APIs REST; habilitarlo para formularios si es necesario
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(
								"/registro**",          // Permitir acceso sin autenticación
								"/js/**", "/css/**", "/img/**" // Recursos estáticos
						).permitAll()
						.requestMatchers(HttpMethod.OPTIONS).permitAll() // Permitir pre-flight requests de CORS
						.requestMatchers("/salud/auth/**").permitAll()      // Permitir acceso público a autenticación
						.anyRequest().authenticated() // Requerir autenticación para el resto de las rutas
				)
				.sessionManagement(sessionManager -> sessionManager
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session management para JWT
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}
