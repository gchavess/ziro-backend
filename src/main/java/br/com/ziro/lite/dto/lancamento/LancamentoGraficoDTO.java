package br.com.ziro.lite.dto.lancamento;

import java.math.BigDecimal;
import java.util.List;

public record LancamentoGraficoDTO(
    List<String> months, // eixo X = meses
    List<Dataset> datasets // uma linha por conta
    ) {

  public record Dataset(
      String label, // nome da conta
      List<BigDecimal> data, // valores por mês
      String backgroundColor // cor da linha
      ) {}
}
