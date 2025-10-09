package br.com.ziro.lite.dto.lancamento;

import br.com.ziro.lite.dto.conta.ContaDTO;
import br.com.ziro.lite.entity.lancamento.Lancamento;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoDTO {

  private Long id;
  private Date dataCriacao;
  private String descricao;
  private String observacao;
  private Date dataVencimento;
  private Date dataPagamento;
  private BigDecimal valorBruto;
  private ContaDTO conta;
  private String contaCodigo;
  private Long usuarioCriacaoId;

  public static LancamentoDTO fromEntity(Lancamento entity) {
    return LancamentoDTO.builder()
        .id(entity.getId())
        .dataCriacao(entity.getDataCriacao())
        .descricao(entity.getDescricao())
        .observacao(entity.getObservacao())
        .dataVencimento(entity.getDataVencimento())
        .dataPagamento(entity.getDataPagamento())
        .valorBruto(entity.getValorBruto())
        .conta(entity.getConta() != null ? ContaDTO.fromEntity(entity.getConta()) : null)
        .contaCodigo(entity.getConta() != null ? entity.getConta().getCodigo() : null)
        .usuarioCriacaoId(
            entity.getUsuarioCriacao() != null ? entity.getUsuarioCriacao().getId() : null)
        .build();
  }
}
