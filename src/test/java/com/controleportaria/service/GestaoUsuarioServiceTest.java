package com.controleportaria.service;

import com.controleportaria.model.Usuario;
import com.controleportaria.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestaoUsuarioServiceTest {
    @Mock UsuarioRepository repository;
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void cadastroSalvaSenhaBCrypt() {
        GestaoUsuarioService service = new GestaoUsuarioService(repository, encoder);
        Usuario usuario = new Usuario();
        usuario.setSenha("senhaDeTeste123!");
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Usuario salvo = service.cadastrar(usuario);
        assertNotEquals("senhaDeTeste123!", salvo.getSenha());
        assertTrue(encoder.matches("senhaDeTeste123!", salvo.getSenha()));
    }

    @Test
    void edicaoSemNovaSenhaPreservaHashAnterior() {
        GestaoUsuarioService service = new GestaoUsuarioService(repository, encoder);
        Usuario antigo = new Usuario();
        antigo.setSenha(encoder.encode("senhaOriginal123!"));
        Usuario editado = new Usuario();
        editado.setId(1L);
        editado.setSenha("");
        when(repository.findById(1L)).thenReturn(Optional.of(antigo));
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Usuario salvo = service.cadastrar(editado);
        assertEquals(antigo.getSenha(), salvo.getSenha());
    }

    @Test
    void cadastroSemSenhaFalha() {
        GestaoUsuarioService service = new GestaoUsuarioService(repository, encoder);
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(new Usuario()));
        verify(repository, never()).save(any());
    }
}
