package com.controleportaria.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.controleportaria.model.Entrada;
import com.controleportaria.model.StatusEntrada;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {
	
	long countByStatus(StatusEntrada status);

	Entrada findByPlaca(String placa);
	
	boolean existsByPlaca(String placa);
	
	@Query("select entra from Entrada entra where entra.placa like %?1%")
	List<Entrada> findEntradaByPlaca(String placa);
	
	@Query("select e from Entrada e where e.horaEntrada between :inicio and :fim")
	List<Entrada> findEntradaByDataHora(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
	
	@Query("select e from Entrada e where e.horaEntrada >= :inicio and e.horaEntrada < :fim")
	List<Entrada> findEntradaByData(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
	
	@Query("select entra from Entrada entra where entra.nomeMotorista like %?1%")
	List<Entrada> findEntradaByName(String nomeMotorista);
	
	@Query("from Entrada e where e.status = :status")
	List<Entrada> findEntradaByStatus(StatusEntrada status);
}
