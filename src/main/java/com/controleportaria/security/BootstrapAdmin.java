package com.controleportaria.security;

import com.controleportaria.model.Role;
import com.controleportaria.model.Usuario;
import com.controleportaria.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapAdmin implements CommandLineRunner {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final Environment env;

    public BootstrapAdmin(UsuarioRepository repository, PasswordEncoder encoder, Environment env) {
        this.repository = repository;
        this.encoder = encoder;
        this.env = env;
    }

    @Override
    public void run(String... args) {
        if (!env.getProperty("app.bootstrap-admin.enabled", Boolean.class, false)) return;
        // Nunca sobrescrever administradores previamente criados.
        if (repository.existsByRole(Role.ADMIN)) return;

        String username = env.getProperty("app.bootstrap-admin.username", "").trim();
        String password = env.getProperty("app.bootstrap-admin.password", "");
        String name = env.getProperty("app.bootstrap-admin.name", "Administrador");
        String matricula = env.getProperty("app.bootstrap-admin.matricula", "0000");
        if (username.isBlank() || password.length() < 12 || username.length() > 30
                || name.isBlank() || name.length() > 40 || !matricula.matches("\\d{1,4}")) {
            throw new IllegalStateException("Bootstrap ADMIN: configure usuario, senha >= 12 caracteres, nome e matricula validos.");
        }
        if (repository.findByNomeUsuario(username) != null) {
            throw new IllegalStateException("Bootstrap ADMIN: nome de usuario ja cadastrado.");
        }
        Usuario admin = new Usuario();
        admin.setNome(name);
        admin.setMatricula(matricula);
        admin.setNomeUsuario(username);
        admin.setSenha(encoder.encode(password));
        admin.setRole(Role.ADMIN);
        repository.save(admin);
    }
}
