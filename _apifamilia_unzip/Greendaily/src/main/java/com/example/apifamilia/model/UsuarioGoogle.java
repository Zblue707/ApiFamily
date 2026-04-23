package com.example.apifamilia.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios_google")
public class UsuarioGoogle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "google_id", nullable = false, unique = true, length = 100)
	private String googleId;

	@Column(nullable = false, length = 150)
	private String nombre;

	@Column(nullable = false, unique = true, length = 150)
	private String email;

	@Column(name = "foto_url", length = 255)
	private String fotoUrl;

	@Column(name = "fecha_registro", nullable = false)
	private LocalDateTime fechaRegistro;

	@Column(name = "ultima_sesion", nullable = false)
	private LocalDateTime ultimaSesion;

	@PrePersist
	void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		fechaRegistro = now;
		ultimaSesion = now;
	}

	@PreUpdate
	void onUpdate() {
		ultimaSesion = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFotoUrl() {
		return fotoUrl;
	}

	public void setFotoUrl(String fotoUrl) {
		this.fotoUrl = fotoUrl;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public LocalDateTime getUltimaSesion() {
		return ultimaSesion;
	}

	public void setUltimaSesion(LocalDateTime ultimaSesion) {
		this.ultimaSesion = ultimaSesion;
	}
}
