package br.com.ziro.lite.service.conta;

import br.com.ziro.lite.dto.conta.AssociarNaturezaContaDTO;
import br.com.ziro.lite.dto.conta.ContaDTO;
import br.com.ziro.lite.dto.conta.ContaOptionDTO;
import br.com.ziro.lite.dto.conta.ContaTreeNodeDTO;
import br.com.ziro.lite.dto.usuario.UsuarioDTO;
import br.com.ziro.lite.entity.conta.Conta;
import br.com.ziro.lite.entity.contextoconta.ContextoConta;
import br.com.ziro.lite.entity.naturezaconta.NaturezaConta;
import br.com.ziro.lite.entity.usuario.Usuario;
import br.com.ziro.lite.exception.conta.ContaNaoEncontradoException;
import br.com.ziro.lite.repository.conta.ContaRepository;
import br.com.ziro.lite.repository.naturezaconta.NaturezaContaRepository;
import br.com.ziro.lite.repository.usuario.UsuarioRepository;
import br.com.ziro.lite.security.UsuarioLogado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private NaturezaContaRepository naturezaContaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioLogado usuarioLogado;

    @InjectMocks
    private ContaService contaService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        lenient().when(usuarioLogado.getCurrentDTO()).thenReturn(UsuarioDTO.fromEntity(usuario));
        lenient().when(usuarioLogado.getCurrent()).thenReturn(usuario);
        lenient().when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

    }

    @Test
    void listarTodos_deveRetornarContas() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setDescricao("Conta A");
        conta.setUsuarioCriacao(usuario);
        when(contaRepository.findAllByUsuarioCriacao(usuario)).thenReturn(List.of(conta));

        List<ContaDTO> result = contaService.listarTodos();

        assertEquals(1, result.size());
        assertEquals("Conta A", result.get(0).getDescricao());
    }

    @Test
    void buscarPorId_deveRetornarConta() throws ContaNaoEncontradoException {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setDescricao("Conta A");
        conta.setUsuarioCriacao(usuario);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        ContaDTO result = contaService.buscarPorId(1L);

        assertEquals("Conta A", result.getDescricao());
    }

    @Test
    void buscarPorId_deveLancarExcecaoQuandoNaoEncontrar() {
        when(contaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradoException.class, () -> contaService.buscarPorId(1L));
    }

    @Test
    void salvar_deveSalvarConta() throws Exception {
        ContaDTO dto = new ContaDTO();
        dto.setDescricao("Conta Nova");

        Conta savedConta = new Conta();
        savedConta.setId(1L);
        savedConta.setDescricao("Conta Nova");
        savedConta.setUsuarioCriacao(usuario);

        when(contaRepository.save(any(Conta.class))).thenReturn(savedConta);

        ContaDTO result = contaService.salvar(dto);

        assertEquals("Conta Nova", result.getDescricao());
        verify(contaRepository).save(any(Conta.class));
    }


    @Test
    void atualizar_deveAtualizarConta() throws Exception {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setDescricao("Conta A");
        conta.setUsuarioCriacao(usuario);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ContaDTO dto = new ContaDTO();
        dto.setDescricao("Conta Atualizada");

        ContaDTO result = contaService.atualizar(1L, dto);

        assertEquals("Conta Atualizada", result.getDescricao());
    }

    @Test
    void associarNatureza_deveAssociar() throws Exception {
        NaturezaConta natureza = new NaturezaConta();
        natureza.setId(1L);

        ContextoConta contexto = new ContextoConta();
        contexto.setId(1L);

        natureza.setContextoConta(contexto);
        when(naturezaContaRepository.findById(1L)).thenReturn(Optional.of(natureza));

        Conta conta = new Conta();
        conta.setId(1L);
        conta.setUsuarioCriacao(usuario);

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        AssociarNaturezaContaDTO request = new AssociarNaturezaContaDTO();
        request.setNaturezaId(1L);
        request.setContas(List.of(1L));


        List<ContaDTO> result = contaService.associarNatureza(request);

        assertEquals(1, result.size());
        assertNotNull(result.get(0).getNaturezaConta());
        assertEquals(natureza.getId(), result.get(0).getNaturezaConta().getId());
    }

    @Test
    void deletar_deveChamarRepositoryDelete() {
        contaService.deletar(1L);
        verify(contaRepository).deleteById(1L);
    }

    @Test
    void getTree_deveMontarArvore() {
        Conta pai = new Conta();
        pai.setId(1L);
        pai.setDescricao("Pai");
        pai.setUsuarioCriacao(usuario);


        Conta filho = new Conta();
        filho.setId(2L);
        filho.setDescricao("Filho");
        filho.setPai(pai);
        filho.setUsuarioCriacao(usuario);


        when(contaRepository.findAllByUsuarioCriacao(usuario)).thenReturn(List.of(pai, filho));

        List<ContaTreeNodeDTO> tree = contaService.getTree();

        assertEquals(1, tree.size());
        assertEquals("Pai", tree.get(0).getLabel());
        assertEquals(1, tree.get(0).getChildren().size());
        assertEquals("Filho", tree.get(0).getChildren().get(0).getLabel());
    }

    @Test
    void listarDropdown_deveMontarDropdown() {
        Conta pai = new Conta();
        pai.setId(1L);
        pai.setDescricao("Pai");
        pai.setUsuarioCriacao(usuario);

        Conta filho = new Conta();
        filho.setId(2L);
        filho.setDescricao("Filho");
        filho.setPai(pai);
        filho.setUsuarioCriacao(usuario);

        when(contaRepository.findAllByUsuarioCriacao(usuario)).thenReturn(List.of(pai, filho));

        List<ContaOptionDTO> options = contaService.listarDropdown();

        assertEquals(1, options.size());
        assertEquals("Pai", options.get(0).getLabel());
        assertEquals(1, options.get(0).getChildren().size());
        assertEquals("Filho", options.get(0).getChildren().get(0).getLabel());
    }
}
