package br.com.ziro.lite.controller.csv;

import br.com.ziro.lite.service.csv.CsvImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Tag(name = "Import", description = "Operações relacionadas às importações")
public class CsvImportController {

  private final CsvImportService csvImportService;

  @Operation(
      summary = "Importa lançamentos e contas via CSV",
      description =
          "Recebe um arquivo CSV contendo lançamentos financeiros (transferências) e contas/categorias. "
              + "Valida o conteúdo e persiste os dados no sistema.")
  @PostMapping
  public void importar(MultipartFile file) throws Exception {
    csvImportService.importar(file);
  }
}
