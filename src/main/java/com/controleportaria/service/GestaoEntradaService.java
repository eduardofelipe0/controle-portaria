package com.controleportaria.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.controleportaria.exception.NegocioException;
import com.controleportaria.model.Entrada;
import com.controleportaria.model.StatusEntrada;
import com.controleportaria.repository.EntradaRepository;
import com.controleportaria.user.ControllerUsuario;

@Service
@Transactional
public class GestaoEntradaService {

	@Autowired
	private EntradaRepository entradaRepository;

	public void finalizar(Long id) {
		Entrada entrada = buscar(id);
		if (entrada.getStatus().equals(StatusEntrada.FINALIZADA)) {
			throw new NegocioException("Entrada já finalizada!");
		} else {
			SecurityContext sc = SecurityContextHolder.getContext();
			String username = sc.getAuthentication().getName();
			entrada.setUsuarioDaSaida(username);
			entrada.setHoraSaida(LocalDateTime.now());
			entrada.setStatus(StatusEntrada.FINALIZADA);
			entradaRepository.save(entrada);
		}
	}

	public Entrada buscar(Long id) {
		return entradaRepository.findById(id).get();
	}

	public List<Entrada> buscarPlaca(String placa) {
		return (List<Entrada>) entradaRepository.findEntradaByPlaca(placa);
	}

	public List<Entrada> buscarData(LocalDate horaEntrada) {
		return entradaRepository.findEntradaByData(horaEntrada.atStartOfDay(), horaEntrada.plusDays(1).atStartOfDay());
	}

	public List<Entrada> intervaloDatas(LocalDateTime horaEntrada, LocalDateTime horaEntrada1) {
		return (List<Entrada>) entradaRepository.findEntradaByDataHora(horaEntrada, horaEntrada1);
	}

	public List<Entrada> buscarDataIntervalo(LocalDateTime intervaloDatas) {
		return (List<Entrada>) entradaRepository.findEntradaByDataHora(intervaloDatas, intervaloDatas);
	}

	public List<Entrada> buscarPorNome(String nomeMotorista) {
		return (List<Entrada>) entradaRepository.findEntradaByName(nomeMotorista);
	}

	public String usuarioLogado() {
		ControllerUsuario uc = new ControllerUsuario();
		String nome = uc.getUsuario().getNomeUsuario();
		return nome;
	}

	public void criar(Entrada dados) {
        SecurityContext sc = SecurityContextHolder.getContext();
        String username = sc.getAuthentication().getName();
        if (dados.getId() != null && sc.getAuthentication().getAuthorities().stream()
                .noneMatch(a -> "ADMIN".equals(a.getAuthority()))) {
            throw new AccessDeniedException("Apenas administradores podem editar entradas.");
        }
        caracteres(dados);
        if (dados.getId() == null) {
            dados.setStatus(StatusEntrada.ABERTA);
            dados.setUsuarioDaEntrada(username);
            dados.setHoraEntrada(LocalDateTime.now());
            entradaRepository.save(dados);
        } else {
            Entrada atual = buscar(dados.getId());
            if (atual.getStatus() == StatusEntrada.FINALIZADA)
                throw new NegocioException("Uma entrada finalizada não pode ser editada.");
            atual.setVeiculo(dados.getVeiculo());
            atual.setPlaca(dados.getPlaca());
            atual.setNomeMotorista(dados.getNomeMotorista());
            atual.setNumeroApt(dados.getNumeroApt());
            atual.setTipo(dados.getTipo());
            entradaRepository.save(atual);
        }
    }

	public List<Entrada> listar() {
		return (List<Entrada>) entradaRepository.findAll();
	}

	public List<Entrada> listAll() {
		return entradaRepository.findAll(Sort.by("placa").ascending());
	}

	public List<Entrada> listarPorStatus(StatusEntrada status) {
		return (List<Entrada>) entradaRepository.findEntradaByStatus(status);
	}

	public void caracteres(Entrada entrada) {
        entrada.setPlaca(entrada.getPlaca().trim().toUpperCase(java.util.Locale.ROOT));
        entrada.setVeiculo(capitalizar(entrada.getVeiculo()));
        entrada.setNomeMotorista(capitalizar(entrada.getNomeMotorista()));
    }

    private String capitalizar(String valor) {
        String texto = valor == null ? "" : valor.trim().replaceAll("\\s+", " ");
        if (texto.isEmpty()) return texto;
        StringBuilder out = new StringBuilder();
        boolean inicio = true;
        for (char letra : texto.toLowerCase(java.util.Locale.forLanguageTag("pt-BR")).toCharArray()) {
            out.append(inicio ? Character.toUpperCase(letra) : letra);
            inicio = Character.isWhitespace(letra);
        }
        return out.toString();
    }
}
