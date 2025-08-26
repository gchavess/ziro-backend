package br.com.ziro.lite.controller.naturezaconta;

import br.com.ziro.lite.dto.naturezaconta.NaturezaContaAgrupadaDTO;
import br.com.ziro.lite.dto.naturezaconta.NaturezaContaDTO;
import br.com.ziro.lite.exception.contextoconta.ContextoContaNaoEncontradoException;
import br.com.ziro.lite.exception.naturezaconta.NaturezaContaNaoEncontradoException;
import br.com.ziro.lite.exception.usuario.UsuarioNaoEncontradoException;
import br.com.ziro.lite.service.naturezaconta.NaturezaContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/naturezas_conta")
@RequiredArgsConstructor
@Tag(name = "Naturezas de Conta", description = "Operações relacionadas às naturezas de conta")
public class NaturezaContaController {

  private final NaturezaContaService service;

  @Operation(
      summary = "Listar todas as naturezas de conta",
      description = "Retorna uma lista com todas as naturezas de conta cadastradas no sistema.")
  @GetMapping
  public List<NaturezaContaDTO> listarTodos() {
    return service.listarTodos();
  }

  @Operation(
      summary = "Buscar natureza de conta por ID",
      description = "Retorna os detalhes de uma natureza de conta específica com base no seu ID.")
  @GetMapping("/{id}")
  public NaturezaContaDTO buscarPorId(@PathVariable Long id)
      throws NaturezaContaNaoEncontradoException {
    return service.buscarPorId(id);
  }

  @Operation(
      summary = "Criar nova natureza de conta",
      description = "Cria uma nova natureza de conta no sistema com as informações fornecidas.")
  @PostMapping
  public NaturezaContaDTO criar(@RequestBody NaturezaContaDTO request)
      throws UsuarioNaoEncontradoException, ContextoContaNaoEncontradoException {
    return service.salvar(request);
  }

  @Operation(
      summary = "Atualizar natureza de conta",
      description =
          "Atualiza os dados de uma natureza de conta existente com base no ID informado.")
  @PutMapping("/{id}")
  public NaturezaContaDTO atualizar(@PathVariable Long id, @RequestBody NaturezaContaDTO request)
      throws ContextoContaNaoEncontradoException, NaturezaContaNaoEncontradoException {
    return service.atualizar(id, request);
  }

  @Operation(
      summary = "Deletar natureza de conta",
      description = "Remove uma natureza de conta do sistema com base no ID informado.")
  @DeleteMapping("/{id}")
  public void deletar(@PathVariable Long id) {
    service.deletar(id);
  }

  @Operation(
      summary = "Listar naturezas de conta agrupadas",
      description = "Retorna uma lista de naturezas de conta agrupadas por contexto.")
  @GetMapping("/agrupadas")
  public ResponseEntity<List<NaturezaContaAgrupadaDTO>> listarAgrupadas() {
    List<NaturezaContaAgrupadaDTO> result = service.listarAgrupadasPorContexto();
    return ResponseEntity.ok(result);
  }
}
