package br.com.ziro.lite.entity.naturezaconta;

import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "natureza_conta")
@Getter
@Setter
public class NaturezaConta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "data_criacao", nullable = false, updatable = false)
  private Date dataCriacao;

  @Column(nullable = false)
  private String descricao;

  private String observacao;

  private Long naturezaContaPadraoId;

  @Column(nullable = false, unique = true)
  private String codigo;

  private Boolean padrao;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "usuario_criacao_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_natureza_conta_usuario_criacao"))
  private Usuario usuarioCriacao;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "contexto_conta_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_natureza_conta_contexto_conta"))
  private ContextoConta contextoConta;
}
