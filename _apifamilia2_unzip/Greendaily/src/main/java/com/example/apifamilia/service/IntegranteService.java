package com.example.apifamilia.service;

import java.util.List;

import com.example.apifamilia.model.Integrante;
import com.example.apifamilia.model.UsuarioGoogle;
import com.example.apifamilia.repository.IntegranteRepository;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class IntegranteService {

	private final IntegranteRepository integranteRepository;
	private final UsuarioSesionService usuarioSesionService;

	public IntegranteService(IntegranteRepository integranteRepository, UsuarioSesionService usuarioSesionService) {
		this.integranteRepository = integranteRepository;
		this.usuarioSesionService = usuarioSesionService;
	}

	public List<Integrante> listar(OAuth2User user) {
		UsuarioGoogle usuarioGoogle = usuarioSesionService.obtenerUsuarioActual(user);
		return integranteRepository.findAllByIdUsuarioGoogleOrderByFechaRegistroDesc(usuarioGoogle.getId());
	}

	public Integrante obtenerPorId(Long id, OAuth2User user) {
		UsuarioGoogle usuarioGoogle = usuarioSesionService.obtenerUsuarioActual(user);
		return integranteRepository.findByIdIntegranteAndIdUsuarioGoogle(id, usuarioGoogle.getId())
				.orElseThrow(() -> new IllegalArgumentException("No existe un integrante con id " + id + " para esta cuenta"));
	}

	public Integrante guardar(Integrante integrante, OAuth2User user) {
		UsuarioGoogle usuarioGoogle = usuarioSesionService.obtenerUsuarioActual(user);

		if (integrante.getIdIntegrante() != null
				&& !integranteRepository.existsByIdIntegranteAndIdUsuarioGoogle(integrante.getIdIntegrante(),
						usuarioGoogle.getId())) {
			throw new IllegalArgumentException("No puedes modificar un integrante de otra cuenta.");
		}

		integrante.setIdUsuarioGoogle(usuarioGoogle.getId());

		if (integrante.getCorreo() != null && !integrante.getCorreo().isBlank()) {
			integranteRepository.findByCorreo(integrante.getCorreo())
					.filter(existente -> !existente.getIdIntegrante().equals(integrante.getIdIntegrante()))
					.ifPresent(existente -> {
						throw new IllegalArgumentException("Ya existe un integrante con el correo " + integrante.getCorreo());
					});
		}

		return integranteRepository.save(integrante);
	}

	public void eliminar(Long id, OAuth2User user) {
		Integrante integrante = obtenerPorId(id, user);
		integranteRepository.delete(integrante);
	}
}
