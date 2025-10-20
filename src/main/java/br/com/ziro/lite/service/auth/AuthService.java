package br.com.ziro.lite.service.auth;

import br.com.ziro.lite.entity.auth.LoginDTO;
import br.com.ziro.lite.entity.auth.LoginResponseDTO;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.util.password.PasswordUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UsuarioRepository usuarioRepository;
  final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private final PasswordUtil passwordUtil;

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.duracaoTokenLogin}")
  private long duracaoTokenLogin;

  public AuthService(UsuarioRepository usuarioRepository, final PasswordUtil passwordUtil) {
    this.usuarioRepository = usuarioRepository;
    this.passwordUtil = passwordUtil;
  }

  public String gerarToken(Usuario usuario) {
    long agora = System.currentTimeMillis();
    Key key = Keys.hmacShaKeyFor(secret.getBytes());

    return Jwts.builder()
        .setSubject(usuario.getId().toString())
        .setIssuer("MinhaApp")
        .setIssuedAt(new Date(agora))
        .setExpiration(new Date(agora + duracaoTokenLogin))
        .claim("email", usuario.getEmail())
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Optional<LoginResponseDTO> login(final LoginDTO loginDTO) throws Exception {
    String senhaHash = this.passwordUtil.hashSHA256(loginDTO.getSenha());

    Optional<Usuario> usuarioOpt =
        usuarioRepository
            .findByEmail(loginDTO.getEmail())
            .filter(usuario -> usuario.getSenha().equals(senhaHash));

    return usuarioOpt.map(
        usuario -> {
          String token = gerarToken(usuario);
          LoginResponseDTO response = new LoginResponseDTO();
          response.setToken(token);
          response.setUsuarioId(usuario.getId());
          response.setEmail(usuario.getEmail());
          return response;
        });
  }

  public boolean validarToken(String token) {
    try {
      Key key = Keys.hmacShaKeyFor(secret.getBytes());

      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Key getSecretKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }
}
