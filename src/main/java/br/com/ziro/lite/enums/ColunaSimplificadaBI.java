package br.com.ziro.lite.enums;

public enum ColunaSimplificadaBI {
  REALIZADO("Realizado", ChartColor.REALIZADO.getHex()),
  COMPROMETIDO("Comprometido", ChartColor.COMPROMETIDO.getHex());

  private final String descricao;
  private final String cor;

  ColunaSimplificadaBI(String descricao, String cor) {
    this.descricao = descricao;
    this.cor = cor;
  }

  public String getDescricao() {
    return descricao;
  }

  public String getCor() {
    return cor;
  }
}
