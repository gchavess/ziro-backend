package br.com.ziro.lite.dto.contextoconta;

import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

  private Boolean padrao;

  private Long contextoContaPadraoId;

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

  public ContextoContaDTO(
      Long contextoContaPadraoId,
      Boolean padrao,
      String descricao,
      String observacao,
      String codigo) {
    this.contextoContaPadraoId = contextoContaPadraoId;
    this.descricao = descricao;
    this.observacao = observacao;
    this.codigo = codigo;
    this.padrao = padrao;
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

  public static ContextoContaDTO inicializarValoresPadroes(ContextoConta padrao) {
    return new ContextoContaDTO(
        padrao.getId(), false, padrao.getDescricao(), padrao.getObservacao(), padrao.getCodigo());
  }
}
