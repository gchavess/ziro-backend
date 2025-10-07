package br.com.ziro.lite.service.auth;

import br.com.ziro.lite.entity.auth.LoginDTO;
import br.com.ziro.lite.entity.auth.LoginResponseDTO;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.util.password.PasswordUtil;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;

    @BeforeEach
    void setUp() throws Exception {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("hashcorreta"); // senha já hash

        // Setar duracaoTokenLogin privado via Reflection
        Field duracaoField = AuthService.class.getDeclaredField("duracaoTokenLogin");
        duracaoField.setAccessible(true);
        duracaoField.set(authService, 3600000L); // 1 hora
    }

    @Test
    void login_deveRetornarLoginResponseDTO_quandoCredenciaisCorretas() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("teste@teste.com");
        loginDTO.setSenha("senha123");

        // Mock do hash da senha
        when(passwordUtil.hashSHA256("senha123")).thenReturn("hashcorreta");

        // Mock do repositório
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));

        Optional<LoginResponseDTO> result = authService.login(loginDTO);

        assertTrue(result.isPresent());
        assertEquals(usuario.getId(), result.get().getUsuarioId());
        assertEquals(usuario.getEmail(), result.get().getEmail());
        assertNotNull(result.get().getToken());
    }

    @Test
    void login_deveRetornarVazio_quandoSenhaIncorreta() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("teste@teste.com");
        loginDTO.setSenha("senhaErrada");

        when(passwordUtil.hashSHA256("senhaErrada")).thenReturn("hasherrada");
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
    void gerarToken_deveRetornarTokenValido() {
        String token = authService.gerarToken(usuario);

        assertNotNull(token);

        // Decodificar token para checar se o subject é o id do usuário
        String subject = Jwts.parserBuilder()
                .setSigningKey(authService.key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertEquals(usuario.getId().toString(), subject);
    }

    @Test
    void validarToken_deveRetornarTrue_quandoTokenValido() {
        String token = authService.gerarToken(usuario);
        assertTrue(authService.validarToken(token));
    }

    @Test
    void validarToken_deveRetornarFalse_quandoTokenInvalido() {
        assertFalse(authService.validarToken("tokeninvalido"));
    }
}
