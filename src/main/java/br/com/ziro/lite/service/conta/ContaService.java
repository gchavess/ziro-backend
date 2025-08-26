package br.com.ziro.lite.service.conta;

import br.com.ziro.lite.dto.conta.AssociarNaturezaContaDTO;
import br.com.ziro.lite.dto.conta.ContaDTO;
import br.com.ziro.lite.dto.conta.ContaTreeNodeDTO;
import br.com.ziro.lite.dto.naturezaconta.NaturezaContaDTO;
import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.naturezaconta.NaturezaConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.exception.conta.ContaNaoEncontradoException;
import br.com.ziro.lite.exception.conta.ContaPaiNaoEncontradoException;
import br.com.ziro.lite.exception.naturezaconta.NaturezaContaNaoEncontradoException;
import br.com.ziro.lite.exception.usuario.UsuarioNaoEncontradoException;
import br.com.ziro.lite.repository.conta.ContaRepository;
import br.com.ziro.lite.repository.naturezaconta.NaturezaContaRepository;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContaService {

  private final ContaRepository repository;
  private final NaturezaContaRepository naturezaContaRepository;
  private final UsuarioRepository usuarioRepository;
  private final UsuarioLogado usuarioLogado;

  public List<ContaDTO> listarTodos() {
    return repository.findAll().stream().map(ContaDTO::fromEntity).toList();
  }

  public ContaDTO buscarPorId(Long id) throws ContaNaoEncontradoException {
    return repository
        .findById(id)
        .map(ContaDTO::fromEntity)
        .orElseThrow(ContaNaoEncontradoException::new);
  }

  public ContaDTO salvar(ContaDTO request)
      throws UsuarioNaoEncontradoException,
          NaturezaContaNaoEncontradoException,
          ContaPaiNaoEncontradoException {
    Conta entity = new Conta();

    Usuario usuarioCriacao =
        usuarioRepository
            .findById(usuarioLogado.get().getId())
            .orElseThrow(UsuarioNaoEncontradoException::new);

    entity.setUsuarioCriacao(usuarioCriacao);

    if (request.getDescricao() != null) {
      entity.setDescricao(request.getDescricao());
    }

    if (request.getObservacao() != null) {
      entity.setObservacao(request.getObservacao());
    }

    if (request.getCodigo() != null) {
      entity.setCodigo(request.getCodigo());
    }

    if (request.getNaturezaConta() != null && request.getNaturezaConta().getId() != null) {
      NaturezaConta naturezaConta =
          naturezaContaRepository
              .findById(request.getNaturezaConta().getId())
              .orElseThrow(NaturezaContaNaoEncontradoException::new);
      entity.setNaturezaConta(naturezaConta);
    }

    if (request.getPaiId() != null) {
      entity.setPai(
          repository.findById(request.getPaiId()).orElseThrow(ContaPaiNaoEncontradoException::new));
    }

    entity.setDataCriacao(new Date());

    return ContaDTO.fromEntity(repository.save(entity));
  }

  public ContaDTO atualizar(Long id, ContaDTO request)
      throws NaturezaContaNaoEncontradoException,
          ContaNaoEncontradoException,
          ContaPaiNaoEncontradoException {
    Conta entity = repository.findById(id).orElseThrow(ContaNaoEncontradoException::new);

    if (request.getDescricao() != null) {
      entity.setDescricao(request.getDescricao());
    }

    if (request.getObservacao() != null) {
      entity.setObservacao(request.getObservacao());
    }

    if (request.getCodigo() != null) {
      entity.setCodigo(request.getCodigo());
    }

    if (request.getNaturezaConta() != null && request.getNaturezaConta().getId() != null) {
      NaturezaConta naturezaConta =
          naturezaContaRepository
              .findById(request.getNaturezaConta().getId())
              .orElseThrow(NaturezaContaNaoEncontradoException::new);
      entity.setNaturezaConta(naturezaConta);
    }

    if (request.getPaiId() != null) {
      entity.setPai(
          repository.findById(request.getPaiId()).orElseThrow(ContaPaiNaoEncontradoException::new));
    } else {
      entity.setPai(null);
    }

    return ContaDTO.fromEntity(repository.save(entity));
  }

  public List<ContaDTO> associarNatureza(AssociarNaturezaContaDTO request)
      throws NaturezaContaNaoEncontradoException, ContaNaoEncontradoException {
    NaturezaConta natureza =
        naturezaContaRepository
            .findById(request.getNaturezaId())
            .orElseThrow(NaturezaContaNaoEncontradoException::new);

    List<ContaDTO> contasAtualizadas = new ArrayList<>();

    for (Long contaId : request.getContas()) {
      Conta entity = repository.findById(contaId).orElseThrow(ContaNaoEncontradoException::new);

      entity.setNaturezaConta(natureza);

      Conta entitySaved = repository.save(entity);

      contasAtualizadas.add(ContaDTO.fromEntity(entitySaved));
    }

    return contasAtualizadas;
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }

  public List<ContaTreeNodeDTO> getTree() {
    List<Conta> contas = repository.findAll();

    // transforma todas as contas em TreeNodeDTO
    Map<Long, ContaTreeNodeDTO> nodeMap =
        contas.stream()
            .collect(
                Collectors.toMap(
                    Conta::getId,
                    conta ->
                        ContaTreeNodeDTO.builder()
                            .id(conta.getId())
                            .label(conta.getDescricao())
                            .expanded(false)
                            .natureza(
                                conta.getNaturezaConta() != null
                                    ? NaturezaContaDTO.fromEntity(conta.getNaturezaConta())
                                    : null)
                            .children(new ArrayList<>())
                            .codigo(conta.getCodigo())
                            .descricao(conta.getDescricao())
                            .observacao(conta.getObservacao())
                            .build()));

    List<ContaTreeNodeDTO> roots = new ArrayList<>();

    // conecta pais e filhos
    for (Conta conta : contas) {
      if (conta.getPai() != null) {
        ContaTreeNodeDTO paiNode = nodeMap.get(conta.getPai().getId());
        ContaTreeNodeDTO filhoNode = nodeMap.get(conta.getId());
        paiNode.getChildren().add(filhoNode);
      } else {
        roots.add(nodeMap.get(conta.getId())); // sem pai → raiz
      }
    }

    return roots;
  }
}
