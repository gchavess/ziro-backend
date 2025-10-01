package br.com.ziro.lite.entity.analisefinanceira;

import br.com.ziro.lite.entity.usuario.Usuario;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "analise_financeira", schema = "public")
@Getter
@Setter
public class AnaliseFinanceira {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "data_criacao", nullable = false)
  private Date dataCriacao;

  private String descricao;

  private String observacao;

  private String codigo;

  @ManyToOne
  @JoinColumn(name = "usuario_criacao_id", nullable = false)
  private Usuario usuarioCriacao;

  private String fato;

  private String causa;

  private String acao;
}
