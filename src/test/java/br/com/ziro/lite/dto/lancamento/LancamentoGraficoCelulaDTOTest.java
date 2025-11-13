package br.com.ziro.lite.dto.lancamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.entity.lancamento.Lancamento;
import br.com.ziro.lite.entity.naturezaconta.NaturezaConta;
import br.com.ziro.lite.util.date.DateFormatterUtil;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

public class LancamentoGraficoCelulaDTOTest {

  private Lancamento criarLancamentoBase() {
    Lancamento l = new Lancamento();
    l.setValorBruto(new BigDecimal("123.45"));
    return l;
  }

  @Test
  void deveMapearDatasComUtilDate() {
    Lancamento l = criarLancamentoBase();

    LocalDate venc = LocalDate.of(2025, 1, 10);
    LocalDate pag = LocalDate.of(2025, 1, 12);

    l.setDataVencimento(java.util.Date.from(venc.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    l.setDataPagamento(java.util.Date.from(pag.atStartOfDay(ZoneId.systemDefault()).toInstant()));

    LancamentoGraficoCelulaDTO dto = LancamentoGraficoCelulaDTO.fromEntity(l);

    assertEquals(venc.format(DateFormatterUtil.DIA_MES_ANO), dto.dataVencimento());
    assertEquals(pag.format(DateFormatterUtil.DIA_MES_ANO), dto.dataPagamento());
  }

  @Test
  void deveMapearDatasComSqlDate() {
    Lancamento l = criarLancamentoBase();

    LocalDate venc = LocalDate.of(2025, 2, 20);
    LocalDate pag = LocalDate.of(2025, 2, 22);

    l.setDataVencimento(Date.valueOf(venc));
    l.setDataPagamento(Date.valueOf(pag));

    LancamentoGraficoCelulaDTO dto = LancamentoGraficoCelulaDTO.fromEntity(l);

    assertEquals(venc.format(DateFormatterUtil.DIA_MES_ANO), dto.dataVencimento());
    assertEquals(pag.format(DateFormatterUtil.DIA_MES_ANO), dto.dataPagamento());
  }

  @Test
  void deveRetornarNullParaDatasVazias() {
    Lancamento l = criarLancamentoBase();

    l.setDataPagamento(null);
    l.setDataVencimento(null);

    LancamentoGraficoCelulaDTO dto = LancamentoGraficoCelulaDTO.fromEntity(l);

    assertNull(dto.dataPagamento());
    assertNull(dto.dataVencimento());
  }

  @Test
  void deveMapearContaNaturezaEContexto() {
    Lancamento l = criarLancamentoBase();

    // CONTEXTO
    ContextoConta ctx = new ContextoConta();
    ctx.setId(10L);
    ctx.setDescricao("Contexto X");

    // NATUREZA
    NaturezaConta nat = new NaturezaConta();
    nat.setId(20L);
    nat.setDescricao("Natureza Y");
    nat.setContextoConta(ctx);

    // CONTA
    Conta conta = new Conta();
    conta.setDescricao("Conta Z");
    conta.setNaturezaConta(nat);

    l.setConta(conta);

    LancamentoGraficoCelulaDTO dto = LancamentoGraficoCelulaDTO.fromEntity(l);

    assertEquals("Conta Z", dto.contaDescricao());
    assertEquals("Natureza Y", dto.naturezaDescricao());
    assertEquals(20L, dto.naturezaId());
    assertEquals("Contexto X", dto.contextoDescricao());
    assertEquals(10L, dto.contextoId());
  }

  @Test
  void deveLidarQuandoNaturezaNula() {
    Lancamento l = criarLancamentoBase();

    Conta conta = new Conta();
    conta.setDescricao("Conta Sem Natureza");

    l.setConta(conta);

    LancamentoGraficoCelulaDTO dto = LancamentoGraficoCelulaDTO.fromEntity(l);

    assertEquals("Conta Sem Natureza", dto.contaDescricao());
    assertNull(dto.contextoDescricao());
    assertNull(dto.contextoId());
    assertNull(dto.naturezaDescricao());
    assertNull(dto.naturezaId());
  }

  @Test
  void deveLidarQuandoContaNula() {
    Lancamento l = criarLancamentoBase();
    l.setConta(null);

    LancamentoGraficoCelulaDTO dto = LancamentoGraficoCelulaDTO.fromEntity(l);

    assertNull(dto.contaDescricao());
    assertNull(dto.contextoDescricao());
    assertNull(dto.contextoId());
    assertNull(dto.naturezaDescricao());
    assertNull(dto.naturezaId());
  }
}
