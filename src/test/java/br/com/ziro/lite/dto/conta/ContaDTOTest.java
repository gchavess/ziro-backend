package br.com.ziro.lite.dto.conta;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.entity.naturezaconta.NaturezaConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import java.util.Date;
import org.junit.jupiter.api.Test;

class ContaDTOTest {

  @Test
  void deveCriarContaDTOAPartirDaEntity() {
    // ARRANGE
    Conta conta = mock(Conta.class);
    Usuario usuario = mock(Usuario.class);
    Conta pai = mock(Conta.class);
    NaturezaConta natureza = mock(NaturezaConta.class);
    ContextoConta contexto = mock(ContextoConta.class);

    when(conta.getId()).thenReturn(1L);
    Date dataCriacao = new Date();
    when(conta.getDataCriacao()).thenReturn(dataCriacao);
    when(conta.getDescricao()).thenReturn("Conta Principal");
    when(conta.getObservacao()).thenReturn("Observação teste");
    when(conta.getCodigo()).thenReturn("123");

    when(usuario.getId()).thenReturn(50L);
    when(conta.getUsuarioCriacao()).thenReturn(usuario);

    when(pai.getId()).thenReturn(10L);
    when(pai.getCodigo()).thenReturn("PAI001");
    when(conta.getPai()).thenReturn(pai);

    // Natureza + Contexto
    when(conta.getNaturezaConta()).thenReturn(natureza);
    when(natureza.getId()).thenReturn(99L);
    when(natureza.getDescricao()).thenReturn("Natureza Teste");

    // 🎯 IMPORTANTE: Adicionando o contexto para NÃO dar NPE
    when(natureza.getContextoConta()).thenReturn(contexto);
    when(contexto.getId()).thenReturn(7L);
    when(contexto.getDescricao()).thenReturn("Contexto XPTO");

    // ACT
    ContaDTO dto = ContaDTO.fromEntity(conta);

    // ASSERT
    assertNotNull(dto);
    assertEquals(1L, dto.getId());
    assertEquals(dataCriacao, dto.getDataCriacao());
    assertEquals("Conta Principal", dto.getDescricao());
    assertEquals("Observação teste", dto.getObservacao());
    assertEquals("123", dto.getCodigo());
    assertEquals(50L, dto.getUsuarioCriacaoId());
    assertEquals(10L, dto.getPaiId());
    assertEquals("PAI001", dto.getPaiCodigo());

    assertNotNull(dto.getNaturezaConta());
    assertEquals(99L, dto.getNaturezaConta().getId());
    assertEquals("Natureza Teste", dto.getNaturezaConta().getDescricao());

    assertNotNull(dto.getNaturezaConta().getContextoConta());
    assertEquals(7L, dto.getNaturezaConta().getContextoConta().getId());
    assertEquals("Contexto XPTO", dto.getNaturezaConta().getContextoConta().getDescricao());
  }

  @Test
  void deveCriarContaDTOMesmoSemPaiENatureza() {
    // ARRANGE
    Conta conta = mock(Conta.class);
    Usuario usuario = mock(Usuario.class);

    when(conta.getId()).thenReturn(2L);
    Date dataCriacao = new Date();
    when(conta.getDataCriacao()).thenReturn(dataCriacao);
    when(conta.getDescricao()).thenReturn("Conta Sem Pai");
    when(conta.getObservacao()).thenReturn(null);
    when(conta.getCodigo()).thenReturn("XYZ");

    when(usuario.getId()).thenReturn(77L);
    when(conta.getUsuarioCriacao()).thenReturn(usuario);

    // Sem pai
    when(conta.getPai()).thenReturn(null);

    // Sem natureza
    when(conta.getNaturezaConta()).thenReturn(null);

    // ACT
    ContaDTO dto = ContaDTO.fromEntity(conta);

    // ASSERT
    assertNotNull(dto);
    assertEquals(2L, dto.getId());
    assertEquals("Conta Sem Pai", dto.getDescricao());
    assertEquals(77L, dto.getUsuarioCriacaoId());
    assertNull(dto.getPaiId());
    assertNull(dto.getPaiCodigo());
    assertNull(dto.getNaturezaConta());
  }
}
