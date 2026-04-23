package com.example.apifamilia.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.apifamilia.model.UsuarioGoogle;
import com.example.apifamilia.repository.UsuarioGoogleRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	private final UsuarioGoogleRepository usuarioGoogleRepository;

	public AuthController(UsuarioGoogleRepository usuarioGoogleRepository) {
		this.usuarioGoogleRepository = usuarioGoogleRepository;
	}

	@GetMapping("/")
	Map<String, Object> home(@AuthenticationPrincipal OAuth2User user) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("application", "apifamilia");

		if (user == null) {
			response.put("authenticated", false);
			response.put("loginUrl", "/oauth2/authorization/google");
			response.put("message", "Inicia sesion con Google para continuar.");
			return response;
		}

		response.put("authenticated", true);
		response.put("name", user.getAttribute("name"));
		response.put("email", user.getAttribute("email"));
		response.put("picture", user.getAttribute("picture"));
		response.put("integrantesUrl", "/integrantes");
		return response;
	}

	@GetMapping("/perfil")
	Map<String, Object> profile(@AuthenticationPrincipal OAuth2User user) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("name", user.getAttribute("name"));
		response.put("email", user.getAttribute("email"));
		response.put("picture", user.getAttribute("picture"));
		response.put("attributes", user.getAttributes());
		return response;
	}

	@GetMapping("/mi-sesion")
	Map<String, Object> session(@AuthenticationPrincipal OAuth2User user) {
		UsuarioGoogle usuarioGoogle = usuarioGoogleRepository
				.findByGoogleId(user.getAttribute("sub"))
				.orElseThrow();

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("id", usuarioGoogle.getId());
		response.put("googleId", usuarioGoogle.getGoogleId());
		response.put("nombre", usuarioGoogle.getNombre());
		response.put("email", usuarioGoogle.getEmail());
		response.put("fotoUrl", usuarioGoogle.getFotoUrl());
		response.put("ultimaSesion", usuarioGoogle.getUltimaSesion());
		return response;
	}
}
