package br.com.ziro.lite.dto.conta;

import static org.junit.jupiter.api.Assertions.*;

import br.com.ziro.lite.dto.naturezaconta.NaturezaContaDTO;
import java.util.List;
import org.junit.jupiter.api.Test;

class ContaTreeNodeDTOTest {

  @Test
  void deveCriarComBuilderCorretamente() {
    // criar NaturezaContaDTO sem builder (usa setters)
    NaturezaContaDTO natureza = new NaturezaContaDTO();
    natureza.setId(10L);
    natureza.setDescricao("Natureza Teste");
    natureza.setCodigo("NAT001");
    natureza.setObservacao("obs nat");

    ContaTreeNodeDTO child =
        ContaTreeNodeDTO.builder()
            .id(2L)
            .label("Filha")
            .codigo("002")
            .descricao("Conta filha")
            .build();

    ContaTreeNodeDTO dto =
        ContaTreeNodeDTO.builder()
            .id(1L)
            .label("Principal")
            .expanded(true)
            .natureza(natureza)
            .children(List.of(child))
            .codigo("001")
            .descricao("Conta principal")
            .observacao("obs")
            .build();

    assertEquals(1L, dto.getId());
    assertEquals("Principal", dto.getLabel());
    assertTrue(Boolean.TRUE.equals(dto.getExpanded()));
    assertEquals("001", dto.getCodigo());
    assertEquals("Conta principal", dto.getDescricao());
    assertEquals("obs", dto.getObservacao());

    assertNotNull(dto.getNatureza());
    assertEquals(10L, dto.getNatureza().getId());
    assertEquals("Natureza Teste", dto.getNatureza().getDescricao());

    assertNotNull(dto.getChildren());
    assertEquals(1, dto.getChildren().size());
    assertEquals(2L, dto.getChildren().get(0).getId());
    assertEquals("Filha", dto.getChildren().get(0).getLabel());
  }

  @Test
  void deveUsarGettersESettersCorretamente() {
    ContaTreeNodeDTO dto = new ContaTreeNodeDTO();
    dto.setId(5L);
    dto.setLabel("Node");
    dto.setExpanded(false);
    dto.setCodigo("XYZ");
    dto.setDescricao("Teste");
    dto.setObservacao("Obs aqui");

    assertEquals(5L, dto.getId());
    assertEquals("Node", dto.getLabel());
    assertFalse(Boolean.TRUE.equals(dto.getExpanded()));
    assertEquals("XYZ", dto.getCodigo());
    assertEquals("Teste", dto.getDescricao());
    assertEquals("Obs aqui", dto.getObservacao());
  }

  @Test
  void deveCriarComConstrutorCompleto() {
    NaturezaContaDTO natureza = new NaturezaContaDTO();
    natureza.setId(99L);
    natureza.setDescricao("Natureza X");
    natureza.setCodigo("NX");

    ContaTreeNodeDTO filho = ContaTreeNodeDTO.builder().id(22L).label("Filho").build();

    ContaTreeNodeDTO dto =
        new ContaTreeNodeDTO(
            11L, "Pai", true, natureza, List.of(filho), "C001", "Conta Pai", "observacao");

    assertEquals(11L, dto.getId());
    assertEquals("Pai", dto.getLabel());
    assertTrue(Boolean.TRUE.equals(dto.getExpanded()));
    assertEquals("C001", dto.getCodigo());
    assertEquals("Conta Pai", dto.getDescricao());
    assertEquals("observacao", dto.getObservacao());

    assertNotNull(dto.getNatureza());
    assertEquals(99L, dto.getNatureza().getId());

    assertEquals(1, dto.getChildren().size());
    assertEquals(22L, dto.getChildren().get(0).getId());
  }
}
