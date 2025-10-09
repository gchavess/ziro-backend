package br.com.ziro.lite.entity.lancamento;

import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.usuario.Usuario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lancamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "data_criacao", nullable = false, updatable = false)
  private Date dataCriacao;

  @Column(nullable = false)
  private String descricao;

  private String observacao;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_criacao_id", nullable = false)
  private Usuario usuarioCriacao;

  @Temporal(TemporalType.DATE)
  @Column(name = "data_vencimento")
  private Date dataVencimento;

  @Temporal(TemporalType.DATE)
  @Column(name = "data_pagamento")
  private Date dataPagamento;

  @Column(name = "valor_bruto", nullable = false, precision = 15, scale = 2)
  private BigDecimal valorBruto;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "conta_id", nullable = false)
  private Conta conta;
}
