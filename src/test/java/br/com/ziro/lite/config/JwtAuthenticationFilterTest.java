package br.com.ziro.lite.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.service.auth.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class JwtAuthenticationFilterTest {

  @Mock private AuthService authService;

  @Mock private UsuarioRepository usuarioRepository;

  @Mock private FilterChain filterChain;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @InjectMocks private JwtAuthenticationFilter filter;

  // Chave secreta para testes
  Key secretKey = Keys.hmacShaKeyFor("my-secret-key-123456789012345678901234".getBytes());

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    SecurityContextHolder.clearContext();

    when(authService.getSecretKey()).thenReturn(secretKey);
  }

  @Test
  void deveSeguirFluxoQuandoNaoTemHeader() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn(null);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void deveSeguirFluxoQuandoHeaderNaoComecaComBearer() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Token 123");

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void deveIgnorarQuandoTokenInvalido() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Bearer abc");
    when(authService.validarToken("abc")).thenReturn(false);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void deveSeguirFluxoQuandoUsuarioNaoExiste() throws Exception {
    String token = gerarTokenComSubject("55");

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(authService.validarToken(token)).thenReturn(true);
    when(usuarioRepository.findById(55L)).thenReturn(Optional.empty());

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void deveAutenticarUsuarioQuandoTokenValidoEUsuarioExiste() throws Exception {
    Usuario usuario = new Usuario();
    usuario.setId(77L);
    usuario.setEmail("teste@teste.com");

    String token = gerarTokenComSubject("77");

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(authService.validarToken(token)).thenReturn(true);
    when(usuarioRepository.findById(77L)).thenReturn(Optional.of(usuario));

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    assertNotNull(auth);
    assertEquals("teste@teste.com", auth.getPrincipal());
    assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
  }

  // ------------------------------
  // Utilitário para gerar token JWT válido
  // ------------------------------
  private String gerarTokenComSubject(String subject) {
    return Jwts.builder()
        .setSubject(subject)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }
}
