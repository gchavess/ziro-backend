package br.com.ziro.lite.repository.contextoconta;

import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContextoContaRepository extends JpaRepository<ContextoConta, Long> {
  List<ContextoConta> findAllByUsuarioCriacao(final Usuario usuarioCriacao);
}
