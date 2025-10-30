package br.com.ziro.lite.repository.lancamento;

import br.com.ziro.lite.entity.lancamento.Lancamento;
import br.com.ziro.lite.entity.usuario.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

  @Query(
      "SELECT l FROM Lancamento l WHERE l.usuarioCriacao = :usuarioCriacao ORDER BY l.dataCriacao ASC")
  List<Lancamento> findAllByUsuarioCriacao(@Param("usuarioCriacao") final Usuario usuarioCriacao);
}
