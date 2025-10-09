package br.com.ziro.lite.mapper.csv;

import br.com.ziro.lite.dto.conta.ContaDTO;
import br.com.ziro.lite.dto.lancamento.LancamentoDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CsvMapper {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]");
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static ContaDTO mapToContaDTO(String[] csvLine) {
    ContaDTO c = new ContaDTO();

    c.setDescricao(csvLine[1]);
    c.setObservacao(csvLine[2]);
    c.setCodigo(csvLine[3]);
    c.setPaiCodigo(csvLine[7].trim().isEmpty() ? null : csvLine[7]);

    return c;
  }

  public static LancamentoDTO mapToLancamentoDTO(String[] csvLine) {
    LancamentoDTO l = new LancamentoDTO();

    l.setDescricao(csvLine[1]);
    l.setObservacao(csvLine[2]);
    l.setDataVencimento(
        csvLine[4].trim().isEmpty()
            ? null
            : Date.from(
                LocalDate.parse(csvLine[4], DATE_FORMATTER)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()));

    l.setDataPagamento(
        csvLine[5].trim().isEmpty()
            ? null
            : Date.from(
                LocalDate.parse(csvLine[5], DATE_FORMATTER)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()));
    l.setValorBruto(csvLine[6].trim().isEmpty() ? null : new BigDecimal(csvLine[6].trim()));
    l.setContaCodigo(csvLine[7].trim().isEmpty() ? null : csvLine[7]);
    return l;
  }
}
