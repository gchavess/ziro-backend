package br.com.ziro.lite.service.csv;

import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.conta.ContaDTO;
import br.com.ziro.lite.dto.lancamento.LancamentoDTO;
import br.com.ziro.lite.mapper.csv.CsvMapper;
import br.com.ziro.lite.service.conta.ContaService;
import br.com.ziro.lite.service.lancamento.LancamentoService;
import br.com.ziro.lite.util.csv.CsvImportUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.multipart.MultipartFile;

class CsvImportServiceTest {

  private ContaService contaService;
  private LancamentoService lancamentoService;
  private CsvImportService csvImportService;

  private MultipartFile file;

  @BeforeEach
  void setup() throws Exception {
    contaService = mock(ContaService.class);
    lancamentoService = mock(LancamentoService.class);
    csvImportService = new CsvImportService(contaService, lancamentoService);

    file = mock(MultipartFile.class);
    when(file.getInputStream()).thenReturn(new ByteArrayInputStream("TXT".getBytes()));
  }

  @Test
  void deveImportarSomenteContas() throws Exception {

    List<String[]> linhas = List.<String[]>of(new String[] {"CONTA", "1", "Conta Teste", "123"});

    ContaDTO contaDTO = new ContaDTO();

    try (MockedStatic<CsvImportUtil> csvMock = mockStatic(CsvImportUtil.class);
        MockedStatic<CsvMapper> mapperMock = mockStatic(CsvMapper.class)) {

      csvMock.when(() -> CsvImportUtil.readCsv(any(InputStream.class))).thenReturn(linhas);
      mapperMock.when(() -> CsvMapper.mapToContaDTO(linhas.get(0))).thenReturn(contaDTO);

      csvImportService.importar(file);

      verify(contaService, times(1)).salvar(contaDTO);
      verify(lancamentoService, never()).salvar(any());
    }
  }

  @Test
  void deveImportarSomenteLancamentos() throws Exception {

    List<String[]> linhas =
        List.<String[]>of(new String[] {"LANCAMENTO", "10", "100.50", "2024-01-01"});

    LancamentoDTO lancDTO = new LancamentoDTO();

    try (MockedStatic<CsvImportUtil> csvMock = mockStatic(CsvImportUtil.class);
        MockedStatic<CsvMapper> mapperMock = mockStatic(CsvMapper.class)) {

      csvMock.when(() -> CsvImportUtil.readCsv(any(InputStream.class))).thenReturn(linhas);
      mapperMock.when(() -> CsvMapper.mapToLancamentoDTO(linhas.get(0))).thenReturn(lancDTO);

      csvImportService.importar(file);

      verify(lancamentoService, times(1)).salvar(lancDTO);
      verify(contaService, never()).salvar(any());
    }
  }

  @Test
  void deveImportarContasELancamentos() throws Exception {

    List<String[]> linhas =
        List.of(
            new String[] {"CONTA", "1", "Conta Teste", "123"},
            new String[] {"LANCAMENTO", "10", "100.50", "2024-01-01"});

    ContaDTO contaDTO = new ContaDTO();
    LancamentoDTO lancDTO = new LancamentoDTO();

    try (MockedStatic<CsvImportUtil> csvMock = mockStatic(CsvImportUtil.class);
        MockedStatic<CsvMapper> mapperMock = mockStatic(CsvMapper.class)) {

      csvMock.when(() -> CsvImportUtil.readCsv(any(InputStream.class))).thenReturn(linhas);

      mapperMock.when(() -> CsvMapper.mapToContaDTO(linhas.get(0))).thenReturn(contaDTO);
      mapperMock.when(() -> CsvMapper.mapToLancamentoDTO(linhas.get(1))).thenReturn(lancDTO);

      csvImportService.importar(file);

      verify(contaService, times(1)).salvar(contaDTO);
      verify(lancamentoService, times(1)).salvar(lancDTO);
    }
  }
}
