package br.com.ziro.lite.dto.naturezaconta;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.naturezaconta.NaturezaConta;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaturezaContaDTO {

  private Long id;
  private Date dataCriacao;
  private String descricao;
  private String observacao;
  private String codigo;
  private Boolean padrao;
  private Long naturezaContaPadraoId;
  private UsuarioDTO usuarioCriacao;
  private ContextoContaDTO contextoConta;

  public NaturezaContaDTO() {}

  public NaturezaContaDTO(
      Long naturezaContaPadraoId,
      final Boolean padrao,
      String descricao,
      String observacao,
      String codigo,
      ContextoContaDTO contextoConta) {
    this.naturezaContaPadraoId = naturezaContaPadraoId;
    this.descricao = descricao;
    this.observacao = observacao;
    this.codigo = codigo;
    this.contextoConta = contextoConta;
    this.padrao = padrao;
  }

  public static NaturezaContaDTO fromEntity(NaturezaConta entity) {
    NaturezaContaDTO dto = new NaturezaContaDTO();
    dto.setId(entity.getId());
    dto.setDescricao(entity.getDescricao());
    dto.setObservacao(entity.getObservacao());
    dto.setCodigo(entity.getCodigo());
    dto.setDataCriacao(entity.getDataCriacao());
    dto.setUsuarioCriacao(UsuarioDTO.fromEntity(entity.getUsuarioCriacao()));
    dto.setContextoConta(ContextoContaDTO.fromEntity(entity.getContextoConta()));
    return dto;
  }

  public static NaturezaContaDTO inicializarValoresPadroes(NaturezaConta padrao) {
    return new NaturezaContaDTO(
        padrao.getId(),
        false,
        padrao.getDescricao(),
        padrao.getObservacao(),
        padrao.getCodigo(),
        ContextoContaDTO.fromEntity(padrao.getContextoConta()));
  }
}
