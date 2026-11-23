package com.controleportaria.security;

import com.controleportaria.model.Role;
import com.controleportaria.model.Usuario;
import com.controleportaria.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootstrapAdminTest {
    @Mock UsuarioRepository repository;
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    private MockEnvironment config() {
        return new MockEnvironment()
            .withProperty("app.bootstrap-admin.enabled", "true")
            .withProperty("app.bootstrap-admin.username", "admin-local")
            .withProperty("app.bootstrap-admin.password", "Segura-12345!Test")
            .withProperty("app.bootstrap-admin.name", "Admin Local")
            .withProperty("app.bootstrap-admin.matricula", "1234");
    }

    @Test
    void criaPrimeiroAdminComSenhaCriptografada() {
        BootstrapAdmin bootstrap = new BootstrapAdmin(repository, encoder, config());
        bootstrap.run();
        org.mockito.ArgumentCaptor<Usuario> captor = org.mockito.ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(captor.capture());
        assertEquals(Role.ADMIN, captor.getValue().getRole());
        assertTrue(encoder.matches("Segura-12345!Test", captor.getValue().getSenha()));
    }

    @Test
    void naoDuplicaAdministradorExistente() {
        when(repository.existsByRole(Role.ADMIN)).thenReturn(true);
        new BootstrapAdmin(repository, encoder, config()).run();
        verify(repository, never()).save(any());
    }

    @Test
    void ficaDesativadoPorPadrao() {
        new BootstrapAdmin(repository, encoder, new MockEnvironment()).run();
        verifyNoInteractions(repository);
    }

    @Test
    void recusaSenhaFraca() {
        MockEnvironment environment = config().withProperty("app.bootstrap-admin.password", "curta");
        assertThrows(IllegalStateException.class,
            () -> new BootstrapAdmin(repository, encoder, environment).run());
        verify(repository, never()).save(any());
    }
}
