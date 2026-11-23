package com.controleportaria.controller;

import com.controleportaria.model.Role;
import com.controleportaria.model.Usuario;
import com.controleportaria.repository.UsuarioRepository;
import com.controleportaria.service.GestaoUsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final GestaoUsuarioService service;
    private final UsuarioRepository repository;
    public UsuarioController(GestaoUsuarioService service, UsuarioRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @ModelAttribute("roles")
    public Role[] roles() { return Role.values(); }

    private String formulario(Model model, boolean editando) {
        model.addAttribute("conteudo", "usuario/cadastroUsuario");
        model.addAttribute("pageTitle", editando ? "Editar usuário" : "Novo usuário");
        return "layout";
    }
    private String lista(Model model, java.util.List<Usuario> usuarios) {
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("conteudo", "usuario/listaUsuarios");
        model.addAttribute("pageTitle", "Usuários");
        return "layout";
    }

    @GetMapping("/new")
    public String novo(Model model) {
        model.addAttribute("usuarioAtual", new Usuario());
        return formulario(model, false);
    }
    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("usuarioAtual") Usuario usuario,
            BindingResult result, Model model, RedirectAttributes redirect) {
        boolean editando = usuario.getId() != null;
        if (result.hasErrors()) return formulario(model, editando);
        try {
            service.cadastrar(usuario);
        } catch (IllegalArgumentException ex) {
            result.reject("usuario.invalido", ex.getMessage());
            return formulario(model, editando);
        }
        redirect.addFlashAttribute("message", !editando ?
                "Usuário cadastrado com sucesso." : "Usuário atualizado com sucesso.");
        return "redirect:/usuarios/listar";
    }
    @GetMapping("/listar")
    public String listar(Model model) { return lista(model, service.listar()); }
    @GetMapping("/permissao")
    public String porRole(@RequestParam(value = "role", required = false) Role role, Model model) {
        return lista(model, role == null ? service.listar() : service.listarPorRole(role));
    }
    @GetMapping("/buscar")
    public String buscar(@RequestParam(value = "buscarUsuario", defaultValue = "") String nome, Model model) {
        return lista(model, nome.isBlank() ? service.listar() : service.buscarNome(nome.trim()));
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario u = service.buscar(id);
        Usuario f = new Usuario();
        f.setId(u.getId());
        f.setNome(u.getNome());
        f.setMatricula(u.getMatricula());
        f.setNomeUsuario(u.getNomeUsuario());
        f.setRole(u.getRole());
        model.addAttribute("usuarioAtual", f);
        return formulario(model, true);
    }
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, Authentication authentication, RedirectAttributes redirect) {
        Usuario u = repository.findById(id).orElse(null);
        if (u != null && u.getNomeUsuario().equals(authentication.getName())) {
            redirect.addFlashAttribute("error", "Você não pode excluir sua própria conta enquanto está conectado.");
        } else if (u != null) {
            repository.delete(u);
            redirect.addFlashAttribute("message", "Usuário removido com sucesso.");
        }
        return "redirect:/usuarios/listar";
    }
}
