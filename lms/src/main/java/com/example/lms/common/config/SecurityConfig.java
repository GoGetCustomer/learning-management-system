package com.example.lms.common.config;

import com.example.lms.common.auth.filter.*;
import com.example.lms.common.auth.handler.CustomAccessDeniedHandler;
import com.example.lms.common.auth.handler.CustomAuthenticationEntryPoint;
import com.example.lms.common.auth.jwt.TokenProvider;
import com.example.lms.common.auth.service.CustomInstructorDetailsService;
import com.example.lms.common.auth.service.CustomStudentDetailsService;
import com.example.lms.domain.user.enums.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final TokenProvider tokenProvider;
	private final ObjectMapper objectMapper;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
												   @Qualifier("studentAuthenticationManager") AuthenticationManager studentAuthenticationManager,
												   @Qualifier("instructorAuthenticationManager") AuthenticationManager instructorAuthenticationManager) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(SWAGGER_PATTERNS).permitAll()
						.requestMatchers(HttpMethod.POST, "/api/students/signup", "/api/instructors/signup").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/users/reissue").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/course").hasAuthority(Role.INSTRUCTOR.getAuthority())
						.requestMatchers(HttpMethod.PUT, "/api/course/**").hasAuthority(Role.INSTRUCTOR.getAuthority())
						.requestMatchers(HttpMethod.DELETE, "/api/course/**").hasAuthority(Role.INSTRUCTOR.getAuthority())
						.requestMatchers(HttpMethod.GET, "/api/course/**").permitAll()
						.requestMatchers("/api/students/**").hasAuthority(Role.STUDENT.getAuthority())
						.requestMatchers(HttpMethod.GET, "/api/instructors/{instructorId}").authenticated()
						.requestMatchers("/api/instructors", "/api/instructors/**").hasAuthority(Role.INSTRUCTOR.getAuthority())
						.anyRequest().authenticated()
				)
				.addFilterAt(new StudentLoginFilter(studentAuthenticationManager, tokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class)
				.addFilterAt(new InstructorLoginFilter(instructorAuthenticationManager, tokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(new JwtAuthenticationFilter(tokenProvider), CustomUsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new CustomLogoutFilter(tokenProvider, objectMapper), LogoutFilter.class)
				.exceptionHandling((exceptionHandling) -> exceptionHandling
						.authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
						.accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper)));
		return http.build();
	}

	@Bean
	@Primary
	public AuthenticationManager studentAuthenticationManager(
			CustomStudentDetailsService studentDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder
	) {
		DaoAuthenticationProvider studentAuthProvider = new DaoAuthenticationProvider();
		studentAuthProvider.setUserDetailsService(studentDetailsService);
		studentAuthProvider.setPasswordEncoder(bCryptPasswordEncoder);

		return new ProviderManager(List.of(studentAuthProvider));
	}

	@Bean
	public AuthenticationManager instructorAuthenticationManager(
			CustomInstructorDetailsService instructorDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder
	) {
		DaoAuthenticationProvider instructorAuthProvider = new DaoAuthenticationProvider();
		instructorAuthProvider.setUserDetailsService(instructorDetailsService);
		instructorAuthProvider.setPasswordEncoder(bCryptPasswordEncoder);

		return new ProviderManager(List.of(instructorAuthProvider));
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private static final String[] SWAGGER_PATTERNS = {
			"/swagger-ui/**",
			"/actuator/**",
			"/v3/api-docs/**",
	};
}