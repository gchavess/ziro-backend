package br.com.ziro.lite.entity.contextoconta;

import br.com.ziro.lite.entity.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contexto_conta")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ContextoConta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String descricao;

  private String observacao;

  @Column(nullable = false)
  private String codigo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "usuario_criacao_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_contexto_conta_usuario_criacao"))
  private Usuario usuarioCriacao;

  private Date dataCriacao;
}
