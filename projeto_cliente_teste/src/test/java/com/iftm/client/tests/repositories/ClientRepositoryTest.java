package com.iftm.client.tests.repositories;

import com.iftm.client.entities.Client;
import com.iftm.client.repositories.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository repositorio;

    /* Cenário de Teste 1
    * Testar o método que retorna o cliente com nome existente;
    *   - Testar um nome existente;
    *   - Testar um nome não existente;
    * */

    @Test
    public void testarSeFindByNameRetornaNomeExistente() {
        String name = "gilberto gil";
        Optional<Client> cliente = repositorio.findByNameIgnoreCase(name);

        Assertions.assertTrue(cliente.isPresent());
        Assertions.assertEquals(name, cliente.get().getName().toLowerCase(Locale.ROOT));
    }

    @Test
    public void testarSeFindByNameRetornaNomeInexistente() {
        String name = "ana";
        Optional<Client> cliente = repositorio.findByNameIgnoreCase(name);
        Assertions.assertFalse(cliente.isPresent());
    }

    /* Cenário de Teste 2
    * Testar o método que retorna vários cliente com parte do nome similar ao texto informado;
    *   - Testar um texto existente;
    *   - Testar um texto não existente;
    *   - Testar find para nome vazio (Neste caso teria que retornar todos os clientes);
    * */

    @Test
    public void testarSeFindByNameLikeRetornaClientesExistentesContendoCaracteres() {
        String name = "%jo%";
        List<Client> clientesRecebidos = repositorio.findByNameLikeIgnoreCase(name);

        String clientesEsperados[] = {"Jose Saramago","Jorge Amado"};

        Assertions.assertFalse(clientesRecebidos.isEmpty());
        Assertions.assertEquals(2, clientesRecebidos.size());

        for( int i=0; i < clientesEsperados.length; i++) {
            Assertions.assertEquals(clientesEsperados[i], clientesRecebidos.get(i).getName());
        }
    }

    @Test
    public void testarSeFindByNameLikeRetornaClientesInexistentesContendoCaracteres() {
        String name = "%ya%";
        List<Client> clientesRecebidos = repositorio.findByNameLikeIgnoreCase(name);

        Integer quantidadeEsperada = 0;

        Assertions.assertTrue(clientesRecebidos.isEmpty());
        Assertions.assertEquals(quantidadeEsperada, clientesRecebidos.size());
    }

    @Test
    public void testarSeFindByNameLikeRetornaTodosOsClientesCasoNomeSejaVazio() {
        String name = "%";
        List<Client> clientesRecebidos = repositorio.findByNameLikeIgnoreCase(name);

        String clientesEsperados[] = {"Conceição Evaristo","Lázaro Ramos","Clarice Lispector","Carolina Maria de Jesus", "Gilberto Gil",
            "Djamila Ribeiro", "Jose Saramago", "Toni Morrison", "Yuval Noah Harari", "Chimamanda Adichie", "Silvio Almeida", "Jorge Amado"};

        Assertions.assertFalse(clientesRecebidos.isEmpty());
        Assertions.assertEquals(12, clientesRecebidos.size());

        for( int i=0; i < clientesEsperados.length; i++) {
            Assertions.assertEquals(clientesEsperados[i], clientesRecebidos.get(i).getName());
        }
    }

    /* Cenário de Teste 3
     * Testar o método que retorna vários clientes baseado no salário;
     *   - Testar o método que busca clientes com salários superiores a um valor;
     *   - Testar o método que busca clientes com salários inferiores a um valor;
     *   - Testar o método que busca clientes com salários que esteja no intervalo entre dois valores informados.
     * */

    @Test
    public void testarSefindByIncomeGreaterThanRetornaSalariosSuperioresAoValor() {
        Double valor = 5000.00;
        List<Client> clientesRecebidos = repositorio.findByIncomeGreaterThan(valor);

        String clientesEsperados[] = {"Carolina Maria de Jesus", "Toni Morrison"};

        Assertions.assertFalse(clientesRecebidos.isEmpty());
        Assertions.assertEquals(2, clientesRecebidos.size());

        for(int i=0; i < clientesEsperados.length; i++) {
            Assertions.assertEquals(clientesEsperados[i], clientesRecebidos.get(i).getName());
        }
    }

    @Test
    public void testarSefindByIncomeLessThanRetornaSalariosInferioresAoValor() {
        Double valor = 3000.00;
        List<Client> clientesRecebidos = repositorio.findByIncomeLessThan(valor);

        String clientesEsperados[] = {"Conceição Evaristo", "Lázaro Ramos", "Gilberto Gil", "Yuval Noah Harari",
                "Chimamanda Adichie", "Jorge Amado"};

        Assertions.assertFalse(clientesRecebidos.isEmpty());
        Assertions.assertEquals(6, clientesRecebidos.size());

        for(int i=0; i < clientesEsperados.length; i++) {
            Assertions.assertEquals(clientesEsperados[i], clientesRecebidos.get(i).getName());
        }
    }

    @Test
    public void testarSeFindByIncomeBetweenRetornaSalariosEntreRangeDeValor() {
        Double valorMin = 1000.00;
        Double valorMax = 2000.00;
        List<Client> clientesRecebidos = repositorio.findByIncomeBetween(valorMin, valorMax);

        String clientesEsperados[] = {"Conceição Evaristo", "Yuval Noah Harari", "Chimamanda Adichie"};

        Assertions.assertFalse(clientesRecebidos.isEmpty());
        Assertions.assertEquals(3, clientesRecebidos.size());

        for (int i=0; i < clientesEsperados.length; i++) {
            Assertions.assertEquals(clientesEsperados[i], clientesRecebidos.get(i).getName());
        }
    }

    /* Cenário de Teste 4
     * Testar o método que retorna vários clientes baseado na sua data de aniversário.
     *   - Teste o método buscando clientes que nasceram entre duas datas, sugestão uma data qualquer e a data atual.
     * */

    @Test
    public void testarSeFindClientBybirthDateBetweenRetornaClientesComDataAniversarioEmRange() {
        Instant dataInicio = Instant.parse("2017-12-25T20:30:50Z");
        Instant dataTermino = Instant.now();
        List<Client> clienteRecebido = repositorio.findClientBybirthDateBetween(dataInicio, dataTermino);

        String clienteEsperado = "Conceição Evaristo";

        Assertions.assertFalse(clienteRecebido.isEmpty());
        Assertions.assertEquals(1, clienteRecebido.size());
        Assertions.assertEquals(clienteEsperado, clienteRecebido.get(0).getName());
    }

    /* Cenário de Teste 5
     * Testar o update (save) de um cliente. Modifique o nome, o salário e o aniversário e utilize os métodos criados
     * anteriormente para verificar se realmente foram modificados.
     * */

    @Test
    public void testarSeFindByNameRetornaNomeExistenteAposAlterarNome() {
        Long id = 1L;
        Optional<Client> clienteInicial = repositorio.findById(id);

        Assertions.assertTrue(clienteInicial.isPresent());

        clienteInicial.get().setName("Ana Maria Silva");
        clienteInicial.get().setIncome(10000.00);
        clienteInicial.get().setBirthDate(Instant.now());

        repositorio.save(clienteInicial.get());
        Optional<Client> clienteModificado = repositorio.findByNameIgnoreCase("Ana Maria Silva");

        Assertions.assertTrue(clienteModificado.isPresent());
        Assertions.assertEquals("Ana Maria Silva", clienteModificado.get().getName());
    }

}
