package br.com.ziro.lite.dto.conta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import org.junit.jupiter.api.Test;

class ContaOptionDTOTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    ContaOptionDTO dto = new ContaOptionDTO();
    dto.setLabel("Conta");
    dto.setValue("123");
    dto.setChildren(Collections.emptyList());

    assertEquals("Conta", dto.getLabel());
    assertEquals("123", dto.getValue());
    assertEquals(0, dto.getChildren().size());
  }

  @Test
  void testAllArgsConstructor() {
    ContaOptionDTO child = new ContaOptionDTO("Child", "2", null);
    ContaOptionDTO dto = new ContaOptionDTO("Pai", "1", Collections.singletonList(child));

    assertEquals("Pai", dto.getLabel());
    assertEquals("1", dto.getValue());
    assertEquals(1, dto.getChildren().size());
    assertEquals("Child", dto.getChildren().get(0).getLabel());
  }

  @Test
  void testBuilder() {
    ContaOptionDTO dto =
        ContaOptionDTO.builder()
            .label("Test Label")
            .value("999")
            .children(Collections.emptyList())
            .build();

    assertEquals("Test Label", dto.getLabel());
    assertEquals("999", dto.getValue());
    assertTrue(dto.getChildren().isEmpty());
  }

  @Test
  void testEqualsAndHashCode() {
    ContaOptionDTO d1 = new ContaOptionDTO("A", "1", null);
    ContaOptionDTO d2 = new ContaOptionDTO("A", "1", null);

    assertEquals(d1, d2);
    assertEquals(d1.hashCode(), d2.hashCode());
  }

  @Test
  void testToString() {
    ContaOptionDTO dto = new ContaOptionDTO("Teste", "5", null);

    String str = dto.toString();
    assertTrue(str.contains("Teste"));
    assertTrue(str.contains("5"));
  }
}
