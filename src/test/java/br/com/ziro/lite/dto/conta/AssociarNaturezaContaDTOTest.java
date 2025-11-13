package br.com.ziro.lite.dto.conta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;

class AssociarNaturezaContaDTOTest {

  @Test
  void deveCriarDtoComBuilder() {
    AssociarNaturezaContaDTO dto =
        AssociarNaturezaContaDTO.builder().contas(List.of(1L, 2L, 3L)).naturezaId(10L).build();

    assertNotNull(dto);
    assertEquals(List.of(1L, 2L, 3L), dto.getContas());
    assertEquals(10L, dto.getNaturezaId());
  }

  @Test
  void devePermitirSetters() {
    AssociarNaturezaContaDTO dto = new AssociarNaturezaContaDTO();
    dto.setContas(List.of(5L, 8L));
    dto.setNaturezaId(20L);

    assertEquals(List.of(5L, 8L), dto.getContas());
    assertEquals(20L, dto.getNaturezaId());
  }

  @Test
  void deveCriarComConstrutorCompleto() {
    AssociarNaturezaContaDTO dto = new AssociarNaturezaContaDTO(List.of(99L), 7L);

    assertEquals(List.of(99L), dto.getContas());
    assertEquals(7L, dto.getNaturezaId());
  }
}
