package com.example.apifamilia.service;

import com.example.apifamilia.model.UsuarioGoogle;
import com.example.apifamilia.repository.UsuarioGoogleRepository;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UsuarioSesionService {

	private final UsuarioGoogleRepository usuarioGoogleRepository;

	public UsuarioSesionService(UsuarioGoogleRepository usuarioGoogleRepository) {
		this.usuarioGoogleRepository = usuarioGoogleRepository;
	}

	public UsuarioGoogle obtenerUsuarioActual(OAuth2User user) {
		if (user == null) {
			throw new IllegalArgumentException("Debes iniciar sesion para continuar.");
		}

		String googleId = user.getAttribute("sub");
		String email = user.getAttribute("email");

		return usuarioGoogleRepository.findByGoogleId(googleId)
				.or(() -> usuarioGoogleRepository.findByEmail(email))
				.orElseThrow(() -> new IllegalArgumentException("No se encontro la cuenta asociada a la sesion actual."));
	}
}
