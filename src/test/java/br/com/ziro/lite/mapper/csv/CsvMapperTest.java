package br.com.ziro.lite.mapper.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.ziro.lite.dto.conta.ContaDTO;
import br.com.ziro.lite.dto.lancamento.LancamentoDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class CsvMapperTest {

  @Test
  void mapToContaDTO_deveMapearCorretamente() {
    String[] csv =
        new String[] {"0", "Descrição da Conta", "Observação teste", "C123", "", "", "", "PAI001"};

    ContaDTO dto = CsvMapper.mapToContaDTO(csv);

    assertEquals("Descrição da Conta", dto.getDescricao());
    assertEquals("Observação teste", dto.getObservacao());
    assertEquals("C123", dto.getCodigo());
    assertEquals("PAI001", dto.getPaiCodigo());
  }

  @Test
  void mapToContaDTO_deveRetornarPaiCodigoNullQuandoVazio() {
    String[] csv = new String[] {"0", "Desc", "Obs", "C123", "", "", "", "   "};

    ContaDTO dto = CsvMapper.mapToContaDTO(csv);

    assertNull(dto.getPaiCodigo());
  }

  @Test
  void mapToLancamentoDTO_deveMapearCorretamente() {
    String[] csv =
        new String[] {
          "0",
          "Descrição Lançamento",
          "Obs Lançamento",
          "",
          "2025-01-10",
          "2025-01-12",
          "250.99",
          "CC100"
        };

    LancamentoDTO dto = CsvMapper.mapToLancamentoDTO(csv);

    assertEquals("Descrição Lançamento", dto.getDescricao());
    assertEquals("Obs Lançamento", dto.getObservacao());

    LocalDate venc = LocalDate.of(2025, 1, 10);
    LocalDate pag = LocalDate.of(2025, 1, 12);

    Date expectedVenc = Date.from(venc.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date expectedPag = Date.from(pag.atStartOfDay(ZoneId.systemDefault()).toInstant());

    assertEquals(expectedVenc, dto.getDataVencimento());
    assertEquals(expectedPag, dto.getDataPagamento());

    assertEquals(new BigDecimal("250.99"), dto.getValorBruto());
    assertEquals("CC100", dto.getContaCodigo());
  }

  @Test
  void mapToLancamentoDTO_deveRetornarNullParaCamposVazios() {
    String[] csv = new String[] {"0", "Desc", "Obs", "", "   ", "", "   ", " "};

    LancamentoDTO dto = CsvMapper.mapToLancamentoDTO(csv);

    assertNull(dto.getDataVencimento());
    assertNull(dto.getDataPagamento());
    assertNull(dto.getValorBruto());
    assertNull(dto.getContaCodigo());
  }
}
