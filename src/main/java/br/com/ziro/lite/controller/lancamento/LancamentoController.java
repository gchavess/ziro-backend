package br.com.ziro.lite.controller.lancamento;

import br.com.ziro.lite.dto.lancamento.LancamentoDTO;
import br.com.ziro.lite.exception.conta.ContaNaoEncontradoException;
import br.com.ziro.lite.exception.lancamento.LancamentoNaoEncontradoException;
import br.com.ziro.lite.service.lancamento.LancamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
@Tag(name = "Lançamentos", description = "Operações relacionadas aos lançamentos")
public class LancamentoController {

  private final LancamentoService service;

  @Operation(
      summary = "Listar todos os lançamentos",
      description = "Retorna uma lista com todos os lançamentos cadastrados no sistema.")
  @GetMapping
  public List<LancamentoDTO> listarTodos() {
    return service.listarTodos();
  }

  @Operation(
      summary = "Buscar lançamento por ID",
      description = "Retorna os detalhes de um lançamento específico com base no seu ID.")
  @GetMapping("/{id}")
  public LancamentoDTO buscarPorId(@PathVariable Long id) throws LancamentoNaoEncontradoException {
    return service.buscarPorId(id);
  }

  @Operation(
      summary = "Criar novo lançamento",
      description = "Cria um novo lançamento no sistema com as informações fornecidas.")
  @PostMapping
  public LancamentoDTO criar(@RequestBody LancamentoDTO request)
      throws ContaNaoEncontradoException {
    return service.salvar(request);
  }

  @Operation(
      summary = "Atualizar lançamento",
      description = "Atualiza os dados de um lançamento existente com base no ID informado.")
  @PutMapping("/{id}")
  public LancamentoDTO atualizar(@PathVariable Long id, @RequestBody LancamentoDTO request)
      throws LancamentoNaoEncontradoException, ContaNaoEncontradoException {
    return service.atualizar(id, request);
  }

  @Operation(
      summary = "Deletar lançamento",
      description = "Remove um lançamento do sistema com base no ID informado.")
  @DeleteMapping("/{id}")
  public void deletar(@PathVariable Long id) {
    service.deletar(id);
  }
}
