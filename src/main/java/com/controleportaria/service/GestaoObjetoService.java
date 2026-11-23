package com.controleportaria.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.controleportaria.exception.NegocioException;
import com.controleportaria.model.Objetos;
import com.controleportaria.model.StatusEntrada;
import com.controleportaria.model.TipoEntradaObjeto;
import com.controleportaria.repository.ObjetosRepository;

@Service
public class GestaoObjetoService {

	@Autowired
	private ObjetosRepository objetosRepository;

	public Objetos cadastrar(Objetos dados) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (dados.getId() != null) {
            boolean admin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> "ADMIN".equals(a.getAuthority()));
            if (!admin) throw new org.springframework.security.access.AccessDeniedException(
                "Apenas administradores podem editar movimentações.");
            Objetos atual = buscar(dados.getId());
            atual.setFuncionario(dados.getFuncionario().trim());
            atual.setComentario(dados.getComentario().trim());
            atual.setTipoEntradaObjeto(dados.getTipoEntradaObjeto());
            return objetosRepository.save(atual);
        }
        dados.setFuncionario(dados.getFuncionario().trim());
        dados.setComentario(dados.getComentario().trim());
        dados.setPorteiroDeEntrada(username);
        dados.setHoraDeEntrada(LocalDateTime.now());
        if (dados.getTipoEntradaObjeto() == TipoEntradaObjeto.UTILIZADOS_NO_HOTEL) {
            dados.setPorteiroDeSaida(username);
            dados.setStatus(StatusEntrada.FINALIZADA);
            dados.setHoraDeSaida(LocalDateTime.now());
        } else {
            dados.setStatus(StatusEntrada.ABERTA);
        }
        return objetosRepository.save(dados);
    }

	public Objetos buscar(Long id) {
		return objetosRepository.findById(id).get();
	}
	public List<Objetos> listarPorTipoEntrada(TipoEntradaObjeto tipo) {
		return (List<Objetos>) objetosRepository.findObjetosByTipoEntradaObjeto(tipo);
	}
	public List<Objetos> listar() {
		return (List<Objetos>) objetosRepository.findAll();
	}

	public void finalizar(Long id) {
		Objetos objeto = buscar(id);
		if (objeto.getStatus().equals(StatusEntrada.FINALIZADA)) {
			throw new NegocioException("Entrada já finalizada!");
		} else {
			SecurityContext sc = SecurityContextHolder.getContext();
			String username = sc.getAuthentication().getName();
			objeto.setPorteiroDeSaida(username);
			objeto.setHoraDeSaida(LocalDateTime.now());
			objeto.setStatus(StatusEntrada.FINALIZADA);
			objetosRepository.save(objeto);
		}
	}
}
