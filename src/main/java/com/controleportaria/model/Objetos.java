package com.controleportaria.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Objetos {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime horaDeEntrada;

	@Size(max = 30)
	private String porteiroDeEntrada;

	@NotBlank(message = "Informe o nome do responsável.")
	@Size(max = 30, message = "O nome deve ter até 30 caracteres.")
	private String funcionario;

	@NotBlank(message = "Descreva o objeto ou movimentação.")
	@Size(max = 500, message = "A descrição deve ter até 500 caracteres.")
	private String comentario;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime horaDeSaida;

	@Enumerated(EnumType.STRING)
	private StatusEntrada status;

	@NotNull(message = "Selecione o tipo de movimentação.")
	@Enumerated(EnumType.STRING)
	private TipoEntradaObjeto tipoEntradaObjeto;

	@Size(max = 30)
	private String porteiroDeSaida;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getHoraDeEntrada() {
		return horaDeEntrada;
	}

	public void setHoraDeEntrada(LocalDateTime horaDeEntrada) {
		this.horaDeEntrada = horaDeEntrada;
	}

	public String getPorteiroDeEntrada() {
		return porteiroDeEntrada;
	}

	public void setPorteiroDeEntrada(String porteiroDeEntrada) {
		this.porteiroDeEntrada = porteiroDeEntrada;
	}

	public String getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public LocalDateTime getHoraDeSaida() {
		return horaDeSaida;
	}

	public void setHoraDeSaida(LocalDateTime horaDeSaida) {
		this.horaDeSaida = horaDeSaida;
	}

	public StatusEntrada getStatus() {
		return status;
	}

	public void setStatus(StatusEntrada status) {
		this.status = status;
	}

	public TipoEntradaObjeto getTipoEntradaObjeto() {
		return tipoEntradaObjeto;
	}

	public void setTipoEntradaObjeto(TipoEntradaObjeto tipoEntradaObjeto) {
		this.tipoEntradaObjeto = tipoEntradaObjeto;
	}

	public String getPorteiroDeSaida() {
		return porteiroDeSaida;
	}

	public void setPorteiroDeSaida(String porteiroDeSaida) {
		this.porteiroDeSaida = porteiroDeSaida;
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
		Objetos other = (Objetos) obj;
		return Objects.equals(id, other.id);
	}

}
