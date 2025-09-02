package br.com.ziro.lite.security;

import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.usuario.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UsuarioLogado {

  private final HttpServletRequest request;

  public UsuarioLogado(HttpServletRequest request) {
    this.request = request;
  }

  public UsuarioDTO getCurrentDTO() {
    String usuarioIdHeader = request.getHeader("XXX-USUARIO-ID");
    if (usuarioIdHeader == null) {
      throw new IllegalStateException("Header XXX-USUARIO-ID não informado!");
    }

    UsuarioDTO usuario = new UsuarioDTO();
    usuario.setId(Long.parseLong(usuarioIdHeader));
    return usuario;
  }

  public Usuario getCurrent() {
    String usuarioIdHeader = request.getHeader("XXX-USUARIO-ID");
    if (usuarioIdHeader == null) {
      throw new IllegalStateException("Header XXX-USUARIO-ID não informado!");
    }

    Usuario usuario = new Usuario();
    usuario.setId(Long.parseLong(usuarioIdHeader));
    return usuario;
  }
}
