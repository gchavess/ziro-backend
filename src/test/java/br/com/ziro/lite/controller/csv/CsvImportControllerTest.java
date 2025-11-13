package br.com.ziro.lite.controller.csv;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.ziro.lite.service.csv.CsvImportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class CsvImportControllerTest {

  @Mock private CsvImportService csvImportService;

  @InjectMocks private CsvImportController controller;

  private MultipartFile mockFile;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    String csvContent = "descricao,valor,data\nPagamento 1,100.00,2025-11-13";
    mockFile = new MockMultipartFile("file", "teste.csv", "text/csv", csvContent.getBytes());
  }

  @Test
  void importar_deveChamarServico() throws Exception {
    controller.importar(mockFile);

    verify(csvImportService, times(1)).importar(mockFile);
  }
}
