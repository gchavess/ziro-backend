package br.com.ziro.lite.service.contextoconta;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.exception.contextoconta.ContextoContaNaoEncontradoException;
import br.com.ziro.lite.repository.contextoconta.ContextoContaRepository;
import br.com.ziro.lite.repository.naturezaconta.NaturezaContaRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import br.com.ziro.lite.util.usuariopadrao.UsuarioPadraoUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContextoContaServiceTest {

  private ContextoContaService contextoContaService;

  @Mock private ContextoContaRepository repository;

  @Mock private UsuarioLogado usuarioLogado;

  @Mock private NaturezaContaRepository naturezaContaRepository;

  private Usuario usuario;
  private UsuarioDTO usuarioDTO;

  @BeforeEach
  void setUp() {
    contextoContaService =
        new ContextoContaService(repository, usuarioLogado, naturezaContaRepository);

    usuario = new Usuario();
    usuario.setId(1L);

    usuarioDTO = new UsuarioDTO(1L, "Teste", "teste@teste.com");
  }

  @Test
  void listarTodos_deveRetornarTodosContextos() {
    ContextoConta c1 = new ContextoConta();
    c1.setId(1L);
    c1.setDescricao("Contexto 1");
    ContextoConta c2 = new ContextoConta();
    c2.setId(2L);
    c2.setDescricao("Contexto 2");

    when(usuarioLogado.getCurrent()).thenReturn(usuario);
    when(repository.findAllByUsuarioCriacaoOrderByDataCriacaoAsc(usuario))
        .thenReturn(Arrays.asList(c1, c2));

    List<ContextoContaDTO> result = contextoContaService.listarTodos();

    assertEquals(2, result.size());
    assertEquals("Contexto 1", result.get(0).getDescricao());
    assertEquals("Contexto 2", result.get(1).getDescricao());
  }

  @Test
  void buscarPorId_deveRetornarOpcional() {
    ContextoConta c = new ContextoConta();
    c.setId(1L);

    when(repository.findById(1L)).thenReturn(Optional.of(c));

    Optional<ContextoConta> result = contextoContaService.buscarPorId(1L);
    assertTrue(result.isPresent());
    assertEquals(1L, result.get().getId());
  }

  @Test
  void salvar_deveSalvarContexto() {
    UsuarioDTO usuarioDTO = new UsuarioDTO(1L, "Teste", "teste@teste.com");

    ContextoContaDTO request =
        new ContextoContaDTO(null, "Descricao", "Obs", "Codigo", usuarioDTO, null);

    lenient().when(usuarioLogado.getCurrentDTO()).thenReturn(usuarioDTO);

    ContextoConta saved = new ContextoConta();
    saved.setId(1L);
    saved.setDescricao(request.getDescricao());

    when(repository.save(any(ContextoConta.class))).thenReturn(saved);

    ContextoContaDTO result = contextoContaService.salvar(request);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Descricao", result.getDescricao());
  }

  @Test
  void atualizar_deveAtualizarContexto() throws ContextoContaNaoEncontradoException {
    ContextoConta entity = new ContextoConta();
    entity.setId(1L);
    entity.setDescricao("Antigo");

    when(repository.findById(1L)).thenReturn(Optional.of(entity));
    when(repository.save(entity)).thenReturn(entity);

    ContextoConta request = new ContextoConta();
    request.setDescricao("Novo");
    request.setObservacao("Obs");
    request.setCodigo("Codigo");

    ContextoContaDTO result = contextoContaService.atualizar(1L, request);

    assertEquals("Novo", result.getDescricao());
  }

  @Test
  void deletar_deveChamarRepository() {
    doNothing().when(naturezaContaRepository).deleteByContextoId(1L);
    doNothing().when(repository).deleteById(1L);

    contextoContaService.deletar(1L);

    verify(naturezaContaRepository, times(1)).deleteByContextoId(1L);
    verify(repository, times(1)).deleteById(1L);
  }

  @Test
  void inicializarValoresPadroes_deveSalvarNovosContextos() throws Exception {
    Usuario usuarioNovo = new Usuario();
    usuarioNovo.setId(2L);

    ContextoConta padrao = new ContextoConta();
    padrao.setId(10L);

    try (MockedStatic<UsuarioPadraoUtil> mocked = Mockito.mockStatic(UsuarioPadraoUtil.class)) {
      mocked.when(UsuarioPadraoUtil::get).thenReturn(usuario);

      when(repository.existsByUsuarioCriacaoAndPadrao(usuario, false)).thenReturn(false);
      when(repository.findAllByUsuarioCriacaoAndPadrao(usuario, true)).thenReturn(List.of(padrao));

      lenient().when(repository.save(any(ContextoConta.class))).thenAnswer(i -> i.getArgument(0));

      contextoContaService.inicializarValoresPadroes(usuarioNovo);

      verify(repository, times(1)).save(any(ContextoConta.class));
    }
  }
}
