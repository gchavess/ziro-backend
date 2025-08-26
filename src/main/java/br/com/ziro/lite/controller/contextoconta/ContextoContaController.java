package br.com.ziro.lite.controller.contextoconta;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.exception.contextoconta.ContextoContaNaoEncontradoException;
import br.com.ziro.lite.service.contextoconta.ContextoContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contextos_conta")
@RequiredArgsConstructor
@Tag(name = "Contextos de Conta", description = "Operações relacionadas aos contextos de conta")
public class ContextoContaController {

  private final ContextoContaService service;

  @Operation(
      summary = "Listar todos os contextos de conta",
      description = "Retorna uma lista com todos os contextos de conta cadastrados no sistema.")
  @GetMapping
  public List<ContextoContaDTO> listarTodos() {
    return service.listarTodos();
  }

  @Operation(
      summary = "Buscar contexto de conta por ID",
      description = "Retorna os detalhes de um contexto de conta específico com base no seu ID.")
  @GetMapping("/{id}")
  public ResponseEntity<ContextoConta> buscarPorId(@PathVariable Long id) {
    return service
        .buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(
      summary = "Criar novo contexto de conta",
      description = "Cria um novo contexto de conta no sistema com as informações fornecidas.")
  @PostMapping
  public ContextoContaDTO criar(@RequestBody ContextoConta contextoConta) {
    return service.salvar(contextoConta);
  }

  @Operation(
      summary = "Atualizar contexto de conta",
      description = "Atualiza os dados de um contexto de conta existente com base no ID informado.")
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

  @Operation(
      summary = "Deletar contexto de conta",
      description = "Remove um contexto de conta do sistema com base no ID informado.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    service.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
