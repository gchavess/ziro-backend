package br.com.ziro.lite.service.csv;

import br.com.ziro.lite.dto.conta.ContaDTO;
import br.com.ziro.lite.dto.lancamento.LancamentoDTO;
import br.com.ziro.lite.mapper.csv.CsvMapper;
import br.com.ziro.lite.service.conta.ContaService;
import br.com.ziro.lite.service.lancamento.LancamentoService;
import br.com.ziro.lite.util.csv.CsvImportUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvImportService {

  private final ContaService contaService;
  private final LancamentoService lancamentoService;

  public CsvImportService(
      final ContaService contaService, final LancamentoService lancamentoService) {

    this.contaService = contaService;
    this.lancamentoService = lancamentoService;
  }

  public void importar(MultipartFile file) throws Exception {
    List<String[]> linhas = CsvImportUtil.readCsv(file.getInputStream());

    List<ContaDTO> contas = new ArrayList<>();
    List<LancamentoDTO> lancamentos = new ArrayList<>();

    for (String[] linha : linhas) {
      String tipo = linha[0];

      if ("CONTA".equalsIgnoreCase(tipo)) {
        contas.add(CsvMapper.mapToContaDTO(linha));
      } else if ("LANCAMENTO".equalsIgnoreCase(tipo)) {
        lancamentos.add(CsvMapper.mapToLancamentoDTO(linha));
      }
    }

    if (!contas.isEmpty()) {
      for (ContaDTO contaDTO : contas) {
        contaService.salvar(contaDTO);
      }
    }

    if (!lancamentos.isEmpty()) {
      for (LancamentoDTO lancamentoDTO : lancamentos) {
        lancamentoService.salvar(lancamentoDTO);
      }
    }
  }
}
