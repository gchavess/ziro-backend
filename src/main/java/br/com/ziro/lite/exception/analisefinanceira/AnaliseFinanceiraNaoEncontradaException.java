package br.com.ziro.lite.exception.analisefinanceira;

import br.com.ziro.lite.exception.base.naoencontrado.NaoEncontradoException;

public class AnaliseFinanceiraNaoEncontradaException extends NaoEncontradoException {

  public AnaliseFinanceiraNaoEncontradaException() {
    super("Análise financeira não encontrada");
  }
}
