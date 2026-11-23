package com.controleportaria.controller;

import com.controleportaria.exception.NegocioException;
import com.controleportaria.model.*;
import com.controleportaria.service.GestaoEntradaService;
import com.controleportaria.repository.EntradaRepository;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
@RequestMapping("/entradas")
public class EntradaController {
    private final EntradaRepository repository;
    private final GestaoEntradaService service;
    public EntradaController(EntradaRepository repository, GestaoEntradaService service) {
        this.repository = repository;
        this.service = service;
    }

    @ModelAttribute("tipos")
    public TipoEntrada[] tipos() { return TipoEntrada.values(); }
    @ModelAttribute("stattus")
    public StatusEntrada[] status() { return StatusEntrada.values(); }

    private String formulario(Model model, boolean editando) {
        model.addAttribute("conteudo", "entrada/registro");
        model.addAttribute("pageTitle", editando ? "Editar entrada" : "Registrar entrada");
        return "layout";
    }
    private String lista(Model model, java.util.List<Entrada> entradas) {
        model.addAttribute("entradas", entradas);
        model.addAttribute("conteudo", "entrada/listaEntradas");
        model.addAttribute("pageTitle", "Veículos");
        return "layout";
    }

    @GetMapping("/new")
    public String novo(Model model) {
        model.addAttribute("entradaAtual", new Entrada());
        return formulario(model, false);
    }

    @PostMapping("/registrar")
    public String registrar(@Valid @ModelAttribute("entradaAtual") Entrada entrada,
            BindingResult result, Model model, RedirectAttributes redirect) {
        boolean editando = entrada.getId() != null;
        if (result.hasErrors()) return formulario(model, editando);
        try {
            service.criar(entrada);
        } catch (NegocioException | IllegalArgumentException ex) {
            result.reject("entrada.invalida", ex.getMessage());
            return formulario(model, editando);
        }
        redirect.addFlashAttribute("message", !editando ?
                "Entrada registrada com sucesso." : "Entrada atualizada com sucesso.");
        return "redirect:/entradas/listar";
    }

    @GetMapping("/listar")
    public String listar(Model model) { return lista(model, service.listar()); }

    @GetMapping("/buscar")
    public String buscar(@RequestParam(value = "buscarEntrada", defaultValue = "") String placa, Model model) {
        return lista(model, placa.isBlank() ? service.listar() : service.buscarPlaca(placa.trim()));
    }

    @GetMapping("/data")
    public String porData(@RequestParam(value = "buscarData", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, Model model) {
        return lista(model, data == null ? service.listar() : service.buscarData(data));
    }

    @GetMapping("/intervalo")
    public String porIntervalo(@RequestParam(value = "dataInicio", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(value = "dataFim", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            Model model) {
        if (inicio == null || fim == null || inicio.isAfter(fim)) {
            model.addAttribute("error", "Informe um intervalo de datas válido.");
            return lista(model, service.listar());
        }
        return lista(model, service.intervaloDatas(inicio, fim));
    }

    @GetMapping("/status")
    public String porStatus(@RequestParam(value = "statusEntrada", required = false) StatusEntrada status,
            Model model) {
        return lista(model, status == null ? service.listar() : service.listarPorStatus(status));
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Entrada entrada = service.buscar(id);
        if (entrada.getStatus() == StatusEntrada.FINALIZADA)
            throw new NegocioException("Esta entrada já foi finalizada e não pode ser editada.");
        model.addAttribute("entradaAtual", entrada);
        return formulario(model, true);
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        repository.findById(id).ifPresent(repository::delete);
        redirect.addFlashAttribute("message", "Entrada removida com sucesso.");
        return "redirect:/entradas/listar";
    }
    @PostMapping("/finalizar/{id}")
    public String finalizar(@PathVariable Long id, RedirectAttributes redirect) {
        service.finalizar(id);
        redirect.addFlashAttribute("message", "Saída registrada com sucesso.");
        return "redirect:/entradas/listar";
    }
    @GetMapping("/export/pdf")
    public void exportar(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        String data = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        response.setHeader("Content-Disposition", "attachment; filename=relatorio-entradas-" + data + ".pdf");
        new EntradaPdfExporter(service.listAll()).export(response);
    }
}
