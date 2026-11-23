package com.controleportaria.controller;

import com.controleportaria.model.StatusEntrada;
import com.controleportaria.repository.EntradaRepository;
import com.controleportaria.repository.ObjetosRepository;
import com.controleportaria.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final EntradaRepository entradas;
    private final ObjetosRepository objetos;
    private final UsuarioRepository usuarios;

    public HomeController(EntradaRepository entradas, ObjetosRepository objetos, UsuarioRepository usuarios) {
        this.entradas = entradas;
        this.objetos = objetos;
        this.usuarios = usuarios;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("conteudo", "entrada/home");
        model.addAttribute("pageTitle", "Visão geral");
        model.addAttribute("totalEntradas", entradas.count());
        model.addAttribute("entradasPendentes", entradas.countByStatus(StatusEntrada.ABERTA));
        model.addAttribute("objetosPendentes", objetos.countByStatus(StatusEntrada.ABERTA));
        model.addAttribute("totalUsuarios", usuarios.count());
        return "layout";
    }
}
