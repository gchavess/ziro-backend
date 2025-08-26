package br.com.ziro.lite.exception.conta;

import br.com.ziro.lite.exception.base.naoencontrado.NaoEncontradoException;

public class ContaPaiNaoEncontradoException extends NaoEncontradoException {

  public ContaPaiNaoEncontradoException() {
    super("Conta pai não encontrado");
  }
}
