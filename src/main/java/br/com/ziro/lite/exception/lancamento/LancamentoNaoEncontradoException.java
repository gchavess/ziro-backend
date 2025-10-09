package br.com.ziro.lite.exception.lancamento;

import br.com.ziro.lite.exception.base.naoencontrado.NaoEncontradoException;

public class LancamentoNaoEncontradoException extends NaoEncontradoException {

  public LancamentoNaoEncontradoException() {
    super("Lançamento não encontrado");
  }
}
