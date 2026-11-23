package com.controleportaria.controller;

import com.controleportaria.model.Entrada;
import com.controleportaria.model.Objetos;
import com.controleportaria.model.Usuario;
import com.controleportaria.repository.EntradaRepository;
import com.controleportaria.repository.ObjetosRepository;
import com.controleportaria.repository.UsuarioRepository;
import com.controleportaria.service.GestaoEntradaService;
import com.controleportaria.service.GestaoObjetoService;
import com.controleportaria.service.GestaoUsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidacaoControllerTest {
    @Test void erroDeEntradaVoltaAoFormularioSemSalvar() {
        GestaoEntradaService service = mock(GestaoEntradaService.class);
        Entrada entrada = new Entrada();
        BindingResult errors = new BeanPropertyBindingResult(entrada, "entradaAtual");
        errors.rejectValue("placa", "invalida", "Placa inválida");
        ExtendedModelMap model = new ExtendedModelMap();
        String view = new EntradaController(mock(EntradaRepository.class), service)
            .registrar(entrada, errors, model, mock(RedirectAttributes.class));
        assertEquals("layout", view);
        assertEquals("entrada/registro", model.get("conteudo"));
        verifyNoInteractions(service);
    }

    @Test void erroDeObjetoVoltaAoFormularioSemSalvar() {
        GestaoObjetoService service = mock(GestaoObjetoService.class);
        Objetos objeto = new Objetos();
        BindingResult errors = new BeanPropertyBindingResult(objeto, "objetoAtual");
        errors.rejectValue("comentario", "obrigatorio", "Descreva o objeto");
        ExtendedModelMap model = new ExtendedModelMap();
        String view = new ObjetoController(service, mock(ObjetosRepository.class))
            .cadastrar(objeto, errors, model, mock(RedirectAttributes.class));
        assertEquals("layout", view);
        assertEquals("objetos/cadastrar", model.get("conteudo"));
        verifyNoInteractions(service);
    }

    @Test void erroDeUsuarioVoltaAoFormularioSemSalvar() {
        GestaoUsuarioService service = mock(GestaoUsuarioService.class);
        Usuario usuario = new Usuario();
        BindingResult errors = new BeanPropertyBindingResult(usuario, "usuarioAtual");
        errors.rejectValue("matricula", "invalida", "Matrícula inválida");
        ExtendedModelMap model = new ExtendedModelMap();
        String view = new UsuarioController(service, mock(UsuarioRepository.class))
            .cadastrar(usuario, errors, model, mock(RedirectAttributes.class));
        assertEquals("layout", view);
        assertEquals("usuario/cadastroUsuario", model.get("conteudo"));
        verifyNoInteractions(service);
    }
}
