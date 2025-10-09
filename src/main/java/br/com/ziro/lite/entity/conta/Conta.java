package br.com.ziro.lite.entity.conta;

import br.com.ziro.lite.entity.naturezaconta.NaturezaConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "conta", schema = "public")
@Getter
@Setter
public class Conta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "data_criacao", nullable = false)
  private Date dataCriacao;

  private String descricao;

  private String observacao;

  @Column(nullable = false)
  private String codigo;

  @ManyToOne
  @JoinColumn(name = "usuario_criacao_id", nullable = false)
  private Usuario usuarioCriacao;

  @ManyToOne
  @JoinColumn(name = "pai_id")
  private Conta pai;

  @OneToMany(mappedBy = "pai")
  private List<Conta> filhos;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "natureza_conta_id", nullable = false)
  private NaturezaConta naturezaConta;
}
