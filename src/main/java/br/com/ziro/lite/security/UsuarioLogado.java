package br.com.ziro.lite.security;

import br.com.ziro.lite.dto.usuario.UsuarioDTO;
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

  public UsuarioDTO get() {
    String usuarioIdHeader = request.getHeader("XXX-USUARIO-ID");
    if (usuarioIdHeader == null) {
      return null;
    }

    UsuarioDTO usuario = new UsuarioDTO();
    usuario.setId(Long.parseLong(usuarioIdHeader));
    return usuario;
  }
}
