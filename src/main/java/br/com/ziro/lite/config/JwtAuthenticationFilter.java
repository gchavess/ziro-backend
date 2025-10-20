package br.com.ziro.lite.config;

import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.service.auth.AuthService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final AuthService authService;
  private final UsuarioRepository usuarioRepository;

  public JwtAuthenticationFilter(AuthService authService, UsuarioRepository usuarioRepository) {
    this.authService = authService;
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.substring(7);

    if (authService.validarToken(token)) {
      // Pega o id do usuário do token
      String usuarioId =
          Jwts.parserBuilder()
              .setSigningKey(authService.getSecretKey())
              .build()
              .parseClaimsJws(token)
              .getBody()
              .getSubject();

      Optional<Usuario> usuarioOpt = usuarioRepository.findById(Long.parseLong(usuarioId));

      if (usuarioOpt.isPresent()) {
        Usuario usuario = usuarioOpt.get();

        // Popula SecurityContext
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                usuario.getEmail(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }

    filterChain.doFilter(request, response);
  }
}
