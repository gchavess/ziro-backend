package br.com.ziro.lite.service.analisefinanceira;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.analisefinanceira.usuario.AnaliseFinanceiraDTO;
import br.com.ziro.lite.entity.analisefinanceira.AnaliseFinanceira;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.exception.analisefinanceira.AnaliseFinanceiraNaoEncontradaException;
import br.com.ziro.lite.repository.analisefinanceira.AnaliseFinanceiraRepository;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnaliseFinanceiraServiceTest {

  @Mock private AnaliseFinanceiraRepository repository;

  @Mock private UsuarioRepository usuarioRepository;

  @Mock private UsuarioLogado usuarioLogado;

  @InjectMocks private AnaliseFinanceiraService service;

  private Usuario usuario;

  @BeforeEach
  void setUp() {
    usuario = new Usuario();
    usuario.setId(1L);

    lenient().when(usuarioLogado.getCurrent()).thenReturn(usuario);
    lenient()
        .when(usuarioLogado.getCurrentDTO())
        .thenReturn(new br.com.ziro.lite.dto.usuario.UsuarioDTO(usuario.getId(), null, null));
  }

  @Test
  void listarTodos_deveRetornarListaDTO() {
    AnaliseFinanceira analise = new AnaliseFinanceira();
    analise.setId(1L);
    analise.setDescricao("Analise A");
    analise.setDataCriacao(new Date());
    analise.setUsuarioCriacao(usuario);

    when(repository.findAllByUsuarioCriacaoOrderByDataCriacaoAsc(usuario))
        .thenReturn(List.of(analise));

    List<AnaliseFinanceiraDTO> result = service.listarTodos();

    assertEquals(1, result.size());
    assertEquals("Analise A", result.get(0).getDescricao());
  }

  @Test
  void buscarPorId_deveRetornarDTO_quandoEncontrar()
      throws AnaliseFinanceiraNaoEncontradaException {
    AnaliseFinanceira analise = new AnaliseFinanceira();
    analise.setId(1L);
    analise.setDescricao("Analise B");

    when(repository.findById(1L)).thenReturn(Optional.of(analise));

    AnaliseFinanceiraDTO result = service.buscarPorId(1L);

    assertEquals(1L, result.getId());
    assertEquals("Analise B", result.getDescricao());
  }

  @Test
  void buscarPorId_deveLancarExcecao_quandoNaoEncontrar() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(AnaliseFinanceiraNaoEncontradaException.class, () -> service.buscarPorId(1L));
  }

  @Test
  void criar_deveSalvarAnalise() {
    AnaliseFinanceiraDTO request = new AnaliseFinanceiraDTO();
    request.setDescricao("Nova Analise");
    request.setObservacao("Observacao");
    request.setCodigo("COD123");
    request.setFato("Fato");
    request.setCausa("Causa");
    request.setAcao("Acao");

    AnaliseFinanceira saved = new AnaliseFinanceira();
    saved.setId(1L);
    saved.setDescricao("Nova Analise");

    when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
    when(repository.save(any())).thenReturn(saved);

    AnaliseFinanceiraDTO result = service.criar(request);

    assertEquals(1L, result.getId());
    assertEquals("Nova Analise", result.getDescricao());
  }

  @Test
  void atualizar_deveAtualizarCampos() throws AnaliseFinanceiraNaoEncontradaException {
    AnaliseFinanceira entity = new AnaliseFinanceira();
    entity.setId(1L);
    entity.setDescricao("Antiga");

    when(repository.findById(1L)).thenReturn(Optional.of(entity));
    when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    AnaliseFinanceiraDTO request = new AnaliseFinanceiraDTO();
    request.setDescricao("Atualizada");

    AnaliseFinanceiraDTO result = service.atualizar(1L, request);

    assertEquals("Atualizada", result.getDescricao());
  }

  @Test
  void atualizar_deveLancarExcecao_quandoNaoEncontrar() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(
        AnaliseFinanceiraNaoEncontradaException.class,
        () -> service.atualizar(1L, new AnaliseFinanceiraDTO()));
  }

  @Test
  void deletar_deveChamarRepositoryDelete() {
    service.deletar(1L);
    verify(repository).deleteById(1L);
  }
}
