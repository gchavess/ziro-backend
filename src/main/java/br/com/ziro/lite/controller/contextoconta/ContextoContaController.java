package br.com.ziro.lite.controller.contextoconta;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.exception.contextoconta.ContextoContaNaoEncontradoException;
import br.com.ziro.lite.service.contextoconta.ContextoContaService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contextos_conta")
public class ContextoContaController {
  private final ContextoContaService service;

  public ContextoContaController(ContextoContaService service) {
    this.service = service;
  }

  @GetMapping
  public List<ContextoContaDTO> listarTodos() {
    return service.listarTodos();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ContextoConta> buscarPorId(@PathVariable Long id) {
    return service
        .buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ContextoContaDTO criar(@RequestBody ContextoConta contextoConta) {
    return service.salvar(contextoConta);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ContextoContaDTO> atualizar(
      @PathVariable Long id, @RequestBody ContextoConta contextoConta)
      throws ContextoContaNaoEncontradoException {
    try {
      return ResponseEntity.ok(service.atualizar(id, contextoConta));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    service.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
