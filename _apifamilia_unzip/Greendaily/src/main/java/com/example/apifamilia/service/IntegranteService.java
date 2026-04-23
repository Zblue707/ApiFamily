package com.example.apifamilia.service;

import java.util.List;

import com.example.apifamilia.model.Integrante;
import com.example.apifamilia.repository.IntegranteRepository;

import org.springframework.stereotype.Service;

@Service
public class IntegranteService {

	private final IntegranteRepository integranteRepository;

	public IntegranteService(IntegranteRepository integranteRepository) {
		this.integranteRepository = integranteRepository;
	}

	public List<Integrante> listar() {
		return integranteRepository.findAll();
	}

	public Integrante obtenerPorId(Long id) {
		return integranteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("No existe un integrante con id " + id));
	}

	public Integrante guardar(Integrante integrante) {
		integranteRepository.findByCorreo(integrante.getCorreo())
				.filter(existente -> !existente.getIdIntegrante().equals(integrante.getIdIntegrante()))
				.ifPresent(existente -> {
					throw new IllegalArgumentException("Ya existe un integrante con el correo " + integrante.getCorreo());
				});

		return integranteRepository.save(integrante);
	}

	public void eliminar(Long id) {
		if (!integranteRepository.existsById(id)) {
			throw new IllegalArgumentException("No existe un integrante con id " + id);
		}
		integranteRepository.deleteById(id);
	}
}
