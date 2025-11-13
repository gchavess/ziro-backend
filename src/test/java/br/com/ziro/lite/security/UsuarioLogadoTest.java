package br.com.ziro.lite.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.usuario.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsuarioLogadoTest {

  private HttpServletRequest request;
  private UsuarioLogado usuarioLogado;

  @BeforeEach
  void setup() {
    request = mock(HttpServletRequest.class);
    usuarioLogado = new UsuarioLogado(request);
  }

  // ------------------------------
  // 1. getCurrentDTO com header presente
  // ------------------------------
  @Test
  void getCurrentDTO_deveRetornarUsuarioDTOQuandoHeaderPresente() {
    when(request.getHeader("XXX-USUARIO-ID")).thenReturn("123");

    UsuarioDTO usuarioDTO = usuarioLogado.getCurrentDTO();

    assertNotNull(usuarioDTO);
    assertEquals(123L, usuarioDTO.getId());
  }

  // ------------------------------
  // 2. getCurrent com header presente
  // ------------------------------
  @Test
  void getCurrent_deveRetornarUsuarioQuandoHeaderPresente() {
    when(request.getHeader("XXX-USUARIO-ID")).thenReturn("456");

    Usuario usuario = usuarioLogado.getCurrent();

    assertNotNull(usuario);
    assertEquals(456L, usuario.getId());
  }

  // ------------------------------
  // 3. getCurrentDTO lança exceção quando header ausente
  // ------------------------------
  @Test
  void getCurrentDTO_deveLancarExcecaoQuandoHeaderAusente() {
    when(request.getHeader("XXX-USUARIO-ID")).thenReturn(null);

    IllegalStateException exception =
        assertThrows(IllegalStateException.class, () -> usuarioLogado.getCurrentDTO());

    assertEquals("Header XXX-USUARIO-ID não informado!", exception.getMessage());
  }

  // ------------------------------
  // 4. getCurrent lança exceção quando header ausente
  // ------------------------------
  @Test
  void getCurrent_deveLancarExcecaoQuandoHeaderAusente() {
    when(request.getHeader("XXX-USUARIO-ID")).thenReturn(null);

    IllegalStateException exception =
        assertThrows(IllegalStateException.class, () -> usuarioLogado.getCurrent());

    assertEquals("Header XXX-USUARIO-ID não informado!", exception.getMessage());
  }
}
