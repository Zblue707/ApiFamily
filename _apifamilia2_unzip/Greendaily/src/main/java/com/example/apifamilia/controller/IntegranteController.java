package com.example.apifamilia.controller;

import java.util.List;
import java.util.Map;

import com.example.apifamilia.model.Integrante;
import com.example.apifamilia.service.IntegranteService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integrantes")
public class IntegranteController {

	private final IntegranteService integranteService;

	public IntegranteController(IntegranteService integranteService) {
		this.integranteService = integranteService;
	}

	@GetMapping
	List<Integrante> listar(@AuthenticationPrincipal OAuth2User user) {
		return integranteService.listar(user);
	}

	@GetMapping("/{id}")
	Integrante obtenerPorId(@PathVariable Long id, @AuthenticationPrincipal OAuth2User user) {
		return integranteService.obtenerPorId(id, user);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Integrante crear(@RequestBody Integrante integrante, @AuthenticationPrincipal OAuth2User user) {
		return integranteService.guardar(integrante, user);
	}

	@PutMapping("/{id}")
	Integrante actualizar(@PathVariable Long id, @RequestBody Integrante integrante,
			@AuthenticationPrincipal OAuth2User user) {
		integrante.setIdIntegrante(id);
		return integranteService.guardar(integrante, user);
	}

	@DeleteMapping("/{id}")
	Map<String, String> eliminar(@PathVariable Long id, @AuthenticationPrincipal OAuth2User user) {
		integranteService.eliminar(id, user);
		return Map.of("message", "Integrante eliminado correctamente");
	}
}
