package br.com.ziro.lite.enums;

public enum ChartColor {
  BLUE("#3b82f6"),
  RED("#ef4444"),
  ORANGE("#f59e0b"),
  GREEN("#10b981"),
  PURPLE("#8b5cf6"),
  PINK("#ec4899"),
  GRAY("#6b7280"),
  DEEP_ORANGE("#ea580c"),
  CYAN("#06b6d4"),
  LIGHT_BLUE("#60a5fa"),
  LIGHT_GREEN("#34d399"),
  AMBER("#fbbf24"),
  SOFT_RED("#f87171"),
  VIOLET("#a78bfa"),
  LIGHT_PINK("#f472b6"),
  LIGHT_GRAY("#9ca3af"),
  ORANGE_LIGHT("#fb923c"),
  SKY_BLUE("#22d3ee"),
  BLUE_SKY("#93c5fd"),
  MINT("#6ee7b7"),
  SOFT_YELLOW("#fde68a"),
  PASTEL_RED("#fca5a5"),
  LAVENDER("#c4b5fd"),
  PASTEL_PINK("#f9a8d4"),
  SILVER("#d1d5db"),
  PEACH("#fdba74"),
  AQUA("#67e8f9"),
  BLUE_LIGHT("#bfdbfe"),
  GREEN_MINT("#a7f3d0"),
  CREAM("#fef3c7"),
  ROSE("#fecaca"),
  LILAC_LIGHT("#ddd6fe"),
  PINK_LIGHT("#fbcfe8"),
  GRAY_LIGHT("#e5e7eb"),
  SAND("#fed7aa"),
  CYAN_LIGHT("#a5f3fc"),

  REALIZADO("#3b82f6"),
  COMPROMETIDO("#f59e0b");

  private final String hex;

  ChartColor(String hex) {
    this.hex = hex;
  }

  public String getHex() {
    return hex;
  }
}
