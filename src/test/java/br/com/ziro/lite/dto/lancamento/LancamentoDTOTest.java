package br.com.ziro.lite.dto.lancamento;

import static org.junit.jupiter.api.Assertions.*;

import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.lancamento.Lancamento;
import br.com.ziro.lite.entity.usuario.Usuario;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class LancamentoDTOTest {

  private Lancamento criarLancamentoCompleto() {
    Lancamento l = new Lancamento();

    l.setId(10L);
    l.setDataCriacao(new Date());
    l.setDescricao("Desc");
    l.setObservacao("Obs");
    l.setDataVencimento(new Date());
    l.setDataPagamento(new Date());
    l.setValorBruto(new BigDecimal("123.45"));

    // Usuario criacao do lancamento
    Usuario u = new Usuario();
    u.setId(99L);
    l.setUsuarioCriacao(u);

    // Conta
    Conta c = new Conta();
    c.setCodigo("ABC123");
    c.setDescricao("Conta X");

    // Conta precisa de usuarioCriacao
    Usuario usuarioConta = new Usuario();
    usuarioConta.setId(777L);
    c.setUsuarioCriacao(usuarioConta);

    l.setConta(c);

    return l;
  }

  @Test
  void deveMapearTodosOsCamposQuandoTudoExiste() {
    Lancamento entity = criarLancamentoCompleto();

    LancamentoDTO dto = LancamentoDTO.fromEntity(entity);

    assertNotNull(dto);
    assertEquals(10L, dto.getId());
    assertEquals(entity.getDataCriacao(), dto.getDataCriacao());
    assertEquals("Desc", dto.getDescricao());
    assertEquals("Obs", dto.getObservacao());
    assertEquals(entity.getDataVencimento(), dto.getDataVencimento());
    assertEquals(entity.getDataPagamento(), dto.getDataPagamento());
    assertEquals(new BigDecimal("123.45"), dto.getValorBruto());

    // Conta
    assertNotNull(dto.getConta());
    assertEquals("ABC123", dto.getContaCodigo());
    assertEquals("Conta X", dto.getConta().getDescricao());

    // Usuario
    assertEquals(99L, dto.getUsuarioCriacaoId());
  }

  @Test
  void deveRetornarNullParaContaQuandoEntityNaoTemConta() {
    Lancamento l = criarLancamentoCompleto();
    l.setConta(null);

    LancamentoDTO dto = LancamentoDTO.fromEntity(l);

    assertNull(dto.getConta());
    assertNull(dto.getContaCodigo());
  }

  @Test
  void deveRetornarNullParaUsuarioQuandoNaoExisteUsuarioCriacao() {
    Lancamento l = criarLancamentoCompleto();
    l.setUsuarioCriacao(null);

    LancamentoDTO dto = LancamentoDTO.fromEntity(l);

    assertNull(dto.getUsuarioCriacaoId());
  }

  @Test
  void devePermitirCamposOpcionaisNulosSemLancarErro() {
    Lancamento l = criarLancamentoCompleto();

    l.setObservacao(null);
    l.setDataVencimento(null);
    l.setDataPagamento(null);

    LancamentoDTO dto = LancamentoDTO.fromEntity(l);

    assertNull(dto.getObservacao());
    assertNull(dto.getDataVencimento());
    assertNull(dto.getDataPagamento());
  }
}
