package br.com.ziro.lite.controller.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.service.usuario.UsuarioService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class UsuarioControllerTest {

  @Mock private UsuarioService usuarioService;

  @InjectMocks private UsuarioController usuarioController;

  private Usuario usuario1;
  private Usuario usuario2;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    usuario1 = new Usuario();
    usuario1.setId(1L);
    usuario1.setNome("Usuário 1");

    usuario2 = new Usuario();
    usuario2.setId(2L);
    usuario2.setNome("Usuário 2");
  }

  @Test
  void listarTodos_deveRetornarListaDeUsuarios() {
    when(usuarioService.listarTodos()).thenReturn(Arrays.asList(usuario1, usuario2));

    ResponseEntity<List<Usuario>> response = usuarioController.listarTodos();

    assertNotNull(response);
    assertEquals(2, response.getBody().size());
    verify(usuarioService, times(1)).listarTodos();
  }

  @Test
  void buscarPorId_quandoUsuarioExiste_deveRetornarUsuario() {
    when(usuarioService.buscarPorId(1L)).thenReturn(Optional.of(usuario1));

    ResponseEntity<Usuario> response = usuarioController.buscarPorId(1L);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(usuario1, response.getBody());
    verify(usuarioService, times(1)).buscarPorId(1L);
  }

  @Test
  void buscarPorId_quandoUsuarioNaoExiste_deveRetornarNotFound() {
    when(usuarioService.buscarPorId(99L)).thenReturn(Optional.empty());

    ResponseEntity<Usuario> response = usuarioController.buscarPorId(99L);

    assertEquals(404, response.getStatusCodeValue());
    verify(usuarioService, times(1)).buscarPorId(99L);
  }

  @Test
  void criar_deveRetornarUsuarioCriado() throws Exception {
    when(usuarioService.criar(usuario1)).thenReturn(usuario1);

    ResponseEntity<Usuario> response = usuarioController.criar(usuario1);

    assertEquals(201, response.getStatusCodeValue());
    assertEquals(usuario1, response.getBody());
    verify(usuarioService, times(1)).criar(usuario1);
  }

  @Test
  void atualizar_quandoUsuarioExiste_deveRetornarUsuarioAtualizado() throws Exception {
    when(usuarioService.atualizar(1L, usuario1)).thenReturn(Optional.of(usuario1));

    ResponseEntity<Usuario> response = usuarioController.atualizar(1L, usuario1);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(usuario1, response.getBody());
    verify(usuarioService, times(1)).atualizar(1L, usuario1);
  }

  @Test
  void atualizar_quandoUsuarioNaoExiste_deveRetornarNotFound() throws Exception {
    when(usuarioService.atualizar(99L, usuario1)).thenReturn(Optional.empty());

    ResponseEntity<Usuario> response = usuarioController.atualizar(99L, usuario1);

    assertEquals(404, response.getStatusCodeValue());
    verify(usuarioService, times(1)).atualizar(99L, usuario1);
  }

  @Test
  void deletar_deveChamarServicoEDarNoContent() {
    doNothing().when(usuarioService).deletar(1L);

    ResponseEntity<Void> response = usuarioController.deletar(1L);

    assertEquals(204, response.getStatusCodeValue());
    verify(usuarioService, times(1)).deletar(1L);
  }
}
