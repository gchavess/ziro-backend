package br.com.ziro.lite.util.usuariopadrao;

import br.com.ziro.lite.entity.usuario.Usuario;

public class UsuarioPadraoUtil {
  public static final Long ID_USUARIO_PADRAO = 1L;

  public static final Usuario get() throws Exception {
    final Usuario usuarioPadrao = new Usuario();

    usuarioPadrao.setId(ID_USUARIO_PADRAO);

    return usuarioPadrao;
  }
}
