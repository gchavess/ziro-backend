package br.com.ziro.lite.service.contextoconta;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.exception.contextoconta.ContextoContaNaoEncontradoException;
import br.com.ziro.lite.repository.contextoconta.ContextoContaRepository;
import br.com.ziro.lite.repository.naturezaconta.NaturezaContaRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import br.com.ziro.lite.util.usuariopadrao.UsuarioPadraoUtil;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ContextoContaService {

  private final ContextoContaRepository repository;
  private final UsuarioLogado usuarioLogado;
  private final NaturezaContaRepository naturezaContaRepository;

  public ContextoContaService(
      final ContextoContaRepository repository,
      final UsuarioLogado usuarioLogado,
      final NaturezaContaRepository naturezaContaRepository) {

    this.repository = repository;
    this.usuarioLogado = usuarioLogado;
    this.naturezaContaRepository = naturezaContaRepository;
  }

  public List<ContextoContaDTO> listarTodos() {

    return repository
        .findAllByUsuarioCriacaoOrderByDataCriacaoAsc(usuarioLogado.getCurrent())
        .stream()
        .map(ContextoContaDTO::fromEntity)
        .toList();
  }

  public Optional<ContextoConta> buscarPorId(Long id) {
    return repository.findById(id);
  }

  public ContextoContaDTO salvar(ContextoContaDTO request) {

    ContextoConta entity = new ContextoConta();
    entity.setDescricao(request.getDescricao());
    entity.setObservacao(request.getObservacao());
    entity.setCodigo(request.getCodigo());
    entity.setPadrao(request.getPadrao());

    final Usuario usuarioCriacao = new Usuario();

    if (request.getUsuarioCriacao() != null && request.getUsuarioCriacao().getId() != null) {
      usuarioCriacao.setId(request.getUsuarioCriacao().getId());
    } else {
      usuarioCriacao.setId(usuarioLogado.getCurrentDTO().getId());
    }

    entity.setUsuarioCriacao(usuarioCriacao);

    if (request.getContextoContaPadraoId() != null) {
      entity.setContextoContaPadraoId(request.getContextoContaPadraoId());
    }

    entity.setDataCriacao(new Date());

    return ContextoContaDTO.fromEntity(repository.save(entity));
  }

  public ContextoContaDTO atualizar(Long id, ContextoConta request)
      throws ContextoContaNaoEncontradoException {
    ContextoConta entity =
        repository.findById(id).orElseThrow(() -> new ContextoContaNaoEncontradoException());

    entity.setDescricao(request.getDescricao());
    entity.setObservacao(request.getObservacao());
    entity.setCodigo(request.getCodigo());

    return ContextoContaDTO.fromEntity(repository.save(entity));
  }

  @Transactional
  public void deletar(Long id) {

    naturezaContaRepository.deleteByContextoId(id);

    repository.deleteById(id);
  }

  public void inicializarValoresPadroes(final Usuario usuarioCriado) throws Exception {
    var usuarioPadrao = UsuarioPadraoUtil.get();

    boolean jaExiste = repository.existsByUsuarioCriacaoAndPadrao(usuarioPadrao, false);
    if (jaExiste) {
      return;
    }

    List<ContextoConta> listaPadrao =
        repository.findAllByUsuarioCriacaoAndPadrao(usuarioPadrao, true);

    listaPadrao.stream()
        .map(
            padrao -> {
              ContextoContaDTO novo = ContextoContaDTO.inicializarValoresPadroes(padrao);
              novo.setUsuarioCriacao(UsuarioDTO.fromEntity(usuarioCriado));
              return this.salvar(novo);
            })
        .toList();
  }
}
