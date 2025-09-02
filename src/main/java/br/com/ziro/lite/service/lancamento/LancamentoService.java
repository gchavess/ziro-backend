package br.com.ziro.lite.service.lancamento;

import br.com.ziro.lite.dto.lancamento.LancamentoDTO;
import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.lancamento.Lancamento;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.exception.conta.ContaNaoEncontradoException;
import br.com.ziro.lite.exception.lancamento.LancamentoNaoEncontradoException;
import br.com.ziro.lite.repository.conta.ContaRepository;
import br.com.ziro.lite.repository.lancamento.LancamentoRepository;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import java.util.Date;
import java.util.List;
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
}
