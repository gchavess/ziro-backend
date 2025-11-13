package br.com.ziro.lite.controller.lancamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.lancamento.LancamentoDTO;
import br.com.ziro.lite.dto.lancamento.LancamentoGraficoDTO;
import br.com.ziro.lite.exception.conta.ContaNaoEncontradoException;
import br.com.ziro.lite.exception.lancamento.LancamentoNaoEncontradoException;
import br.com.ziro.lite.service.lancamento.LancamentoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LancamentoControllerTest {

  @Mock private LancamentoService service;

  @InjectMocks private LancamentoController controller;

  private LancamentoDTO lancamento1;
  private LancamentoDTO lancamento2;
  private LancamentoGraficoDTO graficoDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    lancamento1 = new LancamentoDTO();
    lancamento1.setId(1L);
    lancamento1.setDescricao("Pagamento 1");
    lancamento1.setValorBruto(new BigDecimal("100.00"));
    lancamento1.setDataPagamento(new Date());

    lancamento2 = new LancamentoDTO();
    lancamento2.setId(2L);
    lancamento2.setDescricao("Pagamento 2");
    lancamento2.setValorBruto(new BigDecimal("200.00"));
    lancamento2.setDataPagamento(new Date());

    LancamentoGraficoDTO.Dataset dataset1 =
        new LancamentoGraficoDTO.Dataset(
            "Conta 1", Arrays.asList(new BigDecimal("100.00"), new BigDecimal("200.00")), "blue");
    graficoDTO =
        new LancamentoGraficoDTO(Arrays.asList("Nov/2025", "Nov/2025"), Arrays.asList(dataset1));
  }

  @Test
  void listarTodos_deveRetornarListaDeLancamentos() {
    when(service.listarTodos()).thenReturn(Arrays.asList(lancamento1, lancamento2));

    List<LancamentoDTO> result = controller.listarTodos();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(service, times(1)).listarTodos();
  }

  @Test
  void buscarPorId_quandoExiste_deveRetornarLancamento() throws LancamentoNaoEncontradoException {
    when(service.buscarPorId(1L)).thenReturn(lancamento1);

    LancamentoDTO result = controller.buscarPorId(1L);

    assertNotNull(result);
    assertEquals(lancamento1, result);
    verify(service, times(1)).buscarPorId(1L);
  }

  @Test
  void criar_deveChamarServicoESalvar() throws ContaNaoEncontradoException {
    when(service.salvar(lancamento1)).thenReturn(lancamento1);

    LancamentoDTO result = controller.criar(lancamento1);

    assertNotNull(result);
    assertEquals(lancamento1, result);
    verify(service, times(1)).salvar(lancamento1);
  }

  @Test
  void atualizar_deveChamarServicoEAtualizar()
      throws LancamentoNaoEncontradoException, ContaNaoEncontradoException {
    when(service.atualizar(1L, lancamento1)).thenReturn(lancamento1);

    LancamentoDTO result = controller.atualizar(1L, lancamento1);

    assertNotNull(result);
    assertEquals(lancamento1, result);
    verify(service, times(1)).atualizar(1L, lancamento1);
  }

  @Test
  void deletar_deveChamarServico() {
    doNothing().when(service).deletar(1L);

    controller.deletar(1L);

    verify(service, times(1)).deletar(1L);
  }

  @Test
  void montarGraficoLinhaDetalhadoComFiltro_deveRetornarGrafico() {
    LocalDate inicio = LocalDate.of(2025, 11, 1);
    LocalDate fim = LocalDate.of(2025, 11, 30);

    when(service.montarGraficoLinhaComFiltro(inicio, fim, 1L, 2L)).thenReturn(graficoDTO);

    LancamentoGraficoDTO result =
        controller.montarGraficoLinhaDetalhadoComFiltro(inicio, fim, 1L, 2L);

    assertNotNull(result);
    assertEquals(graficoDTO, result);
    verify(service, times(1)).montarGraficoLinhaComFiltro(inicio, fim, 1L, 2L);
  }

  @Test
  void montarGraficoLinhaSimplificadoComFiltro_deveRetornarGrafico() {
    LocalDate inicio = LocalDate.of(2025, 11, 1);
    LocalDate fim = LocalDate.of(2025, 11, 30);

    when(service.montarGraficoLinhaSimplificadoComFiltro(inicio, fim, 1L, 2L))
        .thenReturn(graficoDTO);

    LancamentoGraficoDTO result =
        controller.montarGraficoLinhaSimplificadoComFiltro(inicio, fim, 1L, 2L);

    assertNotNull(result);
    assertEquals(graficoDTO, result);
    verify(service, times(1)).montarGraficoLinhaSimplificadoComFiltro(inicio, fim, 1L, 2L);
  }
}
