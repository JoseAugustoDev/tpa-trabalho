package org.colecoes.src.arquivo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.colecoes.dominio.Contato;

/**
 * Gera contatos na ordem de inserção necessária para formar uma árvore
 * perfeitamente balanceada quando o telefone é usado como chave.
 */
public final class GeradorArquivosBalanceados {
    private static final int[] QUANTIDADES = {100_000, 200_000, 300_000, 400_000};
    private static final Path DIRETORIO_DADOS = Path.of(
            "src/main/java/org/colecoes/dominio/dados/arvore-contatos");

    private GeradorArquivosBalanceados() {
    }

    public static void main(String[] args) {
        try {
            for (int quantidade : obterQuantidades(args)) {
                Path arquivo = DIRETORIO_DADOS.resolve(
                        "contatos-balanceados-" + quantidade + ".txt");
                gerarArquivo(quantidade, arquivo);
                System.out.println("Arquivo balanceado gerado: " + arquivo);
            }
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Erro ao gerar arquivos balanceados: " + e.getMessage());
        }
    }

    public static List<Contato> gerarArquivo(int quantidade, Path arquivo)
            throws IOException {
        List<Contato> contatos = gerarContatosBalanceados(quantidade);
        LeitorArquivos.salvarContatos(arquivo, contatos);
        return contatos;
    }

    public static List<Contato> gerarContatosBalanceados(int quantidade) {
        List<Contato> ordenados = GeradorArquivosOrdenados
                .gerarContatosOrdenados(quantidade);
        List<Contato> balanceados = new ArrayList<>(quantidade);
        adicionarIntervaloBalanceado(
                ordenados, balanceados, 0, ordenados.size() - 1);
        return balanceados;
    }

    private static void adicionarIntervaloBalanceado(
            List<Contato> ordenados,
            List<Contato> balanceados,
            int inicio,
            int fim) {
        if (inicio > fim) {
            return;
        }

        int meio = inicio + (fim - inicio) / 2;
        balanceados.add(ordenados.get(meio));
        adicionarIntervaloBalanceado(ordenados, balanceados, inicio, meio - 1);
        adicionarIntervaloBalanceado(ordenados, balanceados, meio + 1, fim);
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
