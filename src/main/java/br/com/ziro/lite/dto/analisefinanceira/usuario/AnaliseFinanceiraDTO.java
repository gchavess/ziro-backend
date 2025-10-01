package br.com.ziro.lite.dto.analisefinanceira.usuario;

import br.com.ziro.lite.entity.analisefinanceira.AnaliseFinanceira;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnaliseFinanceiraDTO {

  private Long id;
  private Date dataCriacao;
  private String descricao;
  private String observacao;
  private String codigo;
  private String fato;
  private String causa;
  private String acao;

  public AnaliseFinanceiraDTO() {}

  public AnaliseFinanceiraDTO(
      Long id,
      Date dataCriacao,
      String descricao,
      String observacao,
      String codigo,
      String fato,
      String causa,
      String acao) {
    this.id = id;
    this.dataCriacao = dataCriacao;
    this.descricao = descricao;
    this.observacao = observacao;
    this.codigo = codigo;
    this.fato = fato;
    this.causa = causa;
    this.acao = acao;
  }

  public static AnaliseFinanceiraDTO fromEntity(AnaliseFinanceira analiseFinanceira) {
    if (analiseFinanceira == null) return null;
    return new AnaliseFinanceiraDTO(
        analiseFinanceira.getId(),
        analiseFinanceira.getDataCriacao(),
        analiseFinanceira.getDescricao(),
        analiseFinanceira.getObservacao(),
        analiseFinanceira.getCodigo(),
        analiseFinanceira.getFato(),
        analiseFinanceira.getCausa(),
        analiseFinanceira.getAcao());
  }
}
