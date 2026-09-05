package org.colecoes;

import java.util.Comparator;
import java.util.Scanner;

import org.colecoes.dominio.Contato;
import org.colecoes.src.listaencadeada.ListaEncadeada;

public class Main {
    private static final Comparator<Contato> POR_NOME = Comparator.comparing(Contato::getNome,
            String.CASE_INSENSITIVE_ORDER);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Instanciar lista ordenada? (S/N)");

        Boolean ordenada = "S".equalsIgnoreCase(scanner.nextLine().trim());
        
        ListaEncadeada<Contato> lista = new ListaEncadeada<>(POR_NOME, ordenada);

        String opcao;
        do {
            exibirMenu();
            opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1":
                    System.out.println("Digite o nome do contato:");
                    String nome = scanner.nextLine().trim();
                    System.out.println("Digite o telefone do contato:");
                    String telefone = scanner.nextLine().trim();
                    Contato novoContato = new Contato(nome, telefone);
                    lista.adicionar(novoContato);
                    break;
                case "2":
                    System.out.println("Digite o nome do contato a ser pesquisado:");
                    String nomePesquisa = scanner.nextLine().trim();
                    Contato contatoPesquisado = lista.pesquisar(new Contato(nomePesquisa, ""));
                    if (contatoPesquisado != null) {
                        System.out.println("Contato encontrado: " + contatoPesquisado);
                    } else {
                        System.out.println("Contato não encontrado."); 
                    }
                    break;
                case "3":
                    System.out.println("Digite o nome do contato a ser removido:");
                    String nomeRemocao = scanner.nextLine().trim();
                    boolean removido = lista.remover(new Contato(nomeRemocao, ""));
                    if (removido) {
                        System.out.println("Contato removido com sucesso.");
                    } else {
                        System.out.println("Contato não encontrado para remoção.");
                    }
                    break;
                case "4":
                    System.out.println("Lista de contatos:");
                    for (Contato contato : lista) {
                        System.out.println(contato);
                    }
                    break;
                case "5":
                    System.out.println("Digite o nome do contato a ser alterado:");
                    String nomeAlteracao = scanner.nextLine().trim();
                    Contato contatoAlteracao = lista.pesquisar(new Contato(nomeAlteracao, ""));
                    if (contatoAlteracao != null) {
                        System.out.println("Contato encontrado: " + contatoAlteracao);
                        System.out.println("Digite o novo telefone do contato:");
                        
                        String novoTelefone = scanner.nextLine().trim();
                        
                        lista.alterar(contatoAlteracao, new Contato(contatoAlteracao.getNome(), novoTelefone));
                        
                        System.out.println("Contato atualizado: " + contatoAlteracao);
                    } else {
                        System.out.println("Contato não encontrado para alteração.");
                    }
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != null && !opcao.equals("0"));
    }

    private static void exibirMenu() {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Adicionar contato");
        System.out.println("2 - Pesquisar contato");
        System.out.println("3 - Remover contato");
        System.out.println("4 - Listar contatos");
        System.out.println("5 - Alterar contato");
        System.out.println("0 - Sair");
    }
}
