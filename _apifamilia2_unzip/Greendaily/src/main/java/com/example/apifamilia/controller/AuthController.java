package com.example.apifamilia.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apifamilia.model.UsuarioGoogle;
import com.example.apifamilia.service.UsuarioSesionService;

@RestController
public class AuthController {

	private final UsuarioSesionService usuarioSesionService;
	private final String frontendUrl;

	public AuthController(UsuarioSesionService usuarioSesionService, @Value("${app.frontend-url}") String frontendUrl) {
		this.usuarioSesionService = usuarioSesionService;
		this.frontendUrl = frontendUrl;
	}

	@GetMapping("/")
	Map<String, Object> home(@AuthenticationPrincipal OAuth2User user) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("application", "apifamilia");
		response.put("authenticated", user != null);
		response.put("loginUrl", "/auth/login/google");
		response.put("sessionUrl", "/auth/status");
		response.put("integrantesUrl", "/integrantes");
		response.put("frontendUrl", frontendUrl + "/familia");
		return response;
	}

	@GetMapping("/auth/login/google")
	void loginGoogle(HttpServletResponse response) throws IOException {
		response.sendRedirect("/oauth2/authorization/google");
	}

	@GetMapping({ "/auth/status", "/mi-sesion", "/perfil" })
	Map<String, Object> status(@AuthenticationPrincipal OidcUser user, HttpSession session) {
		Map<String, Object> response = new LinkedHashMap<>();
		if (user == null) {
			response.put("authenticated", false);
			response.put("loginUrl", "/auth/login/google");
			return response;
		}

		UsuarioGoogle usuarioGoogle = usuarioSesionService.obtenerUsuarioActual(user);
		response.put("authenticated", true);
		response.put("sessionId", session.getId());
		response.put("id", usuarioGoogle.getId());
		response.put("googleId", usuarioGoogle.getGoogleId());
		response.put("nombre", usuarioGoogle.getNombre());
		response.put("email", usuarioGoogle.getEmail());
		response.put("fotoUrl", usuarioGoogle.getFotoUrl());
		response.put("fechaRegistro", usuarioGoogle.getFechaRegistro());
		response.put("ultimaSesion", usuarioGoogle.getUltimaSesion());
		response.put("integrantesUrl", "/integrantes");
		response.put("logoutUrl", "/auth/logout");
		return response;
	}

	@PostMapping("/auth/logout")
	Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response,
			@AuthenticationPrincipal OAuth2User user) {
		new SecurityContextLogoutHandler().logout(request, response, null);

		Map<String, Object> result = new LinkedHashMap<>();
		result.put("authenticated", false);
		result.put("message", "Sesion cerrada correctamente.");
		result.put("frontendUrl", frontendUrl + "/login");
		result.put("previousUser", user != null ? user.getAttribute("email") : null);
		return result;
	}
}
