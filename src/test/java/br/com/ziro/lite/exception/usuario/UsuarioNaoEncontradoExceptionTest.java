package br.com.ziro.lite.exception.usuario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioNaoEncontradoExceptionTest {

    @Test
    void deveTerMensagemPadraoCorreta() {
        UsuarioNaoEncontradoException ex = new UsuarioNaoEncontradoException();
        assertEquals("Usuário não encontrado", ex.getMessage());
    }
}
