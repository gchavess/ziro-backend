package br.com.ziro.lite.controller.conta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.conta.AssociarNaturezaContaDTO;
import br.com.ziro.lite.dto.conta.ContaDTO;
import br.com.ziro.lite.dto.conta.ContaOptionDTO;
import br.com.ziro.lite.dto.conta.ContaTreeNodeDTO;
import br.com.ziro.lite.exception.conta.ContaNaoEncontradoException;
import br.com.ziro.lite.exception.conta.ContaPaiNaoEncontradoException;
import br.com.ziro.lite.exception.naturezaconta.NaturezaContaNaoEncontradoException;
import br.com.ziro.lite.exception.usuario.UsuarioNaoEncontradoException;
import br.com.ziro.lite.service.conta.ContaService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ContaControllerTest {

  @Mock private ContaService service;

  @InjectMocks private ContaController controller;

  private ContaDTO conta1;
  private ContaDTO conta2;
  private ContaTreeNodeDTO treeNode;
  private ContaOptionDTO optionDTO;
  private AssociarNaturezaContaDTO associarDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    conta1 = new ContaDTO();
    conta1.setId(1L);
    conta1.setDescricao("Conta 1");
    conta2 = new ContaDTO();
    conta2.setId(2L);
    conta2.setDescricao("Conta 2");

    treeNode = new ContaTreeNodeDTO();
    treeNode.setId(1L);
    treeNode.setLabel("Conta 1");
    treeNode.setChildren(Arrays.asList());

    optionDTO = new ContaOptionDTO();
    optionDTO.setLabel("Conta 1");
    optionDTO.setValue("100");
    optionDTO.setChildren(Arrays.asList());

    associarDTO = new AssociarNaturezaContaDTO();
    associarDTO.setNaturezaId(1L);
    associarDTO.setContas(Arrays.asList(1L, 2L));
  }

  @Test
  void listarTodos_deveRetornarLista() {
    when(service.listarTodos()).thenReturn(Arrays.asList(conta1, conta2));

    List<ContaDTO> result = controller.listarTodos();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(service, times(1)).listarTodos();
  }

  @Test
  void buscarPorId_deveRetornarConta() throws ContaNaoEncontradoException {
    when(service.buscarPorId(1L)).thenReturn(conta1);

    ContaDTO result = controller.buscarPorId(1L);

    assertNotNull(result);
    assertEquals(conta1, result);
    verify(service, times(1)).buscarPorId(1L);
  }

  @Test
  void criar_deveChamarServicoESalvar()
      throws UsuarioNaoEncontradoException,
          NaturezaContaNaoEncontradoException,
          ContaPaiNaoEncontradoException {
    when(service.salvar(conta1)).thenReturn(conta1);

    ContaDTO result = controller.criar(conta1);

    assertNotNull(result);
    assertEquals(conta1, result);
    verify(service, times(1)).salvar(conta1);
  }

  @Test
  void atualizar_deveChamarServicoEAtualizar()
      throws NaturezaContaNaoEncontradoException,
          ContaNaoEncontradoException,
          ContaPaiNaoEncontradoException {
    when(service.atualizar(1L, conta1)).thenReturn(conta1);

    ContaDTO result = controller.atualizar(1L, conta1);

    assertNotNull(result);
    assertEquals(conta1, result);
    verify(service, times(1)).atualizar(1L, conta1);
  }

  @Test
  void deletar_deveChamarServico() {
    doNothing().when(service).deletar(1L);

    controller.deletar(1L);

    verify(service, times(1)).deletar(1L);
  }

  @Test
  void getTree_deveRetornarTree() {
    when(service.getTree()).thenReturn(Arrays.asList(treeNode));

    List<ContaTreeNodeDTO> result = controller.getTree();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(treeNode, result.get(0));
    verify(service, times(1)).getTree();
  }

  @Test
  void associarNatureza_deveChamarServico()
      throws NaturezaContaNaoEncontradoException, ContaNaoEncontradoException {
    when(service.associarNatureza(associarDTO)).thenReturn(Arrays.asList(conta1, conta2));

    List<ContaDTO> result = controller.associarNatureza(associarDTO);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(service, times(1)).associarNatureza(associarDTO);
  }

  @Test
  void listarDropdown_deveRetornarOpcoes() {
    when(service.listarDropdown()).thenReturn(Arrays.asList(optionDTO));

    List<ContaOptionDTO> result = controller.listarDropdown();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(optionDTO, result.get(0));
    verify(service, times(1)).listarDropdown();
  }
}
