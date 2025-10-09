package br.com.ziro.lite.repository.lancamento;

import br.com.ziro.lite.entity.lancamento.Lancamento;
import br.com.ziro.lite.entity.usuario.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
  List<Lancamento> findAllByUsuarioCriacao(final Usuario usuarioCriacao);
}
