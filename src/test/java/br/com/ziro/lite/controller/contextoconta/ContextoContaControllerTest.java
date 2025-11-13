package br.com.ziro.lite.controller.contextoconta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import br.com.ziro.lite.dto.contextoconta.ContextoContaDTO;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.exception.contextoconta.ContextoContaNaoEncontradoException;
import br.com.ziro.lite.service.contextoconta.ContextoContaService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class ContextoContaControllerTest {

  @Mock private ContextoContaService service;

  @InjectMocks private ContextoContaController controller;

  private ContextoContaDTO dto1;
  private ContextoContaDTO dto2;
  private ContextoConta entity1;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    dto1 = new ContextoContaDTO();
    dto1.setId(1L);
    dto1.setDescricao("Contexto 1");

    dto2 = new ContextoContaDTO();
    dto2.setId(2L);
    dto2.setDescricao("Contexto 2");

    entity1 = new ContextoConta();
    entity1.setId(1L);
    entity1.setDescricao("Contexto 1");
  }

  @Test
  void listarTodos_deveRetornarLista() {
    when(service.listarTodos()).thenReturn(Arrays.asList(dto1, dto2));

    List<ContextoContaDTO> result = controller.listarTodos();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(service, times(1)).listarTodos();
  }

  @Test
  void buscarPorId_quandoExiste_deveRetornarOk() {
    when(service.buscarPorId(1L)).thenReturn(Optional.of(entity1));

    ResponseEntity<ContextoConta> response = controller.buscarPorId(1L);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(entity1, response.getBody());
    verify(service, times(1)).buscarPorId(1L);
  }

  @Test
  void buscarPorId_quandoNaoExiste_deveRetornarNotFound() {
    when(service.buscarPorId(99L)).thenReturn(Optional.empty());

    ResponseEntity<ContextoConta> response = controller.buscarPorId(99L);

    assertEquals(404, response.getStatusCodeValue());
    verify(service, times(1)).buscarPorId(99L);
  }

  @Test
  void criar_deveChamarServicoESalvar() {
    when(service.salvar(dto1)).thenReturn(dto1);

    ContextoContaDTO result = controller.criar(dto1);

    assertNotNull(result);
    assertEquals(dto1, result);
    verify(service, times(1)).salvar(dto1);
  }

  @Test
  void atualizar_quandoExiste_deveRetornarOk() throws ContextoContaNaoEncontradoException {
    when(service.atualizar(1L, entity1)).thenReturn(dto1);

    ResponseEntity<ContextoContaDTO> response = controller.atualizar(1L, entity1);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(dto1, response.getBody());
    verify(service, times(1)).atualizar(1L, entity1);
  }

  @Test
  void atualizar_quandoNaoExiste_deveRetornarNotFound() throws ContextoContaNaoEncontradoException {
    when(service.atualizar(99L, entity1)).thenThrow(new RuntimeException());

    ResponseEntity<ContextoContaDTO> response = controller.atualizar(99L, entity1);

    assertEquals(404, response.getStatusCodeValue());
    verify(service, times(1)).atualizar(99L, entity1);
  }

  @Test
  void deletar_deveChamarServico() {
    doNothing().when(service).deletar(1L);

    ResponseEntity<Void> response = controller.deletar(1L);

    assertEquals(204, response.getStatusCodeValue());
    verify(service, times(1)).deletar(1L);
  }
}
