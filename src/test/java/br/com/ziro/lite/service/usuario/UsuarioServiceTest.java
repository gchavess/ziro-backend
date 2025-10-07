package br.com.ziro.lite.service.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.service.contextoconta.ContextoContaService;
import br.com.ziro.lite.service.naturezaconta.NaturezaContaService;
import br.com.ziro.lite.util.password.PasswordUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

  @Mock private UsuarioRepository usuarioRepository;

  @Mock private PasswordUtil passwordUtil;

  @Mock private ContextoContaService contextoContaService;

  @Mock private NaturezaContaService naturezaContaService;

  @InjectMocks private UsuarioService usuarioService;

  private Usuario usuario;

  @BeforeEach
  void setup() {
    usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNome("Gustavo");
    usuario.setEmail("gustavo@email.com");
    usuario.setSenha("123456");
  }

  @Test
  void listarTodos_deveRetornarListaDeUsuarios() {
    when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

    List<Usuario> resultado = usuarioService.listarTodos();

    assertEquals(1, resultado.size());
    assertEquals("Gustavo", resultado.get(0).getNome());
  }

  @Test
  void buscarPorId_deveRetornarUsuarioQuandoExiste() {
    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

    Optional<Usuario> resultado = usuarioService.buscarPorId(1L);

    assertTrue(resultado.isPresent());
    assertEquals("Gustavo", resultado.get().getNome());
  }

  @Test
  void criar_deveSalvarUsuarioComSenhaHashEDataCriacao() throws Exception {
    when(passwordUtil.hashSHA256("123456")).thenReturn("hash123");
    when(usuarioRepository.save(any(Usuario.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Usuario resultado = usuarioService.criar(usuario);

    assertEquals("hash123", resultado.getSenha());
    assertNotNull(resultado.getDataCriacao());
    verify(contextoContaService, times(1)).inicializarValoresPadroes(resultado);
    verify(naturezaContaService, times(1)).inicializarValoresPadroes(resultado);
  }

  @Test
  void atualizar_deveAtualizarUsuarioExistente() throws Exception {
    Usuario atualizado = new Usuario();
    atualizado.setNome("Novo Nome");
    atualizado.setEmail("novo@email.com");
    atualizado.setSenha("novaSenha");

    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
    when(passwordUtil.hashSHA256("novaSenha")).thenReturn("hashNovaSenha");
    when(usuarioRepository.save(any(Usuario.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Optional<Usuario> resultado = usuarioService.atualizar(1L, atualizado);

    assertTrue(resultado.isPresent());
    assertEquals("Novo Nome", resultado.get().getNome());
    assertEquals("novo@email.com", resultado.get().getEmail());
    assertEquals("hashNovaSenha", resultado.get().getSenha());
  }

  @Test
  void atualizar_deveNaoAtualizarSeUsuarioNaoExistir() throws Exception {
    when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

    Optional<Usuario> resultado = usuarioService.atualizar(2L, usuario);

    assertTrue(resultado.isEmpty());
    verify(usuarioRepository, never()).save(any());
  }

  @Test
  void deletar_deveChamarDeleteDoRepositorio() {
    usuarioService.deletar(1L);

    verify(usuarioRepository, times(1)).deleteById(1L);
  }
}
