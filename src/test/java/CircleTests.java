import java.util.List;

import exceptions.CirculoNotFoundException;
import exceptions.ContatoNotFoundException;
import model.Circulo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Gerenciamento de círculos")
public class CircleTests {
	private static final String AMIGOS = "amigos";
	private static final String TRABALHO = "trabalho";
	private static final String FAMILIA = "familia";

	private GContatos gcont;
	private Circulo familia, trabalho, amigos;

	@BeforeEach
	public void setUp() {
		familia = new Circulo(FAMILIA, 3);
		trabalho = new Circulo(TRABALHO, 3);
		amigos = new Circulo(AMIGOS, 1);

		gcont = new GContatos();
	}

	/**
	 * Cria {@code quantidade} contatos novos e tenta adicioná-los ao círculo.
	 * @return quantos contatos o círculo aceitou
	 */
	private int adicionarContatos(String idCirculo, int quantidade) throws CirculoNotFoundException, ContatoNotFoundException {
		int adicionados = 0;
		for (int i = 1; i <= quantidade; i++) {
			String id = idCirculo + "-contato" + i;
			gcont.criarContato(id, id + "@ufc.br");
			if (gcont.adicionarContatoAoCirculo(id, idCirculo)) {
				adicionados++;
			}
		}
		return adicionados;
	}

	@Test
	@DisplayName("Deve adicionar círculos com identificadores distintos")
	public void adicionarCirculos() {
		assertTrue(gcont.criarCirculo(FAMILIA, 3), "O circulo deve ser adicionado");
		assertTrue(gcont.criarCirculo(AMIGOS, 1), "O circulo deve ser adicionado");
		assertTrue(gcont.criarCirculo(TRABALHO, 3), "O circulo deve ser adicionado");

		assertEquals(3, gcont.getNumeroDeCirculos(), "Todos os 3 circulos devem ser adicionados");
	}

	@Test
	@DisplayName("Não deve adicionar círculo com limite menor ou igual a zero")
	public void adicionarCirculoLimiteInvalido() {
		assertFalse(gcont.criarCirculo(TRABALHO, 0), "Circulo com limite zero nao deve ser adicionado");
		assertFalse(gcont.criarCirculo(TRABALHO, -5), "Circulo com limite negativo nao deve ser adicionado");

		assertEquals(0, gcont.getNumeroDeCirculos(), "Nenhum circulo deveria ter sido adicionado");
		assertNull(gcont.getCirculo(TRABALHO), "Circulo com limite invalido nao deve ser cadastrado");
	}

	@Test
	@DisplayName("Não deve adicionar círculo com identificador duplicado")
	public void adicionarCirculoDuplicado() {
		assertTrue(gcont.criarCirculo(TRABALHO, 3), "O circulo deve ser adicionado");
		assertFalse(gcont.criarCirculo(TRABALHO, 5), "Circulo com id duplicado nao deve ser adicionado");

		assertEquals(1, gcont.getNumeroDeCirculos(), "Apenas um circulo devia ter sido adicionado");
	}

	@Test
	@DisplayName("Deve recuperar um círculo existente pelo identificador")
	public void buscandoCirculoExistente() {
		assertTrue(gcont.criarCirculo(FAMILIA, 3), "O circulo deve ser adicionado");
		assertEquals(familia, gcont.getCirculo(FAMILIA), "O circulo recuperado nao e o procurado");
	}

	@Test
	@DisplayName("Deve retornar null ao buscar um círculo inexistente")
	public void buscandoCirculoInexistente() {
		assertNull(gcont.getCirculo("inimigos"), "Circulo nao existente");
	}

	@Test
	@DisplayName("Deve listar todos os círculos em ordem alfabética")
	public void recuperandoTodosOsCirculos() {
		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarCirculo(AMIGOS, 1);
		gcont.criarCirculo(TRABALHO, 3);

		assertEquals(List.of(amigos, familia, trabalho), gcont.getTodosCirculos(), "Lista de circulos incorreta");
	}

	@Test
	@DisplayName("Deve remover um círculo existente")
	public void removendoCirculoExistente() {
		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarCirculo(AMIGOS, 1);
		gcont.criarCirculo(TRABALHO, 3);

		assertTrue(gcont.removerCirculo(AMIGOS), "Circulo nao removido");
		assertEquals(2, gcont.getNumeroDeCirculos(), "Quantidade de circulos errada");
		assertEquals(List.of(familia, trabalho), gcont.getTodosCirculos(), "Lista de circulos incorreta");
		assertNull(gcont.getCirculo(AMIGOS), "Circulo removido nao deve ser encontrado");
	}

	@Test
	@DisplayName("Não deve remover um círculo inexistente")
	public void removendoCirculoInexistente() {
		gcont.criarCirculo(FAMILIA, 3);
		gcont.criarCirculo(AMIGOS, 1);
		gcont.criarCirculo(TRABALHO, 3);

		assertFalse(gcont.removerCirculo("inimigos"), "Circulo nao existe, logo nao pode ser removido");
		assertEquals(3, gcont.getNumeroDeCirculos(), "Quantidade de circulos errada");
		assertEquals(List.of(amigos, familia, trabalho), gcont.getTodosCirculos(), "Lista de circulos incorreta");
	}

	@Test
	@DisplayName("Deve atualizar o limite de um círculo existente")
	public void atualizandoCirculoExistente() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);
		assertTrue(gcont.atualizarCirculo(new Circulo(FAMILIA, 4)), "O circulo deve ser atualizado");
		assertEquals(4, adicionarContatos(FAMILIA, 5), "Com o novo limite, o circulo deve comportar 4 contatos");
	}

	@Test
	@DisplayName("Não deve atualizar um círculo para limite menor ou igual a zero")
	public void atualizandoCirculoLimiteInvalido() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);
		assertFalse(gcont.atualizarCirculo(new Circulo(FAMILIA, 0)), "O circulo possui limite invalido");
		assertFalse(gcont.atualizarCirculo(new Circulo(FAMILIA, -1)), "O circulo possui limite invalido");
		assertEquals(3, adicionarContatos(FAMILIA, 4), "O limite original (3) deve ser mantido");
	}

	@Test
	@DisplayName("Não deve reduzir o limite de um círculo abaixo do número de contatos que ele já possui")
	public void atualizandoCirculoLimiteMenorQueContatos() throws CirculoNotFoundException, ContatoNotFoundException {
		gcont.criarCirculo(FAMILIA, 3);
		adicionarContatos(FAMILIA, 3);

		assertFalse(gcont.atualizarCirculo(new Circulo(FAMILIA, 2)), "O novo limite e menor que o numero de contatos do circulo");
		assertEquals(3, gcont.getCirculo(FAMILIA).getNumeroDeContatos(), "Nenhum contato deve sair do circulo");
		assertTrue(gcont.atualizarCirculo(new Circulo(FAMILIA, 3)), "Limite igual ao numero de contatos e valido");
	}

	@Test
	@DisplayName("Não deve atualizar um círculo inexistente")
	public void atualizandoCirculoInexistente() {
		assertFalse(gcont.atualizarCirculo(new Circulo("inimigos", 4)), "Circulo nao existente");
		assertNull(gcont.getCirculo("inimigos"), "Atualizar nao deve cadastrar o circulo");
	}
}
