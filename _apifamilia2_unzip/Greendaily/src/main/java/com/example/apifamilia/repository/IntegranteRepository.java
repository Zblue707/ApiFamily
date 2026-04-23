package com.example.apifamilia.repository;

import java.util.List;
import java.util.Optional;

import com.example.apifamilia.model.Integrante;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IntegranteRepository extends JpaRepository<Integrante, Long> {

	Optional<Integrante> findByCorreo(String correo);

	List<Integrante> findAllByIdUsuarioGoogleOrderByFechaRegistroDesc(Long idUsuarioGoogle);

	Optional<Integrante> findByIdIntegranteAndIdUsuarioGoogle(Long idIntegrante, Long idUsuarioGoogle);

	boolean existsByIdIntegranteAndIdUsuarioGoogle(Long idIntegrante, Long idUsuarioGoogle);
}
