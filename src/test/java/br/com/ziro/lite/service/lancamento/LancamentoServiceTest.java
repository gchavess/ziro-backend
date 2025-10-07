package br.com.ziro.lite.service.lancamento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.lancamento.LancamentoDTO;
import br.com.ziro.lite.dto.lancamento.LancamentoGraficoCelulaDTO;
import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.lancamento.Lancamento;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.exception.conta.ContaNaoEncontradoException;
import br.com.ziro.lite.exception.lancamento.LancamentoNaoEncontradoException;
import br.com.ziro.lite.repository.conta.ContaRepository;
import br.com.ziro.lite.repository.lancamento.LancamentoRepository;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LancamentoServiceTest {

  @InjectMocks private LancamentoService service;

  @Mock private LancamentoRepository repository;

  @Mock private ContaRepository contaRepository;

  @Mock private UsuarioRepository usuarioRepository;

  @Mock private UsuarioLogado usuarioLogado;

  private Usuario usuario;
  private UsuarioDTO usuarioDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    usuario = new Usuario();
    usuario.setId(1L);

    usuarioDTO = new UsuarioDTO();
    usuarioDTO.setId(1L);

    when(usuarioLogado.getCurrent()).thenReturn(usuario);
    when(usuarioLogado.getCurrentDTO()).thenReturn(usuarioDTO);
  }

  @Test
  void listarTodos_deveRetornarLancamentos() {
    Lancamento lancamento = new Lancamento();
    lancamento.setId(1L);

    when(repository.findAllByUsuarioCriacao(usuario)).thenReturn(List.of(lancamento));

    var result = service.listarTodos();

    assertEquals(1, result.size());
    assertEquals(lancamento.getId(), result.get(0).getId());
  }

  @Test
  void buscarPorId_existente_deveRetornarLancamento() throws LancamentoNaoEncontradoException {
    Lancamento lancamento = new Lancamento();
    lancamento.setId(1L);

    when(repository.findById(1L)).thenReturn(Optional.of(lancamento));

    var result = service.buscarPorId(1L);

    assertEquals(1L, result.getId());
  }

  @Test
  void buscarPorId_naoExistente_deveLancarExcecao() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(LancamentoNaoEncontradoException.class, () -> service.buscarPorId(1L));
  }

  @Test
  void salvar_comContaExistente_deveSalvarLancamento() throws ContaNaoEncontradoException {
    Conta conta = new Conta();
    conta.setId(10L);
    conta.setUsuarioCriacao(usuario); // ✅ evita NullPointer

    LancamentoDTO dto = new LancamentoDTO();
    dto.setDescricao("Teste");
    dto.setConta(
        new br.com.ziro.lite.dto.conta.ContaDTO() {
          {
            setId(conta.getId());
          }
        });
    dto.setValorBruto(BigDecimal.valueOf(100));

    when(usuarioRepository.findById(usuarioDTO.getId())).thenReturn(Optional.of(usuario));
    when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = service.salvar(dto);

    assertEquals("Teste", result.getDescricao());
    assertEquals(BigDecimal.valueOf(100), result.getValorBruto());
    assertNotNull(result.getConta());
  }

  @Test
  void atualizar_existente_deveAtualizarCampos()
      throws LancamentoNaoEncontradoException, ContaNaoEncontradoException {
    Lancamento lancamento = new Lancamento();
    lancamento.setId(1L);

    LancamentoDTO dto = new LancamentoDTO();
    dto.setDescricao("Atualizado");
    dto.setValorBruto(BigDecimal.valueOf(200));

    when(repository.findById(1L)).thenReturn(Optional.of(lancamento));
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = service.atualizar(1L, dto);

    assertEquals("Atualizado", result.getDescricao());
    assertEquals(BigDecimal.valueOf(200), result.getValorBruto());
  }

  @Test
  void deletar_deveChamarDelete() {
    service.deletar(1L);
    verify(repository, times(1)).deleteById(1L);
  }

  @Test
  void montarGraficoLinhaComFiltro_deveGerarGrafico() {
    // ✅ valores correspondem ao construtor do record
    LancamentoGraficoCelulaDTO celula =
        new LancamentoGraficoCelulaDTO(
            "Conta A", // contaDescricao
            BigDecimal.valueOf(100), // valorBruto
            "01/10/2025", // dataPagamento
            null, // dataVencimento
            "Contexto X", // contextoDescricao
            1L, // contextoId
            "Natureza Y", // naturezaDescricao
            1L // naturezaId
            );

    // Mock do método listarParaGrafico do próprio service
    LancamentoService spyService = spy(service);
    doReturn(List.of(celula)).when(spyService).listarParaGrafico();

    var grafico =
        spyService.montarGraficoLinhaComFiltro(
            LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 31), null, null);

    // ✅ agrupamento padrão é por contextoDescricao
    assertEquals(1, grafico.datasets().size());
    assertEquals("Contexto X", grafico.datasets().get(0).label());
    assertEquals(BigDecimal.valueOf(100), grafico.datasets().get(0).data().get(0));
  }
}
