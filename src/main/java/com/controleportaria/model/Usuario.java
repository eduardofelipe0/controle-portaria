package com.controleportaria.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "Informe o nome completo.")
	@Size(max = 40, message = "O nome deve ter até 40 caracteres.")
	private String nome;

	@NotBlank(message = "Informe a matrícula.")
	@Pattern(regexp = "[0-9]{1,10}", message = "A matrícula deve conter de 1 a 10 dígitos.")
	private String matricula;

	@NotBlank(message = "Informe o nome de usuário.")
	@Size(max = 30, message = "O usuário deve ter até 30 caracteres.")
	@Pattern(regexp = "[a-zA-Z0-9._-]+", message = "Use letras, números, ponto, hífen ou sublinhado.")
	private String nomeUsuario;

	@NotNull(message = "Selecione uma permissão.")
	@Enumerated(EnumType.STRING)
	private Role role;

	@Size(max = 100, message = "A senha é muito longa.")
	private String senha;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}
}
