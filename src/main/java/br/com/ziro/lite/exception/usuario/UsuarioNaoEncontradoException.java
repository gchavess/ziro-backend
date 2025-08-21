package br.com.ziro.lite.exception.usuario;

import br.com.ziro.lite.exception.base.naoencontrado.NaoEncontradoException;

public class UsuarioNaoEncontradoException extends NaoEncontradoException {

  public UsuarioNaoEncontradoException() {
    super("Usuário não encontrado");
  }
}
