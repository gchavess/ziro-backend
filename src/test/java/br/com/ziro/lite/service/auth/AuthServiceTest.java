package br.com.ziro.lite.service.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import br.com.ziro.lite.entity.auth.LoginDTO;
import br.com.ziro.lite.entity.auth.LoginResponseDTO;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.util.password.PasswordUtil;
import io.jsonwebtoken.Jwts;
import java.lang.reflect.Field;
import java.security.Key;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private UsuarioRepository usuarioRepository;
  @Mock private PasswordUtil passwordUtil;

  @InjectMocks private AuthService authService;

  private Usuario usuario;

  @BeforeEach
  void setUp() throws Exception {
    usuario = new Usuario();
    usuario.setId(1L);
    usuario.setEmail("teste@teste.com");
    usuario.setSenha("hashcorreta");
    usuario.setNome("João da Silva");

    // injeta valores @Value
    setPrivateField("secret", "12345678901234567890123456789012"); // 32 chars
    setPrivateField("duracaoTokenLogin", 3600000L);
  }

  private void setPrivateField(String fieldName, Object value) throws Exception {
    Field field = AuthService.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(authService, value);
  }

  @Test
  void login_deveRetornarLoginResponseDTO_quandoCredenciaisCorretas() throws Exception {
    LoginDTO loginDTO = new LoginDTO();
    loginDTO.setEmail("teste@teste.com");
    loginDTO.setSenha("senha123");

    when(passwordUtil.hashSHA256("senha123")).thenReturn("hashcorreta");
    when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

    Optional<LoginResponseDTO> result = authService.login(loginDTO);

    assertTrue(result.isPresent());
    LoginResponseDTO r = result.get();

    assertEquals(1L, r.getUsuarioId());
    assertEquals("teste@teste.com", r.getEmail());
    assertEquals("João", r.getNome()); // buscarPrimeiroNome
    assertEquals("JS", r.getIniciaisNome()); // buscarIniciaisNome
    assertNotNull(r.getToken());
  }

  @Test
  void login_deveRetornarVazio_quandoSenhaIncorreta() throws Exception {
    LoginDTO loginDTO = new LoginDTO();
    loginDTO.setEmail("teste@teste.com");
    loginDTO.setSenha("errada");

    when(passwordUtil.hashSHA256("errada")).thenReturn("hasherrada");
    when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

    Optional<LoginResponseDTO> result = authService.login(loginDTO);

    assertTrue(result.isEmpty());
  }

  @Test
  void login_deveRetornarVazio_quandoEmailNaoEncontrado() throws Exception {
    LoginDTO loginDTO = new LoginDTO();
    loginDTO.setEmail("naoexiste@teste.com");
    loginDTO.setSenha("qualquer");

    when(usuarioRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

    Optional<LoginResponseDTO> result = authService.login(loginDTO);

    assertTrue(result.isEmpty());
  }

  @Test
  void gerarToken_deveGerarTokenJwtValido() {
    String token = authService.gerarToken(usuario);

    assertNotNull(token);

    String subject =
        Jwts.parserBuilder()
            .setSigningKey(authService.getSecretKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();

    assertEquals("1", subject);
  }

  @Test
  void validarToken_deveRetornarTrue_quandoTokenValido() {
    String token = authService.gerarToken(usuario);
    assertTrue(authService.validarToken(token));
  }

  @Test
  void validarToken_deveRetornarFalse_quandoTokenInvalido() {
    assertFalse(authService.validarToken("token_invalido"));
  }

  @Test
  void buscarPrimeiroNome_deveRetornarPrimeiroNome() throws Exception {
    var method = AuthService.class.getDeclaredMethod("buscarPrimeiroNome", String.class);
    method.setAccessible(true);

    assertEquals("Maria", method.invoke(authService, "Maria Joaquina"));
    assertEquals("Carlos", method.invoke(authService, "Carlos"));
    assertEquals("", method.invoke(authService, ""));
    assertEquals("", method.invoke(authService, (Object) null));
  }

  @Test
  void buscarIniciaisNome_deveRetornarIniciais() throws Exception {
    var method = AuthService.class.getDeclaredMethod("buscarIniciaisNome", String.class);
    method.setAccessible(true);

    assertEquals("MJ", method.invoke(authService, "Maria Joaquina"));
    assertEquals("C", method.invoke(authService, "Carlos"));
    assertEquals("", method.invoke(authService, ""));
    assertEquals("", method.invoke(authService, (Object) null));
  }

  @Test
  void getSecretKey_deveRetornarChaveValida() {
    Key k = authService.getSecretKey();
    assertNotNull(k);
  }
}
