package br.com.ziro.lite.service.naturezaconta;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import br.com.ziro.lite.dto.naturezaconta.NaturezaContaAgrupadaDTO;
import br.com.ziro.lite.dto.naturezaconta.NaturezaContaDTO;
import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.entity.naturezaconta.NaturezaConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.exception.contextoconta.ContextoContaNaoEncontradoException;
import br.com.ziro.lite.exception.naturezaconta.NaturezaContaNaoEncontradoException;
import br.com.ziro.lite.repository.contextoconta.ContextoContaRepository;
import br.com.ziro.lite.repository.naturezaconta.NaturezaContaRepository;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NaturezaContaServiceTest {

  @Mock private NaturezaContaRepository repository;

  @Mock private UsuarioRepository usuarioRepository;

  @Mock private ContextoContaRepository contextoRepository;

  @Mock private UsuarioLogado usuarioLogado;

  @InjectMocks private NaturezaContaService service;

  private Usuario usuario;
  private ContextoConta contexto;
  private NaturezaConta natureza;

  @BeforeEach
  void setup() {
    usuario = new Usuario();
    usuario.setId(1L);

    contexto = new ContextoConta();
    contexto.setId(10L);
    contexto.setDescricao("Contexto 1");

    natureza = new NaturezaConta();
    natureza.setId(100L);
    natureza.setDescricao("Natureza 1");
    natureza.setContextoConta(contexto);

    lenient().when(usuarioLogado.getCurrent()).thenReturn(usuario);

    UsuarioDTO usuarioDTO = new UsuarioDTO();
    usuarioDTO.setId(usuario.getId());
    lenient().when(usuarioLogado.getCurrentDTO()).thenReturn(usuarioDTO);
  }

  @Test
  void listarTodos_deveRetornarNaturezas() {
    when(repository.findAllByUsuarioCriacao(usuario)).thenReturn(List.of(natureza));

    List<NaturezaContaDTO> resultado = service.listarTodos();

    assertEquals(1, resultado.size());
    assertEquals("Natureza 1", resultado.get(0).getDescricao());
  }

  @Test
  void buscarPorId_deveRetornarNaturezaQuandoExistir() throws NaturezaContaNaoEncontradoException {
    when(repository.findById(100L)).thenReturn(Optional.of(natureza));

    NaturezaContaDTO resultado = service.buscarPorId(100L);

    assertEquals("Natureza 1", resultado.getDescricao());
  }

  @Test
  void buscarPorId_deveLancarExcecaoQuandoNaoExistir() {
    when(repository.findById(200L)).thenReturn(Optional.empty());

    assertThrows(NaturezaContaNaoEncontradoException.class, () -> service.buscarPorId(200L));
  }

  @Test
  void salvar_deveCriarNatureza() throws ContextoContaNaoEncontradoException {
    NaturezaContaDTO dto = new NaturezaContaDTO();
    dto.setDescricao("Nova Natureza");
    dto.setContextoConta(new ContextoContaDTO());
    dto.getContextoConta().setId(10L);

    when(contextoRepository.findById(10L)).thenReturn(Optional.of(contexto));
    when(repository.save(any(NaturezaConta.class))).thenAnswer(inv -> inv.getArgument(0));

    NaturezaContaDTO resultado = service.salvar(dto);

    assertEquals("Nova Natureza", resultado.getDescricao());
    assertNotNull(resultado.getContextoConta());
  }

  @Test
  void atualizar_deveAtualizarNatureza()
      throws ContextoContaNaoEncontradoException, NaturezaContaNaoEncontradoException {
    NaturezaContaDTO dto = new NaturezaContaDTO();
    dto.setDescricao("Atualizada");
    dto.setContextoConta(new ContextoContaDTO());
    dto.getContextoConta().setId(10L);

    when(repository.findById(100L)).thenReturn(Optional.of(natureza));
    when(contextoRepository.findById(10L)).thenReturn(Optional.of(contexto));
    when(repository.save(any(NaturezaConta.class))).thenAnswer(inv -> inv.getArgument(0));

    NaturezaContaDTO resultado = service.atualizar(100L, dto);

    assertEquals("Atualizada", resultado.getDescricao());
  }

  @Test
  void deletar_deveChamarDeleteDoRepositorio() {
    service.deletar(100L);

    verify(repository, times(1)).deleteById(100L);
  }

  @Test
  void listarAgrupadasPorContexto_deveRetornarDTOsAgrupados() {
    when(contextoRepository.findAllByUsuarioCriacao(usuario)).thenReturn(List.of(contexto));
    when(repository.findAllByUsuarioCriacao(usuario)).thenReturn(List.of(natureza));

    List<NaturezaContaAgrupadaDTO> resultado = service.listarAgrupadasPorContexto();

    assertEquals(1, resultado.size());
    assertEquals(1, resultado.get(0).getChildren().size());
    assertEquals("Natureza 1", resultado.get(0).getChildren().get(0).getDescricao());
  }

  @Test
  void inicializarValoresPadroes_deveChamarSalvarQuandoNaoExiste() throws Exception {
    when(repository.existsByUsuarioCriacaoAndPadrao(any(), eq(false))).thenReturn(false);
    when(repository.findAllByUsuarioCriacaoAndPadrao(any(), eq(true)))
        .thenReturn(List.of(natureza));
    when(contextoRepository.findByUsuarioCriacaoAndContextoContaPadraoId(any(), any()))
        .thenReturn(Optional.of(contexto));
    when(contextoRepository.findById(anyLong())).thenReturn(Optional.of(contexto));
    when(repository.save(any(NaturezaConta.class))).thenAnswer(inv -> inv.getArgument(0));

    service.inicializarValoresPadroes(usuario);

    verify(repository, atLeastOnce()).save(any());
  }
}
