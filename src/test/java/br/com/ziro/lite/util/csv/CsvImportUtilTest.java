package br.com.ziro.lite.util.csv;

import static org.junit.jupiter.api.Assertions.*;

import com.opencsv.exceptions.CsvValidationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class CsvImportUtilTest {

  // ------------------------------
  // 1. Ler CSV de InputStream
  // ------------------------------
  @Test
  void readCsv_deveLerCsvDeInputStream() throws IOException, CsvValidationException {
    String csvContent = "nome,idade\nJoao,30\nMaria,25";
    InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

    List<String[]> lines = CsvImportUtil.readCsv(inputStream);

    assertEquals(3, lines.size());
    assertArrayEquals(new String[] {"nome", "idade"}, lines.get(0));
    assertArrayEquals(new String[] {"Joao", "30"}, lines.get(1));
    assertArrayEquals(new String[] {"Maria", "25"}, lines.get(2));
  }

  // ------------------------------
  // 2. Ler CSV de filePath
  // ------------------------------
  @Test
  void readCsv_deveLerCsvDeFilePath() throws IOException, CsvValidationException {
    Path tempFile = Files.createTempFile("test", ".csv");
    String csvContent = "produto,preco\nCaneta,2.5\nCaderno,10.0";
    Files.writeString(tempFile, csvContent);

    List<String[]> lines = CsvImportUtil.readCsv(tempFile.toString());

    assertEquals(3, lines.size());
    assertArrayEquals(new String[] {"produto", "preco"}, lines.get(0));
    assertArrayEquals(new String[] {"Caneta", "2.5"}, lines.get(1));
    assertArrayEquals(new String[] {"Caderno", "10.0"}, lines.get(2));

    Files.deleteIfExists(tempFile);
  }

  // ------------------------------
  // 3. Arquivo vazio → retorna lista vazia
  // ------------------------------
  @Test
  void readCsv_deveRetornarListaVaziaParaArquivoVazio() throws IOException, CsvValidationException {
    String csvContent = "";
    InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

    List<String[]> lines = CsvImportUtil.readCsv(inputStream);

    assertTrue(lines.isEmpty());
  }
}
