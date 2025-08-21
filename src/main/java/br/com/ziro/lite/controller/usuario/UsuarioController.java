package br.com.ziro.lite.controller.usuario;

import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "CRUD para usuários")
public class UsuarioController {

  private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @GetMapping
  @Operation(summary = "Listar todos os usuários")
  public ResponseEntity<List<Usuario>> listarTodos() {
    return ResponseEntity.ok(usuarioService.listarTodos());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar usuário por ID")
  public ResponseEntity<Usuario> buscarPorId(final @PathVariable Long id) {
    return usuarioService
        .buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Criar novo usuário")
  public ResponseEntity<Usuario> criar(final @Valid @RequestBody Usuario usuario) throws Exception {
    Usuario criado = usuarioService.criar(usuario);
    return ResponseEntity.status(201).body(criado);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar usuário existente")
  public ResponseEntity<Usuario> atualizar(
      final @PathVariable Long id, final @Valid @RequestBody Usuario usuario) throws Exception {
    return usuarioService
        .atualizar(id, usuario)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Deletar usuário por ID")
  public ResponseEntity<Void> deletar(final @PathVariable Long id) {
    usuarioService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
