import model.Circulo;
import model.Contato;
import exceptions.CirculoNotFoundException;
import exceptions.ContatoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Relacionamento entre contatos e círculos")
public class ContactsCircleRelationsTest {

	private static final String AMIGOS = "amigos";
	private static final String TRABALHO = "trabalho";
	private static final String FAMILIA = "familia";
	private static final String JOAQUIM_EMAIL = "joaquim@ufc.br";
	private static final String JOAQUIM = "joaquim";
	private static final String ANA_EMAIL = "ana@ufc.br";
	private static final String ANA = "ana";
	private static final String MARIO_EMAIL = "mario@ufc.br";
	private static final String MARIO = "mario";
	private static final String JOSE_EMAIL = "jose@ufc.br";
	private static final String JOSE = "jose";
	private static final String JAMES_EMAIL = "james@ufc.com";
	private static final String JAMES = "james";
	private GContatos gcont;
	private Circulo familia, trabalho, amigos;
	private Contato james, jose, mario, ana, joaquim;

	@BeforeEach
	public void setUp() {
		familia = new Circulo(FAMILIA, 3);
		trabalho = new Circulo(TRABALHO, 3);
		amigos = new Circulo(AMIGOS, 2);

		james = new Contato(JAMES, JAMES_EMAIL);
		jose = new Contato(JOSE, JOSE_EMAIL);
		mario = new Contato(MARIO, MARIO_EMAIL);
		ana = new Contato(ANA, ANA_EMAIL);
		joaquim = new Contato(JOAQUIM, JOAQUIM_EMAIL);

		gcont = new GContatos();
	}


	@Test
	@DisplayName("Deve adicionar um contato existente a um círculo existente")
	public void adicionarContatoCirculoExistente() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarContato(JAMES, JAMES_EMAIL);
		assertTrue(gcont.adicionarContatoAoCirculo(JAMES, FAMILIA), "Contato deve ser adicionado ao circulo");
		assertEquals(1, gcont.getCirculo(FAMILIA).getNumeroDeContatos(), "Numero de contatos no circulo errado");
		assertEquals(List.of(familia), gcont.recuperarCirculosDoContato(JAMES), "Lista de circulos do contato esta errada");
		assertEquals(List.of(james), gcont.recuperarContatosDoCirculo(FAMILIA), "Lista de contatos de um circulo esta errada");
	}

	@Test
	@DisplayName("Deve lançar ContatoNotFoundException ao adicionar contato inexistente a um círculo")
	public void adicionarContatoInexistenteCirculoExistente() {
		gcont.criarCirculo(FAMILIA, 3);

		ContatoNotFoundException e = assertThrows(ContatoNotFoundException.class, () -> gcont.adicionarContatoAoCirculo(JAMES, FAMILIA));
		assertEquals(JAMES, e.getContatoNaoEncontrado(), "A excecao nao retornou o id do contato que nao existe");
		assertEquals(0, gcont.getCirculo(FAMILIA).getNumeroDeContatos(), "Numero de contatos no circulo errado");
	}

	@Test
	@DisplayName("Deve lançar CirculoNotFoundException ao adicionar contato a um círculo inexistente")
	public void adicionarContatoCirculoInexistente() throws ContatoNotFoundException {
		gcont.criarContato(JAMES, JAMES_EMAIL);

		CirculoNotFoundException e = assertThrows(CirculoNotFoundException.class, () -> gcont.adicionarContatoAoCirculo(JAMES, FAMILIA));
		assertEquals(FAMILIA, e.getCirculoNaoEncontrado(), "A excecao nao retornou o id do circulo que nao existe");

		assertNull(gcont.getCirculo(FAMILIA), "Circulo nao existente");
		assertEquals(List.of(), gcont.recuperarCirculosDoContato(JAMES), "Contato nao esta em nenhum circulo");
	}

	@Test
	@DisplayName("Deve lançar CirculoNotFoundException quando nem o contato nem o círculo existem")
	public void adicionarContatoECirculoInexistentes() {
		CirculoNotFoundException e = assertThrows(CirculoNotFoundException.class, () -> gcont.adicionarContatoAoCirculo(JAMES, FAMILIA));
		assertEquals(FAMILIA, e.getCirculoNaoEncontrado(), "A excecao nao retornou o id do circulo que nao existe");
	}

	@Test
	@DisplayName("Não deve adicionar o mesmo contato duas vezes ao mesmo círculo")
	public void adicionarContatoDuplicadoCirculoExistente() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarContato(JAMES, JAMES_EMAIL);
		gcont.adicionarContatoAoCirculo(JAMES, FAMILIA);
		assertFalse(gcont.adicionarContatoAoCirculo(JAMES, FAMILIA), "Contato ja esta no circulo");
		assertEquals(1, gcont.getCirculo(FAMILIA).getNumeroDeContatos(), "Numero de contatos no circulo errado");
		assertEquals(List.of(familia), gcont.recuperarCirculosDoContato(JAMES), "Lista de circulos do contato esta errada");
		assertEquals(List.of(james), gcont.recuperarContatosDoCirculo(FAMILIA), "Lista de contatos de um circulo esta errada");
	}

	@Test
	@DisplayName("Não deve adicionar contato a um círculo que atingiu o limite")
	public void adicionarAlemDoLimite() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);

		gcont.criarContato(JAMES, JAMES_EMAIL);
		gcont.criarContato(JOSE, JOSE_EMAIL);
		gcont.criarContato(ANA, ANA_EMAIL);
		gcont.criarContato(JOAQUIM, JOAQUIM_EMAIL);

		assertTrue(gcont.adicionarContatoAoCirculo(JAMES, FAMILIA), "Contato deve ser adicionado ao circulo");
		assertTrue(gcont.adicionarContatoAoCirculo(JOSE, FAMILIA), "Contato deve ser adicionado ao circulo");
		assertTrue(gcont.adicionarContatoAoCirculo(ANA, FAMILIA), "Contato deve ser adicionado ao circulo");
		assertFalse(gcont.adicionarContatoAoCirculo(JOAQUIM, FAMILIA), "Limite do circulo atingido");

		assertEquals(3, gcont.getCirculo(FAMILIA).getNumeroDeContatos(), "Numero de contatos no circulo errado");
		assertEquals(List.of(), gcont.recuperarCirculosDoContato(JOAQUIM), "Contato recusado nao deve estar no circulo");
	}

	@Test
	@DisplayName("Deve permitir que um contato participe de vários círculos")
	public void adicionarContatoVariosCirculos() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarCirculo(AMIGOS, 2);

		gcont.criarContato(JAMES, JAMES_EMAIL);
		gcont.criarContato(MARIO, MARIO_EMAIL);
		gcont.criarContato(JOSE, JOSE_EMAIL);

		gcont.adicionarContatoAoCirculo(JAMES, FAMILIA);
		gcont.adicionarContatoAoCirculo(MARIO, FAMILIA);
		gcont.adicionarContatoAoCirculo(JOSE, FAMILIA);

		assertTrue(gcont.adicionarContatoAoCirculo(JAMES, AMIGOS), "Contato deve ser adicionado ao circulo");

		assertEquals(List.of(amigos, familia), gcont.recuperarCirculosDoContato(JAMES), "Lista de circulos do contato esta errada");
		assertEquals(List.of(james, jose, mario), gcont.recuperarContatosDoCirculo(FAMILIA), "Lista de contatos de um circulo esta errada");
	}

	@Test
	@DisplayName("Deve remover um contato de um círculo")
	public void removendoContatoDoCirculo() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarCirculo(AMIGOS, 2);

		gcont.criarContato(JAMES, JAMES_EMAIL);

		gcont.adicionarContatoAoCirculo(JAMES, FAMILIA);
		gcont.adicionarContatoAoCirculo(JAMES, AMIGOS);

		assertTrue(gcont.removerContatoDoCirculo(JAMES, AMIGOS), "Contato deve ser removido do circulo");

		assertEquals(List.of(familia), gcont.recuperarCirculosDoContato(JAMES), "Lista de circulos do contato esta errada");
		assertEquals(List.of(), gcont.recuperarContatosDoCirculo(AMIGOS), "Lista de contatos do circulo esta errada");
	}

	@Test
	@DisplayName("Não deve remover de um círculo um contato que não faz parte dele")
	public void removendoContatoQueNaoEstaNoCirculo() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarContato(JAMES, JAMES_EMAIL);
		gcont.criarContato(JOSE, JOSE_EMAIL);
		gcont.adicionarContatoAoCirculo(JAMES, FAMILIA);

		assertFalse(gcont.removerContatoDoCirculo(JOSE, FAMILIA), "Contato nao esta no circulo");
		assertEquals(List.of(james), gcont.recuperarContatosDoCirculo(FAMILIA), "Lista de contatos do circulo esta errada");
	}

	@Test
	@DisplayName("Remover um contato de um círculo cheio deve liberar espaço para outro")
	public void removendoContatoLiberaEspacoNoCirculo() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(AMIGOS, 2);
		gcont.criarContato(JAMES, JAMES_EMAIL);
		gcont.criarContato(MARIO, MARIO_EMAIL);
		gcont.criarContato(ANA, ANA_EMAIL);

		gcont.adicionarContatoAoCirculo(JAMES, AMIGOS);
		gcont.adicionarContatoAoCirculo(MARIO, AMIGOS);
		assertFalse(gcont.adicionarContatoAoCirculo(ANA, AMIGOS), "Limite do circulo atingido");

		gcont.removerContatoDoCirculo(JAMES, AMIGOS);
		assertTrue(gcont.adicionarContatoAoCirculo(ANA, AMIGOS), "A remocao deveria ter liberado espaco no circulo");
		assertEquals(List.of(ana, mario), gcont.recuperarContatosDoCirculo(AMIGOS), "Lista de contatos do circulo esta errada");
	}

	@Test
	@DisplayName("Deve lançar ContatoNotFoundException ao remover contato inexistente de um círculo")
	public void removendoContatoInexistenteDoCirculo() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarContato(JAMES, JAMES_EMAIL);
		gcont.adicionarContatoAoCirculo(JAMES, FAMILIA);

		ContatoNotFoundException e = assertThrows(ContatoNotFoundException.class, () -> gcont.removerContatoDoCirculo("margarida", FAMILIA));
		assertEquals("margarida", e.getContatoNaoEncontrado(), "A excecao nao retornou o id do contato que nao existe");
		assertEquals(1, gcont.getCirculo(FAMILIA).getNumeroDeContatos(), "Numero de contatos no circulo errado");
	}

	@Test
	@DisplayName("Deve lançar CirculoNotFoundException ao remover contato de um círculo inexistente")
	public void removendoContatoDoCirculoInexistente() {
		CirculoNotFoundException e = assertThrows(CirculoNotFoundException.class, () -> gcont.removerContatoDoCirculo(JAMES, FAMILIA));
		assertEquals(FAMILIA, e.getCirculoNaoEncontrado(), "A excecao nao retornou o id do circulo que nao existe");
	}

	@Test
	@DisplayName("Deve lançar CirculoNotFoundException ao listar os contatos de um círculo inexistente")
	public void recuperandoContatosDeCirculoInexistente() {
		CirculoNotFoundException e = assertThrows(CirculoNotFoundException.class, () -> gcont.recuperarContatosDoCirculo(FAMILIA));
		assertEquals(FAMILIA, e.getCirculoNaoEncontrado(), "A excecao nao retornou o id do circulo que nao existe");
	}

	@Test
	@DisplayName("Remover um círculo deve retirá-lo da lista de círculos dos seus contatos")
	public void removendoCirculoQuePossuiContatos() throws CirculoNotFoundException, ContatoNotFoundException {

		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarCirculo(AMIGOS, 2);
		gcont.criarCirculo(TRABALHO, 3);

		gcont.criarContato(JAMES, JAMES_EMAIL);
		gcont.criarContato(MARIO, MARIO_EMAIL);
		gcont.criarContato(JOSE, JOSE_EMAIL);
		gcont.criarContato(ANA, ANA_EMAIL);
		gcont.criarContato(JOAQUIM, JOAQUIM_EMAIL);

		gcont.adicionarContatoAoCirculo(JAMES, FAMILIA);
		gcont.adicionarContatoAoCirculo(MARIO, FAMILIA);
		gcont.adicionarContatoAoCirculo(JOSE, FAMILIA);

		gcont.adicionarContatoAoCirculo(JAMES, TRABALHO);
		gcont.adicionarContatoAoCirculo(JOAQUIM, TRABALHO);
		gcont.adicionarContatoAoCirculo(ANA, TRABALHO);

		gcont.adicionarContatoAoCirculo(JAMES, AMIGOS);

		assertTrue(gcont.removerCirculo(FAMILIA), "O circulo deve ser removido");

		assertEquals(List.of(amigos, trabalho), gcont.recuperarCirculosDoContato(JAMES), "Lista de circulos do contato esta errada");
		assertEquals(List.of(), gcont.recuperarCirculosDoContato(JOSE), "Lista de circulos do contato esta errada");
		assertNull(gcont.getCirculo(FAMILIA), "Circulo nao existe mais");
	}

	@Test
	@DisplayName("Remover um contato deve retirá-lo de todos os círculos")
	public void removendoContatosQueEstaEmCirculos() throws CirculoNotFoundException, ContatoNotFoundException {

		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarCirculo(AMIGOS, 2);
		gcont.criarCirculo(TRABALHO, 3);

		gcont.criarContato(JAMES, JAMES_EMAIL);
		gcont.criarContato(JOSE, JOSE_EMAIL);
		gcont.criarContato(ANA, ANA_EMAIL);
		gcont.criarContato(MARIO, MARIO_EMAIL);
		gcont.criarContato(JOAQUIM, JOAQUIM_EMAIL);

		gcont.adicionarContatoAoCirculo(JAMES, FAMILIA);
		gcont.adicionarContatoAoCirculo(MARIO, FAMILIA);
		gcont.adicionarContatoAoCirculo(JOSE, FAMILIA);

		gcont.adicionarContatoAoCirculo(JAMES, TRABALHO);
		gcont.adicionarContatoAoCirculo(JOAQUIM, TRABALHO);
		gcont.adicionarContatoAoCirculo(ANA, TRABALHO);

		gcont.adicionarContatoAoCirculo(JAMES, AMIGOS);

		assertTrue(gcont.removerContato(JAMES), "O contato deve ser removido");

		assertEquals(List.of(jose, mario), gcont.recuperarContatosDoCirculo(FAMILIA), "A lista de contatos do circulo esta errada");
		assertEquals(List.of(ana, joaquim), gcont.recuperarContatosDoCirculo(TRABALHO), "A lista de contatos do circulo esta errada");
		assertEquals(List.of(), gcont.recuperarContatosDoCirculo(AMIGOS), "A lista de contatos do circulo esta errada");

		ContatoNotFoundException e = assertThrows(ContatoNotFoundException.class, () -> gcont.recuperarCirculosDoContato(JAMES));
		assertEquals(JAMES, e.getContatoNaoEncontrado(), "A excecao nao retornou o id do contato que nao existe");

		assertNull(gcont.getContato(JAMES), "Contato nao existe mais");
	}

	@Test
	@DisplayName("Deve listar em ordem alfabética os círculos em comum entre dois contatos")
	public void circulosEmComum() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarCirculo(AMIGOS, 2);
		gcont.criarCirculo(TRABALHO, 3);

		gcont.criarContato(JAMES, JAMES_EMAIL);
		gcont.criarContato(MARIO, MARIO_EMAIL);
		gcont.criarContato(JOSE, JOSE_EMAIL);
		gcont.criarContato(ANA, ANA_EMAIL);
		gcont.criarContato(JOAQUIM, JOAQUIM_EMAIL);

		gcont.adicionarContatoAoCirculo(JAMES, FAMILIA);
		gcont.adicionarContatoAoCirculo(MARIO, FAMILIA);

		gcont.adicionarContatoAoCirculo(JAMES, TRABALHO);
		gcont.adicionarContatoAoCirculo(JOAQUIM, TRABALHO);
		gcont.adicionarContatoAoCirculo(ANA, TRABALHO);

		gcont.adicionarContatoAoCirculo(JAMES, AMIGOS);
		gcont.adicionarContatoAoCirculo(MARIO, AMIGOS);

		assertEquals(List.of(trabalho), gcont.getCirculosEmComum(JAMES, ANA));
		assertEquals(List.of(), gcont.getCirculosEmComum(JAMES, JOSE));
		assertEquals(List.of(amigos, familia), gcont.getCirculosEmComum(JAMES, MARIO));
	}

	@Test
	@DisplayName("Deve lançar ContatoNotFoundException ao buscar círculos em comum com um contato inexistente")
	public void circulosEmComumContatoInexistente() {
		gcont.criarContato(JAMES, JAMES_EMAIL);

		ContatoNotFoundException e1 = assertThrows(ContatoNotFoundException.class, () -> gcont.getCirculosEmComum(JAMES, "margarida"));
		assertEquals("margarida", e1.getContatoNaoEncontrado(), "A excecao nao retornou o id do contato que nao existe");

		ContatoNotFoundException e2 = assertThrows(ContatoNotFoundException.class, () -> gcont.getCirculosEmComum("margarida", JAMES));
		assertEquals("margarida", e2.getContatoNaoEncontrado(), "A excecao nao retornou o id do contato que nao existe");
	}

}
