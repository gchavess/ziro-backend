package br.com.ziro.lite.service.analisefinanceira;

import br.com.ziro.lite.dto.analisefinanceira.usuario.AnaliseFinanceiraDTO;
import br.com.ziro.lite.entity.analisefinanceira.AnaliseFinanceira;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.exception.analisefinanceira.AnaliseFinanceiraNaoEncontradaException;
import br.com.ziro.lite.repository.analisefinanceira.AnaliseFinanceiraRepository;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnaliseFinanceiraService {

  private final AnaliseFinanceiraRepository repository;
  private final UsuarioRepository usuarioRepository;
  private final UsuarioLogado usuarioLogado;

  public List<AnaliseFinanceiraDTO> listarTodos() {
    return repository
        .findAllByUsuarioCriacaoOrderByDataCriacaoAsc(usuarioLogado.getCurrent())
        .stream()
        .map(AnaliseFinanceiraDTO::fromEntity)
        .toList();
  }

  public AnaliseFinanceiraDTO buscarPorId(Long id) throws AnaliseFinanceiraNaoEncontradaException {
    return repository
        .findById(id)
        .map(AnaliseFinanceiraDTO::fromEntity)
        .orElseThrow(AnaliseFinanceiraNaoEncontradaException::new);
  }

  public AnaliseFinanceiraDTO criar(AnaliseFinanceiraDTO request) {
    AnaliseFinanceira entity = new AnaliseFinanceira();

    Usuario usuarioCriacao =
        usuarioRepository
            .findById(usuarioLogado.getCurrentDTO().getId())
            .orElseThrow(RuntimeException::new);

    entity.setUsuarioCriacao(usuarioCriacao);
    entity.setDataCriacao(new Date());
    entity.setDescricao(request.getDescricao());
    entity.setObservacao(request.getObservacao());
    entity.setCodigo(request.getCodigo());
    entity.setFato(request.getFato());
    entity.setCausa(request.getCausa());
    entity.setAcao(request.getAcao());

    return AnaliseFinanceiraDTO.fromEntity(repository.save(entity));
  }

  public AnaliseFinanceiraDTO atualizar(Long id, AnaliseFinanceiraDTO request)
      throws AnaliseFinanceiraNaoEncontradaException {

    AnaliseFinanceira entity =
        repository.findById(id).orElseThrow(AnaliseFinanceiraNaoEncontradaException::new);

    if (request.getDescricao() != null) entity.setDescricao(request.getDescricao());
    if (request.getObservacao() != null) entity.setObservacao(request.getObservacao());
    if (request.getCodigo() != null) entity.setCodigo(request.getCodigo());
    if (request.getFato() != null) entity.setFato(request.getFato());
    if (request.getCausa() != null) entity.setCausa(request.getCausa());
    if (request.getAcao() != null) entity.setAcao(request.getAcao());

    return AnaliseFinanceiraDTO.fromEntity(repository.save(entity));
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }
}
