package br.com.ziro.lite.controller.auth;

import br.com.ziro.lite.entity.auth.LoginDTO;
import br.com.ziro.lite.entity.auth.LoginResponseDTO;
import br.com.ziro.lite.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Autenticação")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário (login)")
    public Optional<LoginResponseDTO> login(final @Valid @RequestBody LoginDTO loginDTO) throws Exception {
       return authService.login(loginDTO);
    }

    @GetMapping("/validar-token")
    @Operation(summary = "Validar token JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> validarToken(
            @Parameter(hidden = true)
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        String token = authHeader.replaceFirst("(?i)^Bearer\\s+", "").trim();

        return authService.validarToken(token)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
