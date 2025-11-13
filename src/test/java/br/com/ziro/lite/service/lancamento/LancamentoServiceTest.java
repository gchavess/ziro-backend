package br.com.ziro.lite.service.lancamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.conta.ContaDTO;
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

  // ------------------------------------------------------
  // LISTAR TODOS
  // ------------------------------------------------------

  @Test
  void listarTodos_deveRetornarLancamentos() {
    Lancamento lancamento = new Lancamento();
    lancamento.setId(1L);

    when(repository.findAllByUsuarioCriacao(usuario)).thenReturn(List.of(lancamento));

    var result = service.listarTodos();

    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getId());
  }

  // ------------------------------------------------------
  // BUSCAR POR ID
  // ------------------------------------------------------

  @Test
  void buscarPorId_existente_deveRetornarLancamento() throws Exception {
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

  // ------------------------------------------------------
  // SALVAR
  // ------------------------------------------------------

  @Test
  void salvar_comContaExistente_deveSalvarLancamento() throws Exception {
    Conta conta = new Conta();
    conta.setId(10L);
    conta.setUsuarioCriacao(usuario);

    LancamentoDTO dto = new LancamentoDTO();
    dto.setDescricao("Teste");
    dto.setConta(
        new ContaDTO() {
          {
            setId(10L);
          }
        });
    dto.setValorBruto(BigDecimal.valueOf(100));

    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
    when(contaRepository.findById(10L)).thenReturn(Optional.of(conta));
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = service.salvar(dto);

    assertEquals("Teste", result.getDescricao());
    assertEquals(BigDecimal.valueOf(100), result.getValorBruto());
  }

  @Test
  void salvar_comContaCodigo_deveUsarFindByUsuarioCriacaoAndCodigo() throws Exception {
    Conta conta = new Conta();
    conta.setId(99L);
    conta.setUsuarioCriacao(usuario);

    LancamentoDTO dto = new LancamentoDTO();
    dto.setDescricao("TesteCodigo");
    dto.setContaCodigo("C123");

    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
    when(contaRepository.findByUsuarioCriacaoAndCodigo(usuario, "C123"))
        .thenReturn(Optional.of(conta));
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = service.salvar(dto);

    assertEquals("TesteCodigo", result.getDescricao());
    assertEquals(99L, result.getConta().getId());
  }

  @Test
  void salvar_contaNaoEncontrada_deveLancarException() {
    LancamentoDTO dto = new LancamentoDTO();
    dto.setConta(
        new ContaDTO() {
          {
            setId(123L);
          }
        });

    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
    when(contaRepository.findById(123L)).thenReturn(Optional.empty());

    assertThrows(ContaNaoEncontradoException.class, () -> service.salvar(dto));
  }

  // ------------------------------------------------------
  // ATUALIZAR
  // ------------------------------------------------------

  @Test
  void atualizar_existente_deveAtualizarCampos() throws Exception {
    Lancamento lanc = new Lancamento();
    lanc.setId(1L);

    LancamentoDTO dto = new LancamentoDTO();
    dto.setDescricao("Atualizado");
    dto.setValorBruto(BigDecimal.valueOf(200));

    when(repository.findById(1L)).thenReturn(Optional.of(lanc));
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = service.atualizar(1L, dto);

    assertEquals("Atualizado", result.getDescricao());
    assertEquals(BigDecimal.valueOf(200), result.getValorBruto());
  }

  @Test
  void atualizar_comConta_deveAlterarConta() throws Exception {
    Lancamento lanc = new Lancamento();
    lanc.setId(1L);

    Conta conta = new Conta();
    conta.setId(5L);
    conta.setUsuarioCriacao(usuario);

    LancamentoDTO dto = new LancamentoDTO();
    dto.setConta(
        new ContaDTO() {
          {
            setId(5L);
          }
        });

    when(repository.findById(1L)).thenReturn(Optional.of(lanc));
    when(contaRepository.findById(5L)).thenReturn(Optional.of(conta));
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = service.atualizar(1L, dto);

    assertEquals(5L, result.getConta().getId());
  }

  @Test
  void atualizar_naoEncontrado_deveLancarException() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(
        LancamentoNaoEncontradoException.class, () -> service.atualizar(1L, new LancamentoDTO()));
  }

  // ------------------------------------------------------
  // DELETAR
  // ------------------------------------------------------

  @Test
  void deletar_deveChamarDelete() {
    service.deletar(1L);
    verify(repository).deleteById(1L);
  }

  // ------------------------------------------------------
  // GRAFICO LINHA (COMPLEXO)
  // ------------------------------------------------------

  @Test
  void montarGraficoLinhaComFiltro_deveGerarGrafico() {
    LancamentoGraficoCelulaDTO celula =
        new LancamentoGraficoCelulaDTO(
            "Conta A",
            BigDecimal.valueOf(100),
            "01/10/2025",
            null,
            "Contexto X",
            1L,
            "Natureza Y",
            1L);

    LancamentoService spyService = spy(service);
    doReturn(List.of(celula)).when(spyService).listarParaGrafico();

    var grafico =
        spyService.montarGraficoLinhaComFiltro(
            LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 31), null, null);

    assertEquals(1, grafico.datasets().size());
    assertEquals("Contexto X", grafico.datasets().get(0).label());
    assertEquals(BigDecimal.valueOf(100), grafico.datasets().get(0).data().get(0));
  }

  // ------------------------------------------------------
  // GRAFICO SIMPLIFICADO
  // ------------------------------------------------------

  @Test
  void montarGraficoLinhaSimplificadoComFiltro_deveAgruparRealizadoEComprometido() {

    LancamentoGraficoCelulaDTO realizado =
        new LancamentoGraficoCelulaDTO(
            "Conta B",
            BigDecimal.valueOf(150),
            "10/10/2025",
            null,
            "Contexto Z",
            2L,
            "Natureza A",
            3L);

    LancamentoGraficoCelulaDTO comprometido =
        new LancamentoGraficoCelulaDTO(
            "Conta B",
            BigDecimal.valueOf(200),
            null,
            "15/10/2025",
            "Contexto Z",
            2L,
            "Natureza A",
            3L);

    LancamentoService spyService = spy(service);
    doReturn(List.of(realizado, comprometido)).when(spyService).listarParaGrafico();

    var grafico =
        spyService.montarGraficoLinhaSimplificadoComFiltro(
            LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 31), null, null);

    assertEquals(2, grafico.datasets().size()); // realizado + comprometido
    assertEquals(1, grafico.datasets().get(0).data().size()); // 1 mês
    assertEquals(BigDecimal.valueOf(150), grafico.datasets().get(0).data().get(0));
    assertEquals(BigDecimal.valueOf(200), grafico.datasets().get(1).data().get(0));
  }
}
