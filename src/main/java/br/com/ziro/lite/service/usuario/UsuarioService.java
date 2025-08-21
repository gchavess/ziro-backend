package br.com.ziro.lite.service.usuario;

import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.util.password.PasswordUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private final PasswordUtil passwordUtil;

  public UsuarioService(UsuarioRepository usuarioRepository, final PasswordUtil passwordUtil) {
    this.usuarioRepository = usuarioRepository;
    this.passwordUtil = passwordUtil;
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

    return usuarioRepository.save(usuario);
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
