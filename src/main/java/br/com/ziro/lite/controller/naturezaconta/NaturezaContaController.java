package br.com.ziro.lite.controller.naturezaconta;

import br.com.ziro.lite.dto.naturezaconta.NaturezaContaAgrupadaDTO;
import br.com.ziro.lite.dto.naturezaconta.NaturezaContaDTO;
import br.com.ziro.lite.exception.contextoconta.ContextoContaNaoEncontradoException;
import br.com.ziro.lite.exception.naturezaconta.NaturezaContaNaoEncontradoException;
import br.com.ziro.lite.exception.usuario.UsuarioNaoEncontradoException;
import br.com.ziro.lite.service.naturezaconta.NaturezaContaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/naturezas_conta")
@RequiredArgsConstructor
public class NaturezaContaController {

  private final NaturezaContaService service;

  @GetMapping
  public List<NaturezaContaDTO> listarTodos() {
    return service.listarTodos();
  }

  @GetMapping("/{id}")
  public NaturezaContaDTO buscarPorId(@PathVariable Long id)
      throws NaturezaContaNaoEncontradoException {
    return service.buscarPorId(id);
  }

  @PostMapping
  public NaturezaContaDTO criar(@RequestBody NaturezaContaDTO request)
      throws UsuarioNaoEncontradoException, ContextoContaNaoEncontradoException {
    return service.salvar(request);
  }

  @PutMapping("/{id}")
  public NaturezaContaDTO atualizar(@PathVariable Long id, @RequestBody NaturezaContaDTO request)
      throws ContextoContaNaoEncontradoException, NaturezaContaNaoEncontradoException {
    return service.atualizar(id, request);
  }

  @DeleteMapping("/{id}")
  public void deletar(@PathVariable Long id) {
    service.deletar(id);
  }

  @GetMapping("/agrupadas")
  public ResponseEntity<List<NaturezaContaAgrupadaDTO>> listarAgrupadas() {
    List<NaturezaContaAgrupadaDTO> result = service.listarAgrupadasPorContexto();
    return ResponseEntity.ok(result);
  }
}
