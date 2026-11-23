package com.controleportaria.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Entrada implements Serializable {

	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime horaEntrada;

	@NotBlank(message = "Informe o veículo.")
	@Size(max = 30, message = "O veículo deve ter até 30 caracteres.")
	private String veiculo;

	@NotBlank(message = "Informe a placa.")
	@Pattern(regexp = "[A-Za-z]{3}[0-9][A-Za-z0-9][0-9]{2}", message = "Informe uma placa válida (ABC1234 ou ABC1D23).")
	private String placa;

	@NotBlank(message = "Informe o nome do motorista.")
	@Size(max = 30, message = "O nome do motorista deve ter até 30 caracteres.")
	private String nomeMotorista;

	@Pattern(regexp = "[0-9]{0,6}", message = "O apartamento deve conter apenas números, com até 6 dígitos.")
	private String numeroApt;

	@NotNull(message = "Selecione o tipo de entrada.")
	@Enumerated(EnumType.STRING)
	private TipoEntrada tipo;

	@Enumerated(EnumType.STRING)
	private StatusEntrada status;

	@Size(max = 50)
	private String usuarioDaEntrada;

	@Size(max = 50)
	private String usuarioDaSaida;

	private LocalDateTime horaSaida;

	private LocalDateTime intervaloDatas;

	// Getters And Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(LocalDateTime horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public String getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getNomeMotorista() {
		return nomeMotorista;
	}

	public void setNomeMotorista(String nomeMotorista) {
		this.nomeMotorista = nomeMotorista;
	}

	public String getNumeroApt() {
		return numeroApt;
	}

	public void setNumeroApt(String numeroApt) {
		this.numeroApt = numeroApt;
	}

	public TipoEntrada getTipo() {
		return tipo;
	}

	public void setTipo(TipoEntrada tipo) {
		this.tipo = tipo;
	}

	public StatusEntrada getStatus() {
		return status;
	}

	public void setStatus(StatusEntrada status) {
		this.status = status;
	}

	public LocalDateTime getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(LocalDateTime horaSaida) {
		this.horaSaida = horaSaida;
	}

	public String getUsuarioDaEntrada() {
		return usuarioDaEntrada;
	}

	public void setUsuarioDaEntrada(String usuarioDaEntrada) {
		this.usuarioDaEntrada = usuarioDaEntrada;
	}

	public String getUsuarioDaSaida() {
		return usuarioDaSaida;
	}

	public void setUsuarioDaSaida(String usuarioDaSaida) {
		this.usuarioDaSaida = usuarioDaSaida;
	}

	public LocalDateTime getIntervaloDatas() {
		return intervaloDatas;
	}

	public void setIntervaloDatas(LocalDateTime intervaloDatas) {
		this.intervaloDatas = intervaloDatas;
	}

	@Override
	public int hashCode() {
		return Objects.hash(placa);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entrada other = (Entrada) obj;
		return Objects.equals(placa, other.placa);
	}

	public String converterData(LocalDateTime data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String dataFormatada = formatter.format(data);
		return dataFormatada;
	}

	public boolean checaStatus() {
		if (getStatus() == StatusEntrada.FINALIZADA) {
			return false;
		} else {
			return true;
		}
	}
}
