package com.example.apifamilia.repository;

import java.util.Optional;

import com.example.apifamilia.model.UsuarioGoogle;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioGoogleRepository extends JpaRepository<UsuarioGoogle, Long> {

	Optional<UsuarioGoogle> findByGoogleId(String googleId);

	Optional<UsuarioGoogle> findByEmail(String email);
}
