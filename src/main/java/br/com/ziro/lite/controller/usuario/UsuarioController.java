package br.com.ziro.lite.controller.usuario;

import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações relacionadas aos usuários")
public class UsuarioController {

  private final UsuarioService usuarioService;

  @Operation(
      summary = "Listar todos os usuários",
      description = "Retorna uma lista com todos os usuários cadastrados no sistema.")
  @GetMapping
  public ResponseEntity<List<Usuario>> listarTodos() {
    return ResponseEntity.ok(usuarioService.listarTodos());
  }

  @Operation(
      summary = "Buscar usuário por ID",
      description = "Retorna os detalhes de um usuário específico com base no seu ID.")
  @GetMapping("/{id}")
  public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
    return usuarioService
        .buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(
      summary = "Criar novo usuário",
      description = "Cria um novo usuário no sistema com as informações fornecidas.")
  @PostMapping
  public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) throws Exception {
    Usuario criado = usuarioService.criar(usuario);
    return ResponseEntity.status(201).body(criado);
  }

  @Operation(
      summary = "Atualizar usuário existente",
      description = "Atualiza os dados de um usuário já cadastrado com base no ID informado.")
  @PutMapping("/{id}")
  public ResponseEntity<Usuario> atualizar(
      @PathVariable Long id, @Valid @RequestBody Usuario usuario) throws Exception {
    return usuarioService
        .atualizar(id, usuario)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(
      summary = "Deletar usuário por ID",
      description = "Remove um usuário do sistema com base no ID informado.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    usuarioService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
