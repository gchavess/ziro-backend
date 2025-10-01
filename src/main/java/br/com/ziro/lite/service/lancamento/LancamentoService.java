package br.com.ziro.lite.service.lancamento;

import br.com.ziro.lite.dto.lancamento.LancamentoDTO;
import br.com.ziro.lite.dto.lancamento.LancamentoGraficoCelulaDTO;
import br.com.ziro.lite.dto.lancamento.LancamentoGraficoDTO;
import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.lancamento.Lancamento;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.enums.ChartColor;
import br.com.ziro.lite.enums.ColunaSimplificadaBI;
import br.com.ziro.lite.exception.conta.ContaNaoEncontradoException;
import br.com.ziro.lite.exception.lancamento.LancamentoNaoEncontradoException;
import br.com.ziro.lite.repository.conta.ContaRepository;
import br.com.ziro.lite.repository.lancamento.LancamentoRepository;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import br.com.ziro.lite.util.date.DateFormatterUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LancamentoService {

  private final LancamentoRepository repository;
  private final ContaRepository contaRepository;
  private final UsuarioRepository usuarioRepository;
  private final UsuarioLogado usuarioLogado;

  public List<LancamentoDTO> listarTodos() {
    return repository.findAllByUsuarioCriacao(usuarioLogado.getCurrent()).stream()
        .map(LancamentoDTO::fromEntity)
        .toList();
  }

  public LancamentoDTO buscarPorId(Long id) throws LancamentoNaoEncontradoException {
    return repository
        .findById(id)
        .map(LancamentoDTO::fromEntity)
        .orElseThrow(LancamentoNaoEncontradoException::new);
  }

  public LancamentoDTO salvar(LancamentoDTO request) throws ContaNaoEncontradoException {
    Lancamento entity = new Lancamento();

    Usuario usuarioCriacao =
        usuarioRepository
            .findById(usuarioLogado.getCurrentDTO().getId())
            .orElseThrow(RuntimeException::new);

    entity.setUsuarioCriacao(usuarioCriacao);
    entity.setDataCriacao(new Date());
    entity.setDescricao(request.getDescricao());
    entity.setObservacao(request.getObservacao());
    entity.setDataVencimento(request.getDataVencimento());
    entity.setDataPagamento(request.getDataPagamento());
    entity.setValorBruto(request.getValorBruto());

    if (request.getConta() != null && request.getConta().getId() != null) {
      Conta conta =
          contaRepository
              .findById(request.getConta().getId())
              .orElseThrow(ContaNaoEncontradoException::new);
      entity.setConta(conta);
    }

    if (request.getContaCodigo() != null) {
      System.out.println("Entrou IF: " + request.getContaCodigo());

      entity.setConta(
          contaRepository
              .findByUsuarioCriacaoAndCodigo(usuarioCriacao, request.getContaCodigo())
              .orElseThrow(ContaNaoEncontradoException::new));
    }

    return LancamentoDTO.fromEntity(repository.save(entity));
  }

  public LancamentoDTO atualizar(Long id, LancamentoDTO request)
      throws LancamentoNaoEncontradoException, ContaNaoEncontradoException {
    Lancamento entity = repository.findById(id).orElseThrow(LancamentoNaoEncontradoException::new);

    if (request.getDescricao() != null) entity.setDescricao(request.getDescricao());
    if (request.getObservacao() != null) entity.setObservacao(request.getObservacao());
    if (request.getDataVencimento() != null) entity.setDataVencimento(request.getDataVencimento());
    if (request.getDataPagamento() != null) entity.setDataPagamento(request.getDataPagamento());
    if (request.getValorBruto() != null) entity.setValorBruto(request.getValorBruto());

    if (request.getConta() != null && request.getConta().getId() != null) {
      Conta conta =
          contaRepository
              .findById(request.getConta().getId())
              .orElseThrow(ContaNaoEncontradoException::new);
      entity.setConta(conta);
    }

    return LancamentoDTO.fromEntity(repository.save(entity));
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }

  public List<LancamentoGraficoCelulaDTO> listarParaGrafico() {
    return repository.findAllByUsuarioCriacao(usuarioLogado.getCurrent()).stream()
        .map(LancamentoGraficoCelulaDTO::fromEntity)
        .toList();
  }

  public LancamentoGraficoDTO montarGraficoLinhaComFiltro(
      LocalDate dataInicio, LocalDate dataFim, Long contextoId, Long naturezaId) {

    // Formato para parse de data dos lançamentos
    // Listar e filtrar lançamentos pelo período
    List<LancamentoGraficoCelulaDTO> lancamentos =
        listarParaGrafico().stream()
            .filter(
                l -> {
                  if (l.dataPagamento() == null) return false;
                  LocalDate data =
                      LocalDate.parse(l.dataPagamento(), DateFormatterUtil.DIA_MES_ANO);
                  return !data.isBefore(dataInicio) && !data.isAfter(dataFim);
                })
            .filter(l -> l.contextoDescricao() != null) // somente com contexto
            .collect(Collectors.toList());

    // Gerar todos os meses do período
    List<LocalDate> mesesDoPeriodo = new ArrayList<>();
    LocalDate mesAtual = LocalDate.of(dataInicio.getYear(), dataInicio.getMonth(), 1);
    LocalDate mesFim = LocalDate.of(dataFim.getYear(), dataFim.getMonth(), 1);
    while (!mesAtual.isAfter(mesFim)) {
      mesesDoPeriodo.add(mesAtual);
      mesAtual = mesAtual.plusMonths(1);
    }

    List<String> months =
        mesesDoPeriodo.stream().map(m -> m.format(DateFormatterUtil.MES_ANO)).toList();

    // Agrupamento
    Map<String, List<LancamentoGraficoCelulaDTO>> lancamentosAgrupados;

    if (naturezaId != null) {
      lancamentosAgrupados =
          lancamentos.stream()
              .filter(l -> l.naturezaId() != null && l.naturezaId().equals(naturezaId))
              .collect(Collectors.groupingBy(LancamentoGraficoCelulaDTO::contaDescricao));
    } else if (contextoId != null) {
      lancamentosAgrupados =
          lancamentos.stream()
              .filter(l -> contextoId.equals(l.contextoId()))
              .collect(Collectors.groupingBy(LancamentoGraficoCelulaDTO::naturezaDescricao));
    } else {
      lancamentosAgrupados =
          lancamentos.stream()
              .collect(Collectors.groupingBy(LancamentoGraficoCelulaDTO::contextoDescricao));
    }

    ChartColor[] coresDisponiveis = ChartColor.values();

    List<LancamentoGraficoDTO.Dataset> datasets = new ArrayList<>();
    int indexCor = 0;

    for (Map.Entry<String, List<LancamentoGraficoCelulaDTO>> entry :
        lancamentosAgrupados.entrySet()) {
      String label = entry.getKey();
      List<LancamentoGraficoCelulaDTO> lancamentosGrupo = entry.getValue();

      List<BigDecimal> data = new ArrayList<>();
      for (LocalDate mes : mesesDoPeriodo) {
        BigDecimal totalMes =
            lancamentosGrupo.stream()
                .filter(
                    l -> {
                      if (l.dataPagamento() == null) return false;
                      LocalDate dataTotal =
                          LocalDate.parse(l.dataPagamento(), DateFormatterUtil.DIA_MES_ANO);
                      return dataTotal.getMonth().equals(mes.getMonth())
                          && dataTotal.getYear() == mes.getYear();
                    })
                .map(LancamentoGraficoCelulaDTO::valorBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        data.add(totalMes);
      }

      String cor = coresDisponiveis[indexCor % coresDisponiveis.length].getHex();
      datasets.add(new LancamentoGraficoDTO.Dataset(label, data, cor));
      indexCor++;
    }

    return new LancamentoGraficoDTO(months, datasets);
  }

  public LancamentoGraficoDTO montarGraficoLinhaSimplificadoComFiltro(
      LocalDate dataInicio, LocalDate dataFim, Long contextoId, Long naturezaId) {

    List<LancamentoGraficoCelulaDTO> lancamentos =
        listarParaGrafico().stream()
            .filter(l -> l.dataPagamento() != null || l.dataVencimento() != null)
            .filter(
                l -> {
                  if (l.dataPagamento() != null) {
                    LocalDate data =
                        LocalDate.parse(l.dataPagamento(), DateFormatterUtil.DIA_MES_ANO);
                    return !data.isBefore(dataInicio) && !data.isAfter(dataFim);
                  } else {
                    LocalDate data =
                        LocalDate.parse(l.dataVencimento(), DateFormatterUtil.DIA_MES_ANO);
                    return !data.isBefore(dataInicio) && !data.isAfter(dataFim);
                  }
                })
            .collect(Collectors.toList());

    List<LocalDate> mesesDoPeriodo = new ArrayList<>();
    LocalDate mesAtual = LocalDate.of(dataInicio.getYear(), dataInicio.getMonth(), 1);
    LocalDate mesFim = LocalDate.of(dataFim.getYear(), dataFim.getMonth(), 1);
    while (!mesAtual.isAfter(mesFim)) {
      mesesDoPeriodo.add(mesAtual);
      mesAtual = mesAtual.plusMonths(1);
    }

    List<String> months =
        mesesDoPeriodo.stream().map(m -> m.format(DateFormatterUtil.MES_ANO)).toList();

    List<LancamentoGraficoCelulaDTO> lancamentosFiltrados =
        lancamentos.stream()
            .filter(
                l ->
                    (naturezaId == null
                        || (l.naturezaId() != null && l.naturezaId().equals(naturezaId))))
            .filter(
                l ->
                    (contextoId == null
                        || (l.contextoId() != null && l.contextoId().equals(contextoId))))
            .toList();

    List<BigDecimal> realizados = new ArrayList<>();
    List<BigDecimal> comprometidos = new ArrayList<>();

    for (LocalDate mes : mesesDoPeriodo) {
      BigDecimal totalRealizado =
          lancamentosFiltrados.stream()
              .filter(l -> l.dataPagamento() != null)
              .filter(
                  l -> {
                    LocalDate data =
                        LocalDate.parse(l.dataPagamento(), DateFormatterUtil.DIA_MES_ANO);
                    return data.getMonth() == mes.getMonth() && data.getYear() == mes.getYear();
                  })
              .map(LancamentoGraficoCelulaDTO::valorBruto)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      realizados.add(totalRealizado);

      BigDecimal totalComprometido =
          lancamentosFiltrados.stream()
              .filter(l -> l.dataVencimento() != null && l.dataPagamento() == null)
              .filter(
                  l -> {
                    LocalDate data =
                        LocalDate.parse(l.dataVencimento(), DateFormatterUtil.DIA_MES_ANO);
                    return data.getMonth() == mes.getMonth() && data.getYear() == mes.getYear();
                  })
              .map(LancamentoGraficoCelulaDTO::valorBruto)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      comprometidos.add(totalComprometido);
    }

    List<LancamentoGraficoDTO.Dataset> datasets = new ArrayList<>();
    datasets.add(
        new LancamentoGraficoDTO.Dataset(
            ColunaSimplificadaBI.REALIZADO.getDescricao(),
            realizados,
            ColunaSimplificadaBI.REALIZADO.getCor()));
    datasets.add(
        new LancamentoGraficoDTO.Dataset(
            ColunaSimplificadaBI.COMPROMETIDO.getDescricao(),
            comprometidos,
            ColunaSimplificadaBI.COMPROMETIDO.getCor()));

    return new LancamentoGraficoDTO(months, datasets);
  }
}
