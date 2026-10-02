package org.colecoes.src.arquivo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.colecoes.dominio.Contato;

/**
 * Gera contatos em ordem crescente de telefone. Ao serem inseridos em uma
 * árvore indexada por telefone, os registros formam uma árvore degenerada.
 */
public final class GeradorArquivosOrdenados {
    private static final int[] QUANTIDADES = {100_000, 200_000, 300_000, 400_000};
    private static final Path DIRETORIO_DADOS = Path.of(
            "src/main/java/org/colecoes/dominio/dados/arvore-contatos");
    private static final long SEMENTE = 2026L;

    private GeradorArquivosOrdenados() {
    }

    public static void main(String[] args) {
        try {
            for (int quantidade : obterQuantidades(args)) {
                Path arquivo = DIRETORIO_DADOS.resolve(
                        "contatos-ordenados-" + quantidade + ".txt");
                gerarArquivo(quantidade, arquivo);
                System.out.println("Arquivo ordenado gerado: " + arquivo);
            }
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Erro ao gerar arquivos ordenados: " + e.getMessage());
        }
    }

    public static List<Contato> gerarArquivo(int quantidade, Path arquivo)
            throws IOException {
        List<Contato> contatos = gerarContatosOrdenados(quantidade);
        LeitorArquivos.salvarContatos(arquivo, contatos);
        return contatos;
    }

    public static List<Contato> gerarContatosOrdenados(int quantidade) {
        return GeradorArquivos.gerarContatosEmOrdemCrescente(
                quantidade, SEMENTE + quantidade);
    }

    private static int[] obterQuantidades(String[] args) {
        if (args.length == 0) {
            return QUANTIDADES;
        }

        int[] quantidades = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            try {
                quantidades[i] = Integer.parseInt(args[i]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Quantidade inválida: " + args[i], e);
            }
        }
        return quantidades;
    }
}
