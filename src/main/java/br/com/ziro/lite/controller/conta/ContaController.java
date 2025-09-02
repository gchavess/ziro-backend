package br.com.ziro.lite.controller.conta;

import br.com.ziro.lite.dto.conta.AssociarNaturezaContaDTO;
import br.com.ziro.lite.dto.conta.ContaDTO;
import br.com.ziro.lite.dto.conta.ContaOptionDTO;
import br.com.ziro.lite.dto.conta.ContaTreeNodeDTO;
import br.com.ziro.lite.exception.conta.ContaNaoEncontradoException;
import br.com.ziro.lite.exception.conta.ContaPaiNaoEncontradoException;
import br.com.ziro.lite.exception.naturezaconta.NaturezaContaNaoEncontradoException;
import br.com.ziro.lite.exception.usuario.UsuarioNaoEncontradoException;
import br.com.ziro.lite.service.conta.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
@Tag(name = "Contas", description = "Operações relacionadas às contas")
public class ContaController {

  private final ContaService service;

  @Operation(
      summary = "Listar todas as contas",
      description = "Retorna uma lista com todas as contas cadastradas no sistema.")
  @GetMapping
  public List<ContaDTO> listarTodos() {
    return service.listarTodos();
  }

  @Operation(
      summary = "Buscar conta por ID",
      description = "Retorna os detalhes de uma conta específica com base no seu ID.")
  @GetMapping("/{id}")
  public ContaDTO buscarPorId(@PathVariable Long id) throws ContaNaoEncontradoException {
    return service.buscarPorId(id);
  }

  @Operation(
      summary = "Criar nova conta",
      description = "Cria uma nova conta no sistema com as informações fornecidas.")
  @PostMapping
  public ContaDTO criar(@RequestBody ContaDTO request)
      throws UsuarioNaoEncontradoException,
          NaturezaContaNaoEncontradoException,
          ContaPaiNaoEncontradoException {
    return service.salvar(request);
  }

  @Operation(
      summary = "Atualizar conta",
      description = "Atualiza os dados de uma conta existente com base no ID informado.")
  @PutMapping("/{id}")
  public ContaDTO atualizar(@PathVariable Long id, @RequestBody ContaDTO request)
      throws NaturezaContaNaoEncontradoException,
          ContaNaoEncontradoException,
          ContaPaiNaoEncontradoException {
    return service.atualizar(id, request);
  }

  @Operation(
      summary = "Deletar conta",
      description = "Remove uma conta do sistema com base no ID informado.")
  @DeleteMapping("/{id}")
  public void deletar(@PathVariable Long id) {
    service.deletar(id);
  }

  @Operation(
      summary = "Obter árvore de contas",
      description = "Retorna a hierarquia completa de contas em formato de árvore.")
  @GetMapping("/tree")
  public List<ContaTreeNodeDTO> getTree() {
    return service.getTree();
  }

  @Operation(
      summary = "Associar natureza a contas",
      description = "Associa uma natureza de conta a uma ou mais contas existentes.")
  @PutMapping("/associar_natureza")
  public List<ContaDTO> associarNatureza(@RequestBody AssociarNaturezaContaDTO request)
      throws NaturezaContaNaoEncontradoException, ContaNaoEncontradoException {
    return service.associarNatureza(request);
  }

  @Operation(
      summary = "Lista de contas para dropdown recursivo",
      description =
          "Retorna todas as contas estruturadas em formato de árvore, com 'label', 'value' e 'children', prontas para consumo em componentes de seleção hierárquica (como um dropdown recursivo no frontend).")
  @GetMapping("/dropdown")
  public List<ContaOptionDTO> listarDropdown() {
    return service.listarDropdown();
  }
}
