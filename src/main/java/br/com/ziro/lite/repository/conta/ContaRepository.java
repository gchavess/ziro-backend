package br.com.ziro.lite.repository.conta;

import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.usuario.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
  List<Conta> findAllByUsuarioCriacao(final Usuario usuarioCriacao);
}
