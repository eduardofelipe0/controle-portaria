package com.controleportaria.service;

import com.controleportaria.exception.NegocioException;
import com.controleportaria.model.Entrada;
import com.controleportaria.model.StatusEntrada;
import com.controleportaria.repository.EntradaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestaoEntradaServiceTest {
    @Mock EntradaRepository repository;

    @AfterEach void limparContexto() { SecurityContextHolder.clearContext(); }

    @Test
    void finalizarRegistraResponsavelEHorario() {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("porteiro", "senha"));
        Entrada entrada = new Entrada();
        entrada.setStatus(StatusEntrada.ABERTA);
        when(repository.findById(1L)).thenReturn(Optional.of(entrada));
        GestaoEntradaService service = new GestaoEntradaService();
        org.springframework.test.util.ReflectionTestUtils.setField(service, "entradaRepository", repository);
        service.finalizar(1L);
        assertEquals(StatusEntrada.FINALIZADA, entrada.getStatus());
        assertEquals("porteiro", entrada.getUsuarioDaSaida());
        assertNotNull(entrada.getHoraSaida());
        verify(repository).save(entrada);
    }

    @Test
    void naoFinalizaEntradaJaConcluida() {
        Entrada entrada = new Entrada();
        entrada.setStatus(StatusEntrada.FINALIZADA);
        when(repository.findById(1L)).thenReturn(Optional.of(entrada));
        GestaoEntradaService service = new GestaoEntradaService();
        org.springframework.test.util.ReflectionTestUtils.setField(service, "entradaRepository", repository);
        assertThrows(NegocioException.class, () -> service.finalizar(1L));
        verify(repository, never()).save(any());
    }
}
