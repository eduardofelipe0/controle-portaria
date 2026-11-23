package com.controleportaria.validation;

import com.controleportaria.model.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidacaoFormulariosTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll static void iniciar() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll static void encerrar() { factory.close(); }

    private Entrada entradaValida() {
        Entrada e = new Entrada();
        e.setVeiculo("Honda Civic");
        e.setPlaca("ABC1D23");
        e.setNomeMotorista("Maria da Silva");
        e.setNumeroApt("101");
        e.setTipo(TipoEntrada.HÓSPEDE);
        return e;
    }

    @Test void aceitaEntradaValidaComApartamento101() {
        assertTrue(validator.validate(entradaValida()).isEmpty());
    }

    @Test void rejeitaPlacaInvalida() {
        Entrada e = entradaValida();
        e.setPlaca("AB12");
        assertFalse(validator.validate(e).isEmpty());
    }

    @Test void aceitaEntradaSemApartamento() {
        Entrada e = entradaValida();
        e.setNumeroApt("");
        assertTrue(validator.validate(e).isEmpty());
    }

    @Test void rejeitaEntradaSemTipo() {
        Entrada e = entradaValida();
        e.setTipo(null);
        assertFalse(validator.validate(e).isEmpty());
    }

    @Test void rejeitaObjetoSemDescricao() {
        Objetos o = new Objetos();
        o.setFuncionario("Portaria");
        o.setComentario("  ");
        o.setTipoEntradaObjeto(TipoEntradaObjeto.SAIDA_COM_TERCEIROS);
        assertFalse(validator.validate(o).isEmpty());
    }

    @Test void rejeitaObjetoSemTipo() {
        Objetos o = new Objetos();
        o.setFuncionario("Portaria");
        o.setComentario("Caixa lacrada");
        assertFalse(validator.validate(o).isEmpty());
    }

    @Test void aceitaMatriculaComZerosAEsquerda() {
        Usuario u = new Usuario();
        u.setNome("Operador de Portaria");
        u.setMatricula("0001");
        u.setNomeUsuario("operador01");
        u.setRole(Role.USER);
        assertTrue(validator.validate(u).isEmpty());
    }

    @Test void rejeitaMatriculaComLetras() {
        Usuario u = new Usuario();
        u.setNome("Operador de Portaria");
        u.setMatricula("ABC");
        u.setNomeUsuario("operador01");
        u.setRole(Role.USER);
        assertFalse(validator.validate(u).isEmpty());
    }
}
