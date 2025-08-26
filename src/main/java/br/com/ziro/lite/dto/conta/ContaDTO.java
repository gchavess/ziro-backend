package br.com.ziro.lite.dto.conta;

import br.com.ziro.lite.dto.naturezaconta.NaturezaContaDTO;
import br.com.ziro.lite.entity.conta.Conta;
import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaDTO {

  private Long id;
  private Date dataCriacao;
  private String descricao;
  private String observacao;
  private String codigo;
  private Long usuarioCriacaoId;
  private Long paiId;
  private NaturezaContaDTO naturezaConta;

  public static ContaDTO fromEntity(Conta entity) {
    return ContaDTO.builder()
        .id(entity.getId())
        .dataCriacao(entity.getDataCriacao())
        .descricao(entity.getDescricao())
        .observacao(entity.getObservacao())
        .codigo(entity.getCodigo())
        .usuarioCriacaoId(entity.getUsuarioCriacao().getId())
        .paiId(entity.getPai() != null ? entity.getPai().getId() : null)
        .naturezaConta(
            entity.getNaturezaConta() != null
                ? NaturezaContaDTO.fromEntity(entity.getNaturezaConta())
                : null)
        .build();
  }
}
