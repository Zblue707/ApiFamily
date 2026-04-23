package com.example.apifamilia.config;

import java.util.List;

import com.example.apifamilia.service.GoogleUserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

	@Value("${app.cors.allowed-origins:http://localhost:4200}")
	private List<String> allowedOrigins;

	@Value("${app.frontend.login-url:http://localhost:4200/inicio-sesion}")
	private String frontendLoginUrl;

	@Value("${app.frontend.success-url:http://localhost:4200/familia}")
	private String frontendSuccessUrl;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, GoogleUserService googleUserService) throws Exception {
		return http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/login", "/error", "/oauth2/**").permitAll()
						.anyRequest().authenticated())
				.oauth2Login(oauth -> oauth
						.defaultSuccessUrl(frontendSuccessUrl, true)
						.userInfoEndpoint(userInfo -> userInfo.userService(googleUserService)))
				.logout(logout -> logout.logoutSuccessUrl(frontendLoginUrl))
				.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(allowedOrigins);
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
