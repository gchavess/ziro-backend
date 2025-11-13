package br.com.ziro.lite.controller.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.entity.auth.LoginDTO;
import br.com.ziro.lite.entity.auth.LoginResponseDTO;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.service.auth.AuthService;
import br.com.ziro.lite.service.usuario.UsuarioService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AuthControllerTest {

  @Mock private AuthService authService;

  @Mock private UsuarioService usuarioService;

  @InjectMocks private AuthController controller;

  private LoginDTO loginDTO;
  private LoginResponseDTO loginResponse;
  private Usuario usuario;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    loginDTO = new LoginDTO();
    loginDTO.setEmail("usuario@gmail.com");
    loginDTO.setSenha("senha123");

    loginResponse = new LoginResponseDTO();
    loginResponse.setToken("token123");

    usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNome("Usuario Teste");
  }

  @Test
  void login_deveRetornarToken_quandoSucesso() throws Exception {
    when(authService.login(loginDTO)).thenReturn(Optional.of(loginResponse));

    ResponseEntity<LoginResponseDTO> response = controller.login(loginDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(loginResponse, response.getBody());
    verify(authService, times(1)).login(loginDTO);
  }

  @Test
  void login_deveRetornarUnauthorized_quandoFalha() throws Exception {
    when(authService.login(loginDTO)).thenReturn(Optional.empty());

    ResponseEntity<LoginResponseDTO> response = controller.login(loginDTO);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());
    verify(authService, times(1)).login(loginDTO);
  }

  @Test
  void validarToken_deveRetornarOk_quandoTokenValido() {
    String token = "token123";
    String authHeader = "Bearer " + token;

    when(authService.validarToken(token)).thenReturn(true);

    ResponseEntity<Void> response = controller.validarToken(authHeader);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(authService, times(1)).validarToken(token);
  }

  @Test
  void validarToken_deveRetornarUnauthorized_quandoTokenInvalido() {
    String token = "tokenInvalido";
    String authHeader = "Bearer " + token;

    when(authService.validarToken(token)).thenReturn(false);

    ResponseEntity<Void> response = controller.validarToken(authHeader);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    verify(authService, times(1)).validarToken(token);
  }

  @Test
  void criar_deveRetornarUsuarioCriado() throws Exception {
    when(usuarioService.criar(usuario)).thenReturn(usuario);

    ResponseEntity<Usuario> response = controller.criar(usuario);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(usuario, response.getBody());
    verify(usuarioService, times(1)).criar(usuario);
  }
}
