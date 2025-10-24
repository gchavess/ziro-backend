package br.com.ziro.lite.repository.naturezaconta;

import br.com.ziro.lite.entity.naturezaconta.NaturezaConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturezaContaRepository extends JpaRepository<NaturezaConta, Long> {

  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("DELETE FROM NaturezaConta n WHERE n.contextoConta.id = :contextoId")
  void deleteByContextoId(final Long contextoId);

  List<NaturezaConta> findAllByUsuarioCriacaoOrderByDataCriacaoAsc(Usuario usuarioCriacao);

  List<NaturezaConta> findAllByUsuarioCriacaoAndPadrao(
      final Usuario usuarioCriacao, final Boolean padrao);

  boolean existsByUsuarioCriacaoAndPadrao(Usuario usuario, boolean padrao);
}
