package com.controleportaria.controller;

import com.controleportaria.exception.NegocioException;
import com.controleportaria.model.Objetos;
import com.controleportaria.model.TipoEntradaObjeto;
import com.controleportaria.repository.ObjetosRepository;
import com.controleportaria.service.GestaoObjetoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/objetos")
public class ObjetoController {
    private final GestaoObjetoService service;
    private final ObjetosRepository repository;
    public ObjetoController(GestaoObjetoService service, ObjetosRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @ModelAttribute("tipoObjetos")
    public TipoEntradaObjeto[] tipos() { return TipoEntradaObjeto.values(); }
    @ModelAttribute("objs")
    public TipoEntradaObjeto[] tiposLista() { return TipoEntradaObjeto.values(); }

    private String formulario(Model model, boolean editando) {
        model.addAttribute("conteudo", "objetos/cadastrar");
        model.addAttribute("pageTitle", editando ? "Editar objeto" : "Registrar objeto");
        return "layout";
    }
    private String lista(Model model, java.util.List<Objetos> objetos) {
        model.addAttribute("objetos", objetos);
        model.addAttribute("conteudo", "objetos/listaObjetos");
        model.addAttribute("pageTitle", "Objetos");
        return "layout";
    }

    @GetMapping("/new")
    public String novo(Model model) {
        model.addAttribute("objetoAtual", new Objetos());
        return formulario(model, false);
    }
    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("objetoAtual") Objetos objeto,
            BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) return formulario(model, objeto.getId() != null);
        try {
            service.cadastrar(objeto);
        } catch (NegocioException | IllegalArgumentException ex) {
            result.reject("objeto.invalido", ex.getMessage());
            return formulario(model, objeto.getId() != null);
        }
        redirect.addFlashAttribute("message", "Movimentação salva com sucesso.");
        return "redirect:/objetos/listar";
    }
    @GetMapping("/listar")
    public String listar(Model model) { return lista(model, service.listar()); }
    @GetMapping("/tipo")
    public String tipo(@RequestParam(value = "tipoEntradaObjeto", required = false) TipoEntradaObjeto tipo,
            Model model) { return lista(model, tipo == null ? service.listar() : service.listarPorTipoEntrada(tipo)); }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Objetos objeto = service.buscar(id);
        model.addAttribute("objetoAtual", objeto);
        return formulario(model, true);
    }
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        repository.findById(id).ifPresent(repository::delete);
        redirect.addFlashAttribute("message", "Objeto removido com sucesso.");
        return "redirect:/objetos/listar";
    }
    @PostMapping("/finalizar/{id}")
    public String finalizar(@PathVariable Long id, RedirectAttributes redirect) {
        service.finalizar(id);
        redirect.addFlashAttribute("message", "Movimentação finalizada com sucesso.");
        return "redirect:/objetos/listar";
    }
}
