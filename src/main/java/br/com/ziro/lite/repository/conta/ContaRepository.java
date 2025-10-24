package br.com.ziro.lite.repository.conta;

import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.usuario.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

  @Query("SELECT c FROM Conta c WHERE c.usuarioCriacao = :usuario ORDER BY c.dataCriacao ASC")
  List<Conta> findAllByUsuarioCriacao(@Param("usuario") Usuario usuarioCriacao);

  Optional<Conta> findByUsuarioCriacaoAndCodigo(final Usuario usuarioCriacao, final String codigo);
}
