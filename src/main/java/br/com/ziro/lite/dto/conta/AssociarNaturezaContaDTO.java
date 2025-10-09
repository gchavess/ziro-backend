package br.com.ziro.lite.dto.conta;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssociarNaturezaContaDTO {

  private List<Long> contas;
  private Long naturezaId;
}
