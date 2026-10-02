package org.colecoes;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.colecoes.dominio.Contato;
import org.colecoes.dominio.comparator.ContatoPorNome;
import org.colecoes.dominio.comparator.ContatoPorTelefone;
import org.colecoes.dominio.comparator.ContatoSomentePorNome;
import org.colecoes.src.arvorebinaria.ArvoreBinaria;
import org.colecoes.src.colecao.IColecao;
import org.colecoes.src.listaencadeada.ListaEncadeada;

public class Main {

    private static final Path ARQUIVO_ENTRADA = Path.of("entrada.txt");
    static Scanner scanner = new Scanner(System.in);
    static IColecao<Contato> colecaoPorNome;
    static IColecao<Contato> colecaoPorTelefone;
    static TipoColecao tipoColecao;

    public static void main(String[] args) {
        tipoColecao = selecionarTipoColecao();
        colecaoPorNome = criarColecao(new ContatoPorNome());
        colecaoPorTelefone = criarColecao(new ContatoPorTelefone());

        String opcao;
        do {
            exibirMenu();
            opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> carregarDadosIniciais();
                case "2" -> adicionarContato();
                case "3" -> pesquisarContatoPorNome();
                case "4" -> pesquisarContatoPorTelefone();
                case "5" -> removerContato();
                case "6" -> listarContatos();
                case "7" -> alterarContato();
                case "0" -> {
                    System.out.println("Quantidade atual de contatos: "
                            + colecaoPorNome.quantidadeNos());
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (!opcao.equals("0"));
    }

    private static void carregarDadosIniciais() {
        System.out.println("Carregando dados do arquivo " + ARQUIVO_ENTRADA + "...");

        long inicio = System.nanoTime();
        int quantidadeLida = 0;
        IColecao<Contato> novaColecaoPorNome = criarColecao(new ContatoPorNome());
        IColecao<Contato> novaColecaoPorTelefone = criarColecao(new ContatoPorTelefone());
        Set<String> telefonesCadastrados = new HashSet<>();

        // Implementado com IA
        try (BufferedReader leitor = Files.newBufferedReader(
                ARQUIVO_ENTRADA, StandardCharsets.UTF_8)) {

            String cabecalho = leitor.readLine();
            if (cabecalho == null) {
                throw new IOException("O arquivo está vazio.");
            }

            int quantidadeEsperada = Integer.parseInt(cabecalho.trim());
            String linha;
            int numeroLinha = 1;

            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;
                if (linha.isBlank()) {
                    continue;
                }

                String[] dados = linha.split(";", -1);
                if (dados.length != 2 || dados[0].isBlank() || dados[1].isBlank()) {
                    throw new IOException("Contato inválido na linha " + numeroLinha + ".");
                }

                String nome = dados[0].trim();
                String telefone = dados[1].trim();
                if (!telefonesCadastrados.add(telefone)) {
                    throw new IOException(
                            "Telefone duplicado na linha " + numeroLinha + ": " + telefone + ".");
                }

                Contato contato = new Contato(nome, telefone);
                novaColecaoPorNome.adicionar(contato);
                novaColecaoPorTelefone.adicionar(contato);
                quantidadeLida++;
            }

            if (quantidadeLida != quantidadeEsperada) {
                throw new IOException(
                        "O cabeçalho informa " + quantidadeEsperada
                                + " contatos, mas foram lidos " + quantidadeLida + ".");
            }

            long tempo = System.nanoTime() - inicio;
            System.out.println(quantidadeLida + " contatos carregados com sucesso.");
            System.out.printf("Tempo de leitura e montagem da coleção: %d ns (%.3f ms)%n",
                    tempo, tempo / 1_000_000.0);
            colecaoPorNome = novaColecaoPorNome;
            colecaoPorTelefone = novaColecaoPorTelefone;
        } catch (NoSuchFileException e) {
            System.out.println("Arquivo de entrada não encontrado: "
                    + ARQUIVO_ENTRADA.toAbsolutePath());
        } catch (NumberFormatException e) {
            System.out.println("Cabeçalho inválido no arquivo " + ARQUIVO_ENTRADA + ".");
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " + e.getMessage());
        }
    }

    private static void adicionarContato() {
        System.out.println("Digite o nome do contato:");
        String nome = scanner.nextLine().trim();

        System.out.println("Digite o telefone do contato:");
        String telefone = scanner.nextLine().trim();

        Contato contato = new Contato(nome, telefone);

        if (colecaoPorTelefone.pesquisar(contato) != null) {
            System.out.println("Já existe um contato cadastrado com esse telefone.");
            return;
        }

        colecaoPorNome.adicionar(contato);
        colecaoPorTelefone.adicionar(contato);
        System.out.println("Contato adicionado com sucesso.");
    }

    private static void pesquisarContatoPorNome() {
        System.out.println("Digite o nome do contato a ser pesquisado:");
        String nome = scanner.nextLine().trim();

        long inicio = System.nanoTime();
        Contato contatoPesquisado = buscarContatoPorNome(nome);
        long tempo = System.nanoTime() - inicio;

        if (contatoPesquisado != null) {
            System.out.println("Contato encontrado: " + contatoPesquisado);
        } else {
            System.out.println("Contato não encontrado.");
        }
        System.out.printf("Tempo da pesquisa por nome: %d ns (%.3f ms)%n",
                tempo, tempo / 1_000_000.0);
    }

    private static void pesquisarContatoPorTelefone() {
        System.out.println("Digite o telefone do contato a ser pesquisado:");
        String telefone = scanner.nextLine().trim();

        Contato chavePesquisa = new Contato("", telefone);

        long inicio = System.nanoTime();
        Contato contatoPesquisado = colecaoPorTelefone.pesquisar(chavePesquisa);
        long tempo = System.nanoTime() - inicio;

        if (contatoPesquisado != null) {
            System.out.println("Contato encontrado: " + contatoPesquisado);
        } else {
            System.out.println("Contato não encontrado.");
        }
        System.out.printf("Tempo da pesquisa por telefone: %d ns (%.3f ms)%n",
                tempo, tempo / 1_000_000.0);
    }

    private static void removerContato() {
        System.out.println("Digite o telefone do contato a ser removido:");
        String telefone = scanner.nextLine().trim();

        Contato chavePesquisa = new Contato("", telefone);
        Contato contato = colecaoPorTelefone.pesquisar(chavePesquisa);

        long inicio = System.nanoTime();
        boolean removidoPorTelefone = colecaoPorTelefone.remover(chavePesquisa);
        boolean removidoPorNome = !removidoPorTelefone
                || colecaoPorNome.remover(contato);
        if (removidoPorTelefone && !removidoPorNome) {
            colecaoPorTelefone.adicionar(contato);
        }
        long tempo = System.nanoTime() - inicio;

        boolean removido = removidoPorTelefone && removidoPorNome;

        if (removido) {
            System.out.println("Contato removido com sucesso.");
        } else {
            System.out.println("Contato não encontrado para remoção.");
        }
        System.out.printf("Tempo da remoção por telefone: %d ns (%.3f ms)%n",
                tempo, tempo / 1_000_000.0);
    }

    private static void listarContatos() {
        System.out.println("Contatos:");
        System.out.println(colecaoPorNome);
    }

    private static void alterarContato() {
        System.out.println("Digite o nome do contato a ser alterado:");
        String nome = scanner.nextLine().trim();

        Contato contatoAlteracao = buscarContatoPorNome(nome);

        if (contatoAlteracao == null) {
            System.out.println("Contato não encontrado para alteração.");
            return;
        }

        System.out.println("Telefone atual: " + contatoAlteracao.getTelefone());
        System.out.println("Digite o novo nome do contato:");
        String novoNome = scanner.nextLine().trim();

        System.out.println("Digite o novo telefone do contato:");
        String novoTelefone = scanner.nextLine().trim();

        if (!novoTelefone.equalsIgnoreCase(contatoAlteracao.getTelefone())
                && colecaoPorTelefone.pesquisar(new Contato("", novoTelefone)) != null) {
            System.out.println("Já existe um contato cadastrado com esse telefone.");
            return;
        }

        Contato contatoAtualizado = new Contato(novoNome, novoTelefone);
        boolean removidoPorNome = colecaoPorNome.remover(contatoAlteracao);
        boolean removidoPorTelefone = colecaoPorTelefone.remover(contatoAlteracao);
        if (!removidoPorNome || !removidoPorTelefone) {
            if (removidoPorNome) colecaoPorNome.adicionar(contatoAlteracao);
            if (removidoPorTelefone) colecaoPorTelefone.adicionar(contatoAlteracao);
            System.out.println("Não foi possível alterar o contato.");
            return;
        }
        colecaoPorNome.adicionar(contatoAtualizado);
        colecaoPorTelefone.adicionar(contatoAtualizado);

        System.out.println("Contato atualizado: " + contatoAtualizado);
    }

    private static Contato buscarContatoPorNome(String nome) {
        Contato chavePesquisa = new Contato(nome, "");
        Comparator<Contato> comparadorPorNome = new ContatoSomentePorNome();

        if (colecaoPorNome instanceof ListaEncadeada<?>) {
            ListaEncadeada<Contato> lista = (ListaEncadeada<Contato>) colecaoPorNome;
            return lista.pesquisar(chavePesquisa, comparadorPorNome);
        }

        if (colecaoPorNome instanceof ArvoreBinaria<?>) {
            ArvoreBinaria<Contato> arvore = (ArvoreBinaria<Contato>) colecaoPorNome;
            return arvore.pesquisar(chavePesquisa, comparadorPorNome);
        }

        throw new IllegalArgumentException("Tipo de coleção não suportado.");
    }

    private static IColecao<Contato> criarColecao(Comparator<Contato> comparador) {
        return switch (tipoColecao) {
            case LISTA_NAO_ORDENADA -> new ListaEncadeada<>(comparador, false);
            case LISTA_ORDENADA -> new ListaEncadeada<>(comparador, true);
            case ARVORE_BINARIA -> new ArvoreBinaria<>(comparador);
        };
    }

    private static TipoColecao selecionarTipoColecao() {
        while (true) {
            System.out.println("Escolha a coleção que deseja utilizar:");
            System.out.println("1 - Lista não ordenada");
            System.out.println("2 - Lista ordenada");
            System.out.println("3 - Árvore binária");

            switch (scanner.nextLine().trim()) {
                case "1":
                    return TipoColecao.LISTA_NAO_ORDENADA;
                case "2":
                    return TipoColecao.LISTA_ORDENADA;
                case "3":
                    return TipoColecao.ARVORE_BINARIA;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private enum TipoColecao {
        LISTA_NAO_ORDENADA,
        LISTA_ORDENADA,
        ARVORE_BINARIA
    }

    private static void exibirMenu() {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Carregar dados iniciais de um arquivo");
        System.out.println("2 - Adicionar contato");
        System.out.println("3 - Pesquisar contato por nome");
        System.out.println("4 - Pesquisar contato por telefone");
        System.out.println("5 - Remover contato");
        System.out.println("6 - Listar contatos");
        System.out.println("7 - Alterar contato");
        System.out.println("0 - Sair");
    }
}
