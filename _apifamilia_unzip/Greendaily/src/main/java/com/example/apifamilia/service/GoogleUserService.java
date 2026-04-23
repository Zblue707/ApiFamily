package com.example.apifamilia.service;

import java.time.LocalDateTime;

import com.example.apifamilia.model.UsuarioGoogle;
import com.example.apifamilia.repository.UsuarioGoogleRepository;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GoogleUserService extends DefaultOAuth2UserService {

	private final UsuarioGoogleRepository usuarioGoogleRepository;

	public GoogleUserService(UsuarioGoogleRepository usuarioGoogleRepository) {
		this.usuarioGoogleRepository = usuarioGoogleRepository;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user = super.loadUser(userRequest);

		String googleId = user.getAttribute("sub");
		String email = user.getAttribute("email");
		String nombre = user.getAttribute("name");
		String fotoUrl = user.getAttribute("picture");

		UsuarioGoogle usuarioGoogle = usuarioGoogleRepository.findByGoogleId(googleId)
				.or(() -> usuarioGoogleRepository.findByEmail(email))
				.orElseGet(UsuarioGoogle::new);

		usuarioGoogle.setGoogleId(googleId);
		usuarioGoogle.setEmail(email);
		usuarioGoogle.setNombre(nombre);
		usuarioGoogle.setFotoUrl(fotoUrl);
		usuarioGoogle.setUltimaSesion(LocalDateTime.now());

		usuarioGoogleRepository.save(usuarioGoogle);
		return user;
	}
}
