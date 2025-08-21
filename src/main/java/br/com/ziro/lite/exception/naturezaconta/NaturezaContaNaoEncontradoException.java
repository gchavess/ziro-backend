package br.com.ziro.lite.exception.naturezaconta;

import br.com.ziro.lite.exception.base.naoencontrado.NaoEncontradoException;

public class NaturezaContaNaoEncontradoException extends NaoEncontradoException {

  public NaturezaContaNaoEncontradoException() {
    super("Natureza de Conta não encontrado");
  }
}
