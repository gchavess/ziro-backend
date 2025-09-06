package br.com.ziro.lite.util.date;

import java.time.format.DateTimeFormatter;

public final class DateFormatterUtil {

  private DateFormatterUtil() {}

  public static final DateTimeFormatter DIA_MES_ANO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  public static final DateTimeFormatter MES_ANO = DateTimeFormatter.ofPattern("MM/yyyy");
}
