package br.com.ziro.lite.controller.auth;

import br.com.ziro.lite.entity.auth.LoginDTO;
import br.com.ziro.lite.entity.auth.LoginResponseDTO;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.service.auth.AuthService;
import br.com.ziro.lite.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Operações de autenticação e validação de tokens")
public class AuthController {

  private final AuthService authService;
  private final UsuarioService usuarioService;

  @Operation(
      summary = "Autenticar usuário (login)",
      description =
          "Realiza a autenticação de um usuário com base nas credenciais fornecidas. "
              + "Se bem-sucedido, retorna um token JWT para ser usado em chamadas subsequentes.")
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO)
      throws Exception {
    Optional<LoginResponseDTO> response = authService.login(loginDTO);
    return response
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

  @Operation(
      summary = "Validar token JWT",
      description =
          "Valida um token JWT enviado no cabeçalho de autorização (Bearer Token). "
              + "Se o token for válido, retorna **200 OK**, caso contrário retorna **401 Unauthorized**.")
  @GetMapping("/validar-token")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> validarToken(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

    String token = authHeader.replaceFirst("(?i)^Bearer\\s+", "").trim();

    return authService.validarToken(token)
        ? ResponseEntity.ok().build()
        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @Operation(
      summary = "Criar novo usuário",
      description = "Cria um novo usuário no sistema com as informações fornecidas.")
  @PostMapping("/conta")
  public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) throws Exception {
    Usuario criado = usuarioService.criar(usuario);
    return ResponseEntity.status(201).body(criado);
  }
}
