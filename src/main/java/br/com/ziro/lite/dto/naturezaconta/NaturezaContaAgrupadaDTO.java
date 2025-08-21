package br.com.ziro.lite.dto.naturezaconta;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NaturezaContaAgrupadaDTO {
  private Long id;
  private String label;
  private List<NaturezaContaAgrupadaDTO> children;
  private String codigo;
  private String descricao;
  private String observacao;
  private ContextoContaDTO contextoConta;
}
