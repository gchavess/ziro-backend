package br.com.ziro.lite.util.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvImportUtil {

  public static List<String[]> readCsv(String filePath) throws IOException, CsvValidationException {
    try (FileReader fr = new FileReader(filePath)) {
      return readCsv(new InputStreamReader(new FileInputStream(filePath)));
    }
  }

  public static List<String[]> readCsv(InputStream inputStream)
      throws IOException, CsvValidationException {
    return readCsv(new InputStreamReader(inputStream));
  }

  private static List<String[]> readCsv(Reader reader) throws IOException, CsvValidationException {
    try (CSVReader csvReader = new CSVReader(reader)) {
      List<String[]> lines = new ArrayList<>();
      String[] nextLine;
      while ((nextLine = csvReader.readNext()) != null) {
        lines.add(nextLine);
      }
      return lines;
    }
  }
}
