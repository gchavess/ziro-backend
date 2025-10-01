package br.com.ziro.lite.controller.analisefinanceira;

import br.com.ziro.lite.dto.analisefinanceira.usuario.AnaliseFinanceiraDTO;
import br.com.ziro.lite.exception.analisefinanceira.AnaliseFinanceiraNaoEncontradaException;
import br.com.ziro.lite.service.analisefinanceira.AnaliseFinanceiraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analises_financeiras")
@RequiredArgsConstructor
@Tag(name = "Análises Financeiras", description = "Operações relacionadas às análises financeiras")
public class AnaliseFinanceiraController {

  private final AnaliseFinanceiraService service;

  @Operation(
      summary = "Listar todas as análises financeiras",
      description = "Retorna uma lista com todas as análises financeiras cadastradas no sistema.")
  @GetMapping
  public List<AnaliseFinanceiraDTO> listarTodos() {
    return service.listarTodos();
  }

  @Operation(
      summary = "Buscar análise financeira por ID",
      description = "Retorna os detalhes de uma análise financeira específica com base no seu ID.")
  @GetMapping("/{id}")
  public ResponseEntity<AnaliseFinanceiraDTO> buscarPorId(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(service.buscarPorId(id));
    } catch (AnaliseFinanceiraNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(
      summary = "Criar nova análise financeira",
      description = "Cria uma nova análise financeira no sistema com as informações fornecidas.")
  @PostMapping
  public AnaliseFinanceiraDTO criar(@RequestBody AnaliseFinanceiraDTO dto) {
    return service.criar(dto);
  }

  @Operation(
      summary = "Atualizar análise financeira",
      description =
          "Atualiza os dados de uma análise financeira existente com base no ID informado.")
  @PutMapping("/{id}")
  public ResponseEntity<AnaliseFinanceiraDTO> atualizar(
      @PathVariable Long id, @RequestBody AnaliseFinanceiraDTO dto) {
    try {
      return ResponseEntity.ok(service.atualizar(id, dto));
    } catch (AnaliseFinanceiraNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(
      summary = "Deletar análise financeira",
      description = "Remove uma análise financeira do sistema com base no ID informado.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    service.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
