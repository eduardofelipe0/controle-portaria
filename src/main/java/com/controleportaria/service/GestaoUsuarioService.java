package com.controleportaria.service;

import com.controleportaria.model.Role;
import com.controleportaria.model.Usuario;
import com.controleportaria.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class GestaoUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;

    public GestaoUsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    public Usuario cadastrar(Usuario usuario) {
        if (usuario.getNomeUsuario() != null && !usuario.getNomeUsuario().isBlank()) {
            Usuario mesmoNome = usuarioRepository.findByNomeUsuario(usuario.getNomeUsuario());
            if (mesmoNome != null && !java.util.Objects.equals(mesmoNome.getId(), usuario.getId())) {
                throw new IllegalArgumentException("Este nome de usuário já está em uso.");
            }
        }
        String senha = usuario.getSenha();
        if (usuario.getId() == null) {
            if (senha == null || senha.length() < 12) {
                throw new IllegalArgumentException("Informe uma senha de pelo menos 12 caracteres para o novo usuario.");
            }
            usuario.setSenha(encoder.encode(senha));
        } else {
            Usuario existente = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado."));
            if (senha != null && !senha.isBlank() && senha.length() < 12) {
                throw new IllegalArgumentException("A nova senha deve ter pelo menos 12 caracteres.");
            }
            usuario.setSenha(senha == null || senha.isBlank() ? existente.getSenha() : encoder.encode(senha));
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> buscarNome(String nome) { return usuarioRepository.findUsuarioByName(nome); }
    public List<Usuario> listar() { return usuarioRepository.findAll(); }
    public List<Usuario> listarPorRole(Role role) { return usuarioRepository.findUsuarioByRole(role); }
    public Usuario buscar(Long id) { return usuarioRepository.findById(id).orElseThrow(); }
    public void deletar(Usuario usuario) { usuarioRepository.delete(usuario); }
}
