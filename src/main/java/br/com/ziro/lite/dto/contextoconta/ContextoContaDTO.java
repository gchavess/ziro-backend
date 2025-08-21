package br.com.ziro.lite.dto.contextoconta;

import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContextoContaDTO {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String descricao;

  private String observacao;

  private String codigo;

  private UsuarioDTO usuarioCriacao;

  private Date dataCriacao;

  public ContextoContaDTO() {}

  public ContextoContaDTO(
      Long id,
      String descricao,
      String observacao,
      String codigo,
      UsuarioDTO usuarioCriacao,
      Date dataCriacao) {
    this.id = id;
    this.descricao = descricao;
    this.observacao = observacao;
    this.codigo = codigo;
    this.usuarioCriacao = usuarioCriacao;
    this.dataCriacao = dataCriacao;
  }

  public static ContextoContaDTO fromEntity(ContextoConta contexto) {
    return new ContextoContaDTO(
        contexto.getId(),
        contexto.getDescricao(),
        contexto.getObservacao(),
        contexto.getCodigo(),
        UsuarioDTO.fromEntity(contexto.getUsuarioCriacao()),
        contexto.getDataCriacao());
  }
}
