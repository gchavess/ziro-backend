package br.com.ziro.lite.exception.contextoconta;

import br.com.ziro.lite.exception.base.naoencontrado.NaoEncontradoException;

public class ContextoContaNaoEncontradoException extends NaoEncontradoException {

  public ContextoContaNaoEncontradoException() {
    super("Contexto de Conta não encontrado");
  }
}
