package br.com.ziro.lite.dto.lancamento;

import br.com.ziro.lite.entity.lancamento.Lancamento;
import br.com.ziro.lite.util.date.DateFormatterUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

public record LancamentoGraficoCelulaDTO(
    String contaDescricao,
    BigDecimal valorBruto,
    String dataPagamento,
    String dataVencimento,
    String contextoDescricao,
    Long contextoId,
    String naturezaDescricao,
    Long naturezaId) {

  public static LancamentoGraficoCelulaDTO fromEntity(Lancamento l) {
    String dataPagamento = null;

    if (l.getDataPagamento() != null) {
      LocalDate localDate;

      if (l.getDataPagamento() instanceof java.sql.Date sqlDate) {
        localDate = sqlDate.toLocalDate();
      } else {
        localDate = l.getDataPagamento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      }

      dataPagamento = localDate.format(DateFormatterUtil.DIA_MES_ANO);
    }

    String dataVencimento = null;

    if (l.getDataVencimento() != null) {
      LocalDate localDate;

      if (l.getDataVencimento() instanceof java.sql.Date sqlDate) {
        localDate = sqlDate.toLocalDate();
      } else {
        localDate = l.getDataVencimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      }

      dataVencimento = localDate.format(DateFormatterUtil.DIA_MES_ANO);
    }

    String contextoDescricao = null;
    Long contextoId = null;
    String naturezaDescricao = null;
    Long naturezaId = null;

    if (l.getConta() != null && l.getConta().getNaturezaConta() != null) {
      naturezaDescricao = l.getConta().getNaturezaConta().getDescricao();
      naturezaId = l.getConta().getNaturezaConta().getId();

      if (l.getConta().getNaturezaConta().getContextoConta() != null) {
        contextoDescricao = l.getConta().getNaturezaConta().getContextoConta().getDescricao();
        contextoId = l.getConta().getNaturezaConta().getContextoConta().getId();
      }
    }

    return new LancamentoGraficoCelulaDTO(
        l.getConta() != null ? l.getConta().getDescricao() : null,
        l.getValorBruto(),
        dataPagamento,
        dataVencimento,
        contextoDescricao,
        contextoId,
        naturezaDescricao,
        naturezaId);
  }
}
