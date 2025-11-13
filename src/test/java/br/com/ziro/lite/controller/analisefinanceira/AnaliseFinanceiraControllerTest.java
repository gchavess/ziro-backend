package br.com.ziro.lite.controller.analisefinanceira;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.analisefinanceira.usuario.AnaliseFinanceiraDTO;
import br.com.ziro.lite.dto.lancamento.LancamentoGraficoDTO;
import br.com.ziro.lite.dto.lancamento.LancamentoGraficoDTO.Dataset;
import br.com.ziro.lite.exception.analisefinanceira.AnaliseFinanceiraNaoEncontradaException;
import br.com.ziro.lite.service.analisefinanceira.AnaliseFinanceiraService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class AnaliseFinanceiraControllerTest {

  @Mock private AnaliseFinanceiraService service;

  @InjectMocks private AnaliseFinanceiraController controller;

  private AnaliseFinanceiraDTO analise1;
  private AnaliseFinanceiraDTO analise2;
  private LancamentoGraficoDTO graficoDTO;
  private Dataset dataset;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    analise1 = new AnaliseFinanceiraDTO();
    analise1.setId(1L);
    analise1.setDescricao("Analise 1");

    analise2 = new AnaliseFinanceiraDTO();
    analise2.setId(2L);
    analise2.setDescricao("Analise 2");

    dataset =
        new Dataset(
            "Conta 1", Arrays.asList(BigDecimal.valueOf(100), BigDecimal.valueOf(200)), "blue");
    graficoDTO =
        new LancamentoGraficoDTO(Arrays.asList("Nov/2025", "Nov/2025"), Arrays.asList(dataset));
  }

  @Test
  void listarTodos_deveRetornarLista() {
    when(service.listarTodos()).thenReturn(Arrays.asList(analise1, analise2));

    List<AnaliseFinanceiraDTO> result = controller.listarTodos();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(service, times(1)).listarTodos();
  }

  @Test
  void buscarPorId_deveRetornarAnalise_quandoExiste()
      throws AnaliseFinanceiraNaoEncontradaException {
    when(service.buscarPorId(1L)).thenReturn(analise1);

    ResponseEntity<AnaliseFinanceiraDTO> response = controller.buscarPorId(1L);

    assertEquals(analise1, response.getBody());
    assertEquals(200, response.getStatusCodeValue());
    verify(service, times(1)).buscarPorId(1L);
  }

  @Test
  void buscarPorId_deveRetornarNotFound_quandoNaoExiste()
      throws AnaliseFinanceiraNaoEncontradaException {
    when(service.buscarPorId(1L)).thenThrow(new AnaliseFinanceiraNaoEncontradaException());

    ResponseEntity<AnaliseFinanceiraDTO> response = controller.buscarPorId(1L);

    assertEquals(404, response.getStatusCodeValue());
    verify(service, times(1)).buscarPorId(1L);
  }

  @Test
  void criar_deveRetornarAnaliseCriada() {
    when(service.criar(analise1)).thenReturn(analise1);

    AnaliseFinanceiraDTO result = controller.criar(analise1);

    assertEquals(analise1, result);
    verify(service, times(1)).criar(analise1);
  }

  @Test
  void atualizar_deveRetornarAnaliseAtualizada_quandoExiste()
      throws AnaliseFinanceiraNaoEncontradaException {
    when(service.atualizar(1L, analise1)).thenReturn(analise1);

    ResponseEntity<AnaliseFinanceiraDTO> response = controller.atualizar(1L, analise1);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(analise1, response.getBody());
    verify(service, times(1)).atualizar(1L, analise1);
  }

  @Test
  void atualizar_deveRetornarNotFound_quandoNaoExiste()
      throws AnaliseFinanceiraNaoEncontradaException {
    when(service.atualizar(1L, analise1)).thenThrow(new AnaliseFinanceiraNaoEncontradaException());

    ResponseEntity<AnaliseFinanceiraDTO> response = controller.atualizar(1L, analise1);

    assertEquals(404, response.getStatusCodeValue());
    verify(service, times(1)).atualizar(1L, analise1);
  }

  @Test
  void deletar_deveChamarServico() {
    doNothing().when(service).deletar(1L);

    ResponseEntity<Void> response = controller.deletar(1L);

    assertEquals(204, response.getStatusCodeValue());
    verify(service, times(1)).deletar(1L);
  }

  @Test
  void gerarInsights_deveRetornarListaDeMaps() {
    Map<String, Object> insight = new HashMap<>();
    insight.put("key", "valor");
    when(service.gerarInsights(graficoDTO)).thenReturn(Arrays.asList(insight));

    List<Map<String, Object>> result = controller.gerarInsights(graficoDTO);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(insight, result.get(0));
    verify(service, times(1)).gerarInsights(graficoDTO);
  }
}
