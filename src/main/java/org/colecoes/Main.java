package org.colecoes;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.colecoes.dominio.Contato;
import org.colecoes.dominio.comparator.ContatoPorTelefone;
import org.colecoes.src.colecao.IColecao;
import org.colecoes.src.listaencadeada.ListaEncadeada;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Instanciar lista ordenada? (S/N)");

        Boolean ordenada = "S".equalsIgnoreCase(scanner.nextLine().trim());

        IColecao<Contato> lista = new ListaEncadeada<>(new ContatoPorTelefone(), ordenada);

        String opcao;
        do {
            exibirMenu();
            opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> carregarDadosIniciais(lista);
                case "2" -> adicionarContato(lista);
                case "3" -> pesquisarContatoPorNome(lista);
                case "4" -> pesquisarContatoPorTelefone(lista);
                case "5" -> removerContato(lista);
                case "6" -> listarContatos(lista);
                case "7" -> alterarContato(lista);
                case "0" -> {
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (!opcao.equals("0"));
    }

    private static void carregarDadosIniciais(IColecao<Contato> lista) {
        System.out.println("Escolha um arquivo para carregar os dados iniciais:");
        System.out.println("1 - contatos-100000.txt");
        System.out.println("2 - contatos-200000.txt");
        System.out.println("3 - contatos-300000.txt");
        System.out.println("4 - contatos-400000.txt");

        String opcao = scanner.nextLine().trim();
        String nomeArquivo = switch (opcao) {
            case "1" -> "src/main/java/org/colecoes/dominio/dados/contatos-100000.txt";
            case "2" -> "src/main/java/org/colecoes/dominio/dados/contatos-200000.txt";
            case "3" -> "src/main/java/org/colecoes/dominio/dados/contatos-300000.txt";
            case "4" -> "src/main/java/org/colecoes/dominio/dados/contatos-400000.txt";
            default -> null;
        };

        if (nomeArquivo == null) {
            System.out.println("Opção inválida.");
            return;
        }

        long inicio = System.nanoTime();
        int quantidadeLida = 0;
        Set<String> telefonesCadastrados = new HashSet<>();
        for (Contato contato : contatosDaLista(lista)) {
            telefonesCadastrados.add(contato.getTelefone());
        }

        // Implementado com IA
        try (BufferedReader leitor = Files.newBufferedReader(
                Path.of(nomeArquivo), StandardCharsets.UTF_8)) {

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

                lista.adicionar(new Contato(nome, telefone));
                quantidadeLida++;
            }

            if (quantidadeLida != quantidadeEsperada) {
                throw new IOException(
                        "O cabeçalho informa " + quantidadeEsperada
                                + " contatos, mas foram lidos " + quantidadeLida + ".");
            }

            long tempo = System.nanoTime() - inicio;
            System.out.println(quantidadeLida + " contatos carregados com sucesso.");
            System.out.printf("Tempo de leitura e montagem da lista: %d ns (%.3f ms)%n",
                    tempo, tempo / 1_000_000.0);
        } catch (NoSuchFileException e) {
            System.out.println("Arquivo não encontrado: " + nomeArquivo);
        } catch (NumberFormatException e) {
            System.out.println("Cabeçalho inválido no arquivo " + nomeArquivo + ".");
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " + e.getMessage());
        }
    }

    private static void adicionarContato(IColecao<Contato> lista) {
        System.out.println("Digite o nome do contato:");
        String nome = scanner.nextLine().trim();

        System.out.println("Digite o telefone do contato:");
        String telefone = scanner.nextLine().trim();

        if (buscarContatoPorTelefone(lista, telefone) != null) {
            System.out.println("Já existe um contato cadastrado com esse telefone.");
            return;
        }

        Contato novoContato = new Contato(nome, telefone);
        lista.adicionar(novoContato);
        System.out.println("Contato adicionado com sucesso.");
    }

    private static void pesquisarContatoPorNome(IColecao<Contato> lista) {
        System.out.println("Digite o nome do contato a ser pesquisado:");
        String nome = scanner.nextLine().trim();

        Contato contatoPesquisado = buscarContatoPorNome(lista, nome);
        if (contatoPesquisado != null) {
            System.out.println("Contato encontrado: " + contatoPesquisado);
        } else {
            System.out.println("Contato não encontrado.");
        }
    }

    private static void pesquisarContatoPorTelefone(IColecao<Contato> lista) {
        System.out.println("Digite o telefone do contato a ser pesquisado:");
        String telefone = scanner.nextLine().trim();

        Contato contatoPesquisado = buscarContatoPorTelefone(lista, telefone);
        if (contatoPesquisado != null) {
            System.out.println("Contato encontrado: " + contatoPesquisado);
        } else {
            System.out.println("Contato não encontrado.");
        }
    }

    private static void removerContato(IColecao<Contato> lista) {
        System.out.println("Digite o telefone do contato a ser removido:");
        String telefone = scanner.nextLine().trim();

        boolean removido = lista.remover(new Contato("", telefone));
        if (removido) {
            System.out.println("Contato removido com sucesso.");
        } else {
            System.out.println("Contato não encontrado para remoção.");
        }
    }

    private static void listarContatos(IColecao<Contato> lista) {
        System.out.println("Lista de contatos:");
        for (Contato contato : contatosDaLista(lista)) {
            System.out.println(contato);
        }
    }

    private static void alterarContato(IColecao<Contato> lista) {
        System.out.println("Digite o telefone do contato a ser alterado:");
        String telefoneAlteracao = scanner.nextLine().trim();
        Contato contatoAlteracao = buscarContatoPorTelefone(lista, telefoneAlteracao);
        if (contatoAlteracao != null) {
            System.out.println("Contato encontrado: " + contatoAlteracao);
            System.out.println("Digite o novo nome do contato:");

            String novoNome = scanner.nextLine().trim();

            lista.remover(contatoAlteracao);
            lista.adicionar(new Contato(novoNome, contatoAlteracao.getTelefone()));

            System.out.println("Contato atualizado: " + contatoAlteracao);
        } else {
            System.out.println("Contato não encontrado para alteração.");
        }
    }

    private static Contato buscarContatoPorTelefone(IColecao<Contato> lista, String telefone) {
        return lista.pesquisar(new Contato("", telefone));
    }

    private static Contato buscarContatoPorNome(IColecao<Contato> lista, String nome) {
        for (Contato contato : contatosDaLista(lista)) {
            if (contato.getNome().equalsIgnoreCase(nome)) {
                return contato;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static Iterable<Contato> contatosDaLista(IColecao<Contato> lista) {
        if (lista instanceof Iterable<?>) {
            return (Iterable<Contato>) lista;
        }
        throw new IllegalArgumentException("A coleção informada não pode ser percorrida.");
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
