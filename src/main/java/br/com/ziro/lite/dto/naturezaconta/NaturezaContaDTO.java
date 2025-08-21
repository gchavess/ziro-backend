package br.com.ziro.lite.dto.naturezaconta;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.naturezaconta.NaturezaConta;
import jakarta.persistence.*;
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
  private UsuarioDTO usuarioCriacao;
  private ContextoContaDTO contextoConta;

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
}
