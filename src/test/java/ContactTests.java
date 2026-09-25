import model.Contato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Gerenciamento de contatos")
public class ContactTests {

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
	private Contato james, jose, mario, ana, joaquim;

	@BeforeEach
	public void setUp() {
		james = new Contato(JAMES, JAMES_EMAIL);
		jose = new Contato(JOSE, JOSE_EMAIL);
		mario = new Contato(MARIO, MARIO_EMAIL);
		ana = new Contato(ANA, ANA_EMAIL);
		joaquim = new Contato(JOAQUIM, JOAQUIM_EMAIL);

		gcont = new GContatos();
	}

	@Test
	@DisplayName("Deve adicionar um contato")
	public void adicionarContato() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");
		assertEquals(1, gcont.getNumeroDeContatos(), "Quantidade de contatos errada");
		assertEquals(james, gcont.getContato(JAMES), "Contato adicionado deve poder ser recuperado");
	}

	@Test
	@DisplayName("Não deve adicionar contato com identificador duplicado")
	public void adicionarContatoDuplicado() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");
		assertFalse(gcont.criarContato(JAMES, "jesus2@ufc.com"), "Contato com id duplicado");
		assertEquals(1, gcont.getNumeroDeContatos(), "Quantidade de contatos errada");
		assertEquals(JAMES_EMAIL, gcont.getContato(JAMES).getEmail(), "O email do contato original deve ser mantido");
	}

	@Test
	@DisplayName("Deve remover um contato existente")
	public void removendoContato() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");
		assertEquals(1, gcont.getNumeroDeContatos(), "Quantidade de contatos errada");
		assertTrue(gcont.removerContato(JAMES), "Contato deve ser removido");
		assertEquals(0, gcont.getNumeroDeContatos(), "Quantidade de contatos errada");
		assertNull(gcont.getContato(JAMES), "Contato removido nao deve ser encontrado");
	}

	@Test
	@DisplayName("Não deve remover um contato inexistente")
	public void removendoContatoInexistente() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");
		assertEquals(1, gcont.getNumeroDeContatos(), "Quantidade de contatos errada");
		assertFalse(gcont.removerContato("ramiro"), "Contato nao cadastrado nao pode ser removido");
		assertEquals(1, gcont.getNumeroDeContatos(), "Quantidade de contatos errada");
	}

	@Test
	@DisplayName("Deve recuperar um contato existente pelo identificador")
	public void recuperandoContato() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");
		Contato recuperado = gcont.getContato(JAMES);
		assertEquals(james, recuperado, "Contato recuperado diferente do buscado");
		assertEquals(JAMES_EMAIL, recuperado.getEmail(), "Email do contato recuperado esta errado");
	}

	@Test
	@DisplayName("Deve retornar null ao buscar um contato inexistente")
	public void recuperandoContatoInexistene() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");
		assertNull(gcont.getContato("ramiro"), "Contato nao existente");
	}

	@Test
	@DisplayName("Deve listar todos os contatos em ordem alfabética")
	public void recuperandoTodosOsContatos() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");
		assertTrue(gcont.criarContato(ANA, ANA_EMAIL), "Contato valido, deve ser adicionado");
		assertTrue(gcont.criarContato(JOSE, JOSE_EMAIL), "Contato valido, deve ser adicionado");

		assertEquals(List.of(ana, james, jose), gcont.getTodosContatos(), "Lista de contatos errada");
	}

	@Test
	@DisplayName("Deve atualizar o email de um contato existente")
	public void atualizandoContato() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");

		assertTrue(gcont.atualizarContato(new Contato(JAMES, "novo@ufc.br")), "Contato valido, deve ser atualizado");
		assertEquals("novo@ufc.br", gcont.getContato(JAMES).getEmail(), "Email do contato nao foi atualizado");
		assertEquals(1, gcont.getNumeroDeContatos(), "Atualizar nao deve criar um novo contato");
	}

	@Test
	@DisplayName("Não deve atualizar um contato inexistente")
	public void atualizandoInexistente() {
		assertFalse(gcont.atualizarContato(james), "Contato nao existente, logo nao pode ser atualizado");
		assertNull(gcont.getContato(JAMES), "Atualizar nao deve cadastrar o contato");
	}

	@Test
	@DisplayName("Deve favoritar um contato existente")
	public void favoritandoUmContato() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");
		assertTrue(gcont.favoritar(JAMES), "Contato deve ser marcado como favorito");
		assertTrue(gcont.eFavorito(JAMES), "Contato esta na lista de favoritos");
		assertFalse(gcont.eFavorito(ANA), "Contato nao esta na lista de favoritos");
	}

	@Test
	@DisplayName("Não deve favoritar um contato inexistente")
	public void favoritandoUmContatoInexistente() {
		assertFalse(gcont.favoritar(JAMES), "Contato nao existe");
		assertFalse(gcont.eFavorito(JAMES), "Contato nao esta na lista de favoritos");
	}

	@Test
	@DisplayName("Deve desfavoritar um contato favorito")
	public void desfavoritandoUmContato() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");
		assertTrue(gcont.favoritar(JAMES), "Contato deve ser marcado como favorito");
		assertTrue(gcont.eFavorito(JAMES), "Contato esta na lista de favoritos");

		assertTrue(gcont.desfavoritar(JAMES), "Contato nao removido dos favoritos");
		assertFalse(gcont.eFavorito(JAMES), "Contato nao esta na lista de favoritos");
	}

	@Test
	@DisplayName("Não deve desfavoritar um contato inexistente")
	public void desfavoritandoUmContatoInexistente() {
		assertFalse(gcont.desfavoritar(JAMES), "Contato nao existe");
		assertFalse(gcont.eFavorito(JAMES), "Contato nao esta na lista de favoritos");
	}

	@Test
	@DisplayName("Deve listar os favoritos em ordem alfabética e retirar da lista contatos desfavoritados ou removidos")
	public void recuperandoTodosOsFavoritos() {
		assertTrue(gcont.criarContato(JAMES, JAMES_EMAIL), "Contato valido, deve ser adicionado");
		assertTrue(gcont.criarContato(MARIO, MARIO_EMAIL), "Contato valido, deve ser adicionado");
		assertTrue(gcont.criarContato(ANA, ANA_EMAIL), "Contato valido, deve ser adicionado");

		assertTrue(gcont.favoritar(JAMES), "Contato deve ser marcado como favorito");
		assertTrue(gcont.favoritar(ANA), "Contato deve ser marcado como favorito");
		assertTrue(gcont.favoritar(MARIO), "Contato deve ser marcado como favorito");

		assertTrue(gcont.eFavorito(ANA), "O contato esta na lista de favoritos");
		assertFalse(gcont.eFavorito(JOSE), "O contato nao esta na lista de favoritos");

		assertEquals(List.of(ana, james, mario), gcont.getFavoritos(), "Lista de favoritos errada");

		assertTrue(gcont.desfavoritar(ANA), "Contato deve ser removido dos favoritos");
		assertEquals(List.of(james, mario), gcont.getFavoritos(), "Lista de favoritos errada apos desfavoritar");

		assertTrue(gcont.removerContato(MARIO), "Contato deve ser removido");
		assertEquals(List.of(james), gcont.getFavoritos(), "Contato removido deve sair dos favoritos");
	}
}
