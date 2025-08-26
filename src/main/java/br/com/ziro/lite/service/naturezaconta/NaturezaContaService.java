package br.com.ziro.lite.service.naturezaconta;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import br.com.ziro.lite.dto.naturezaconta.NaturezaContaAgrupadaDTO;
import br.com.ziro.lite.dto.naturezaconta.NaturezaContaDTO;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.entity.naturezaconta.NaturezaConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.exception.contextoconta.ContextoContaNaoEncontradoException;
import br.com.ziro.lite.exception.naturezaconta.NaturezaContaNaoEncontradoException;
import br.com.ziro.lite.repository.contextoconta.ContextoContaRepository;
import br.com.ziro.lite.repository.naturezaconta.NaturezaContaRepository;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaturezaContaService {

  private final NaturezaContaRepository repository;
  private final UsuarioRepository usuarioRepository;
  private final ContextoContaRepository contextoRepositoru;
  private final UsuarioLogado usuarioLogado;

  public List<NaturezaContaDTO> listarTodos() {
    return repository.findAll().stream().map(NaturezaContaDTO::fromEntity).toList();
  }

  public NaturezaContaDTO buscarPorId(Long id) throws NaturezaContaNaoEncontradoException {
    return repository
        .findById(id)
        .map(NaturezaContaDTO::fromEntity)
        .orElseThrow(() -> new NaturezaContaNaoEncontradoException());
  }

  public NaturezaContaDTO salvar(NaturezaContaDTO request)
      throws ContextoContaNaoEncontradoException {
    NaturezaConta entity = new NaturezaConta();
    entity.setDescricao(request.getDescricao());
    entity.setObservacao(request.getObservacao());
    entity.setCodigo(request.getCodigo());

    final Usuario usuarioCriacao = new Usuario();
    usuarioCriacao.setId(usuarioLogado.get().getId());

    entity.setUsuarioCriacao(usuarioCriacao);
    entity.setContextoConta(
        contextoRepositoru
            .findById(request.getContextoConta().getId())
            .orElseThrow(() -> new ContextoContaNaoEncontradoException()));
    entity.setDataCriacao(new Date());

    return NaturezaContaDTO.fromEntity(repository.save(entity));
  }

  public NaturezaContaDTO atualizar(Long id, NaturezaContaDTO request)
      throws ContextoContaNaoEncontradoException, NaturezaContaNaoEncontradoException {
    NaturezaConta entity =
        repository.findById(id).orElseThrow(() -> new NaturezaContaNaoEncontradoException());

    entity.setDescricao(request.getDescricao());
    entity.setObservacao(request.getObservacao());
    entity.setCodigo(request.getCodigo());
    entity.setContextoConta(
        contextoRepositoru
            .findById(request.getContextoConta().getId())
            .orElseThrow(() -> new ContextoContaNaoEncontradoException()));

    return NaturezaContaDTO.fromEntity(repository.save(entity));
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }

  public List<NaturezaContaAgrupadaDTO> listarAgrupadasPorContexto() {
    // Buscar todos os contextos e naturezas
    List<ContextoConta> contextos = this.contextoRepositoru.findAll();
    List<NaturezaConta> naturezas = this.repository.findAll();

    // Indexar as naturezas por contexto
    Map<Long, List<NaturezaConta>> grouped =
        naturezas.stream().collect(Collectors.groupingBy(n -> n.getContextoConta().getId()));

    // Montar os DTOs
    return contextos.stream()
        .map(
            contexto -> {
              List<NaturezaContaAgrupadaDTO> filhos =
                  grouped.getOrDefault(contexto.getId(), List.of()).stream()
                      .map(
                          n ->
                              new NaturezaContaAgrupadaDTO(
                                  n.getId(),
                                  n.getDescricao(),
                                  null,
                                  n.getCodigo(),
                                  n.getDescricao(),
                                  n.getObservacao(),
                                  ContextoContaDTO.fromEntity(n.getContextoConta())))
                      .toList();

              return new NaturezaContaAgrupadaDTO(
                  contexto.getId(),
                  contexto.getDescricao(),
                  filhos,
                  contexto.getCodigo(),
                  contexto.getDescricao(),
                  contexto.getObservacao(),
                  null);
            })
        .toList();
  }
}
