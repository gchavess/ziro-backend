package br.com.ziro.lite.dto.conta;

import br.com.ziro.lite.dto.naturezaconta.NaturezaContaDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaTreeNodeDTO {

  private Long id;
  private String label;
  private Boolean expanded;
  private NaturezaContaDTO natureza;
  private List<ContaTreeNodeDTO> children;
  private String codigo;
  private String descricao;
  private String observacao;
}
