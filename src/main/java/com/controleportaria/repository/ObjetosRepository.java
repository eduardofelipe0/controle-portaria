package com.controleportaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.controleportaria.model.Objetos;
import com.controleportaria.model.TipoEntradaObjeto;

@Repository
public interface ObjetosRepository extends JpaRepository<Objetos, Long> {
	long countByStatus(com.controleportaria.model.StatusEntrada status);
	
	@Query("from Objetos ob where ob.tipoEntradaObjeto = :tipoEntradaObjeto")
	List<Objetos> findObjetosByTipoEntradaObjeto(TipoEntradaObjeto tipoEntradaObjeto);
}
