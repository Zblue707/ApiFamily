package com.example.apifamilia.config;

import com.example.apifamilia.service.GoogleUserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, GoogleUserService googleUserService,
			@Value("${app.frontend-url}") String frontendUrl) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/auth/login/google", "/auth/status", "/login", "/error", "/oauth2/**")
						.permitAll()
						.anyRequest().authenticated())
				.oauth2Login(oauth -> oauth
						.defaultSuccessUrl(frontendUrl + "/familia", true)
						.failureUrl(frontendUrl + "/login?error=google")
						.userInfoEndpoint(userInfo -> userInfo.userService(googleUserService)))
				.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource(@Value("${app.frontend-url}") String frontendUrl) {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of(frontendUrl));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		configuration.setExposedHeaders(List.of("Set-Cookie"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
