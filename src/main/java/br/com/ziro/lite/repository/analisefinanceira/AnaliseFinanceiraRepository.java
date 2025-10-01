package br.com.ziro.lite.repository.analisefinanceira;

import br.com.ziro.lite.entity.analisefinanceira.AnaliseFinanceira;
import br.com.ziro.lite.entity.usuario.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnaliseFinanceiraRepository extends JpaRepository<AnaliseFinanceira, Long> {
  List<AnaliseFinanceira> findAllByUsuarioCriacaoOrderByDataCriacaoAsc(Usuario usuarioCriacao);
}
