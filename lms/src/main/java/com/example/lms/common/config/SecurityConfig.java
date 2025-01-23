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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(SWAGGER_PATTERNS).permitAll()
						.requestMatchers(HttpMethod.POST, "/api/students/signup", "/api/instructors/signup").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/users/reissue").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/course/**").hasAuthority(Role.INSTRUCTOR.getAuthority())
//						.requestMatchers(HttpMethod.POST, "/api/course").hasAuthority(Role.INSTRUCTOR.getAuthority())
						.requestMatchers(HttpMethod.POST, "/api/course/**").hasAuthority(Role.INSTRUCTOR.getAuthority())
						.requestMatchers(HttpMethod.DELETE, "/api/course/**").hasAuthority(Role.INSTRUCTOR.getAuthority())
						.requestMatchers(HttpMethod.GET, "/api/course/**").permitAll()
								.requestMatchers(HttpMethod.GET, "/api/students/check-login-id/{loginId}", "/api/students/check-email/{email}").permitAll()
								.requestMatchers(HttpMethod.GET, "/api/students/managements").hasAuthority(Role.INSTRUCTOR.getAuthority())
						.requestMatchers("/api/students", "/api/students/**").hasAuthority(Role.STUDENT.getAuthority())
						.requestMatchers(HttpMethod.GET, "/api/instructors/check-login-id/{loginId}", "/api/instructors/check-email/{email}").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/instructors/{instructorId}").authenticated()
						.requestMatchers("/api/instructors", "/api/instructors/**").hasAuthority(Role.INSTRUCTOR.getAuthority())
						.requestMatchers("/api/teachings", "/api/teachings/**").hasAuthority(Role.INSTRUCTOR.getAuthority())
								.requestMatchers(HttpMethod.POST, "/api/registrations/courses/{courseId}").hasAuthority(Role.STUDENT.getAuthority())
								.requestMatchers(HttpMethod.PUT, "/api/registrations/{registrationId}/courses/{courseId}/cancel").authenticated()
								.requestMatchers(HttpMethod.PUT, "/api/registrations/{registrationId}/courses/{courseId}/approve").hasAuthority(Role.INSTRUCTOR.getAuthority())
								.requestMatchers(HttpMethod.GET, "/api/registrations/student/history").hasAuthority(Role.STUDENT.getAuthority())
								.requestMatchers(HttpMethod.GET, "/api/registrations/history/courses/{courseId}").hasAuthority(Role.INSTRUCTOR.getAuthority())
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

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(List.of(ALLOWED_METHODS));
		configuration.setAllowedHeaders(List.of(ALLOWED_HEADERS));
		configuration.setAllowCredentials(false);

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


	private final String[] ALLOWED_METHODS = {
		"HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
	};

	private final String[] ALLOWED_HEADERS = {"Authorization", "Cache-Control", "Content-Type"};
}