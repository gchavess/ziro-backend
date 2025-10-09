package br.com.ziro.lite.service.usuario;

import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.service.contextoconta.ContextoContaService;
import br.com.ziro.lite.service.naturezaconta.NaturezaContaService;
import br.com.ziro.lite.util.password.PasswordUtil;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordUtil passwordUtil;
  private final ContextoContaService contextoContaService;
  private final NaturezaContaService naturezaContaService;

  public UsuarioService(
      UsuarioRepository usuarioRepository,
      final PasswordUtil passwordUtil,
      final ContextoContaService contextoContaService,
      final NaturezaContaService naturezaContaService) {
    this.usuarioRepository = usuarioRepository;
    this.passwordUtil = passwordUtil;
    this.contextoContaService = contextoContaService;
    this.naturezaContaService = naturezaContaService;
  }

  public List<Usuario> listarTodos() {
    return usuarioRepository.findAll();
  }

  public Optional<Usuario> buscarPorId(Long id) {
    return usuarioRepository.findById(id);
  }

  public Usuario criar(Usuario usuario) throws Exception {
    final String senhaOriginal = usuario.getSenha();
    final String senhaHash = this.passwordUtil.hashSHA256(senhaOriginal);
    usuario.setSenha(senhaHash);
    usuario.setDataCriacao(new Date());

    final Usuario usuarioCriado = usuarioRepository.save(usuario);

    this.inicializarValoresPadroes(usuarioCriado);

    return usuarioCriado;
  }

  public void inicializarValoresPadroes(final Usuario usuarioCriado) throws Exception {
    this.contextoContaService.inicializarValoresPadroes(usuarioCriado);

    this.naturezaContaService.inicializarValoresPadroes(usuarioCriado);
  }

  public Optional<Usuario> atualizar(Long id, Usuario usuarioAtualizado) throws Exception {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
    if (usuarioOpt.isPresent()) {
      Usuario usuario = usuarioOpt.get();
      usuario.setNome(usuarioAtualizado.getNome());
      usuario.setEmail(usuarioAtualizado.getEmail());
      String novaSenha = usuarioAtualizado.getSenha();
      if (novaSenha != null && !novaSenha.isEmpty()) {
        String senhaHash = passwordUtil.hashSHA256(novaSenha);
        usuario.setSenha(senhaHash);
      }
      usuarioRepository.save(usuario);
    }
    return usuarioOpt;
  }

  public void deletar(Long id) {
    usuarioRepository.deleteById(id);
  }
}
