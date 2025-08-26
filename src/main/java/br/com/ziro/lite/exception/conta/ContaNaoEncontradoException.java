package br.com.ziro.lite.exception.conta;

import br.com.ziro.lite.exception.base.naoencontrado.NaoEncontradoException;

public class ContaNaoEncontradoException extends NaoEncontradoException {

  public ContaNaoEncontradoException() {
    super("Conta não encontrado");
  }
}
