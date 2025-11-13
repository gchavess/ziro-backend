package br.com.ziro.lite.controller.naturezaconta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import br.com.ziro.lite.dto.naturezaconta.NaturezaContaAgrupadaDTO;
import br.com.ziro.lite.dto.naturezaconta.NaturezaContaDTO;
import br.com.ziro.lite.exception.contextoconta.ContextoContaNaoEncontradoException;
import br.com.ziro.lite.exception.naturezaconta.NaturezaContaNaoEncontradoException;
import br.com.ziro.lite.exception.usuario.UsuarioNaoEncontradoException;
import br.com.ziro.lite.service.naturezaconta.NaturezaContaService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class NaturezaContaControllerTest {

  @Mock private NaturezaContaService service;

  @InjectMocks private NaturezaContaController controller;

  private NaturezaContaDTO dto1;
  private NaturezaContaDTO dto2;
  private NaturezaContaAgrupadaDTO agrupadaDTO;
  private ContextoContaDTO contexto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    contexto = new ContextoContaDTO();
    contexto.setId(1L);
    contexto.setDescricao("Contexto 1");

    dto1 = new NaturezaContaDTO();
    dto1.setId(1L);
    dto1.setDescricao("Natureza 1");
    dto1.setCodigo("NAT1");
    dto1.setContextoConta(contexto);

    dto2 = new NaturezaContaDTO();
    dto2.setId(2L);
    dto2.setDescricao("Natureza 2");
    dto2.setCodigo("NAT2");
    dto2.setContextoConta(contexto);

    agrupadaDTO = new NaturezaContaAgrupadaDTO();
    agrupadaDTO.setId(1L);
    agrupadaDTO.setLabel("Agrupada 1");
    agrupadaDTO.setChildren(Arrays.asList());
    agrupadaDTO.setCodigo("AGR1");
    agrupadaDTO.setDescricao("Descrição Agrupada");
    agrupadaDTO.setObservacao("Observação Agrupada");
    agrupadaDTO.setContextoConta(contexto);
  }

  @Test
  void listarTodos_deveRetornarListaDeNaturezas() {
    when(service.listarTodos()).thenReturn(Arrays.asList(dto1, dto2));

    List<NaturezaContaDTO> result = controller.listarTodos();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(service, times(1)).listarTodos();
  }

  @Test
  void buscarPorId_quandoExiste_deveRetornarNatureza() throws NaturezaContaNaoEncontradoException {
    when(service.buscarPorId(1L)).thenReturn(dto1);

    NaturezaContaDTO result = controller.buscarPorId(1L);

    assertNotNull(result);
    assertEquals(dto1, result);
    verify(service, times(1)).buscarPorId(1L);
  }

  @Test
  void criar_deveChamarServicoESalvar()
      throws UsuarioNaoEncontradoException, ContextoContaNaoEncontradoException {
    when(service.salvar(dto1)).thenReturn(dto1);

    NaturezaContaDTO result = controller.criar(dto1);

    assertNotNull(result);
    assertEquals(dto1, result);
    verify(service, times(1)).salvar(dto1);
  }

  @Test
  void atualizar_deveChamarServicoEAtualizar()
      throws ContextoContaNaoEncontradoException, NaturezaContaNaoEncontradoException {
    when(service.atualizar(1L, dto1)).thenReturn(dto1);

    NaturezaContaDTO result = controller.atualizar(1L, dto1);

    assertNotNull(result);
    assertEquals(dto1, result);
    verify(service, times(1)).atualizar(1L, dto1);
  }

  @Test
  void deletar_deveChamarServico() {
    doNothing().when(service).deletar(1L);

    controller.deletar(1L);

    verify(service, times(1)).deletar(1L);
  }

  @Test
  void listarAgrupadas_deveRetornarListaAgrupada() {
    when(service.listarAgrupadasPorContexto()).thenReturn(Arrays.asList(agrupadaDTO));

    ResponseEntity<List<NaturezaContaAgrupadaDTO>> response = controller.listarAgrupadas();

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(1, response.getBody().size());
    assertEquals(agrupadaDTO, response.getBody().get(0));
    verify(service, times(1)).listarAgrupadasPorContexto();
  }
}
