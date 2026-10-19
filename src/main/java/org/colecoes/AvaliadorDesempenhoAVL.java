package org.colecoes;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.colecoes.dominio.Contato;
import org.colecoes.dominio.comparator.ContatoPorNome;
import org.colecoes.dominio.comparator.ContatoPorTelefone;
import org.colecoes.dominio.comparator.ContatoSomentePorNome;
import org.colecoes.src.arvorebinaria.ArvoreAVL;

/** Executa de forma reproduzivel as medicoes solicitadas para a arvore AVL. */
public final class AvaliadorDesempenhoAVL {
    private static final Path DIRETORIO_PADRAO = Path.of(
            "src", "main", "java", "org", "colecoes", "dominio", "dados", "arvore-contatos");

    private AvaliadorDesempenhoAVL() {
    }

    public static void main(String[] args) throws IOException {
        List<Path> arquivos = obterArquivos(args);
        System.out.println("arquivo;objetos;montagem_ms;pesquisa_telefone_ms;pesquisa_nome_ms;remocao_ms;altura_telefone");

        for (Path arquivo : arquivos) {
            medir(arquivo);
        }
    }

    private static List<Path> obterArquivos(String[] args) {
        if (args.length > 0) {
            List<Path> informados = new ArrayList<>();
            for (String argumento : args) {
                informados.add(Path.of(argumento));
            }
            return informados;
        }

        return List.of(100_000, 200_000, 300_000, 400_000).stream()
                .map(tamanho -> DIRETORIO_PADRAO.resolve(
                        "contatos-ordenados-" + tamanho + ".txt"))
                .toList();
    }

    private static void medir(Path arquivo) throws IOException {
        ArvoreAVL<Contato> porNome = new ArvoreAVL<>(new ContatoPorNome());
        ArvoreAVL<Contato> porTelefone = new ArvoreAVL<>(new ContatoPorTelefone());
        Contato ultimoContato = null;
        int quantidade = 0;

        long inicioMontagem = System.nanoTime();
        try (BufferedReader leitor = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
            int quantidadeEsperada = Integer.parseInt(leitor.readLine().trim());
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] dados = linha.split(";", -1);
                Contato contato = new Contato(dados[0].trim(), dados[1].trim());
                porNome.adicionar(contato);
                porTelefone.adicionar(contato);
                ultimoContato = contato;
                quantidade++;
            }
            if (quantidade != quantidadeEsperada) {
                throw new IOException("Quantidade divergente em " + arquivo);
            }
        }
        long montagem = System.nanoTime() - inicioMontagem;

        long inicioPesquisaTelefone = System.nanoTime();
        Contato encontradoPorTelefone = porTelefone.pesquisar(
                new Contato("", ultimoContato.getTelefone()));
        long pesquisaTelefone = System.nanoTime() - inicioPesquisaTelefone;

        long inicioPesquisaNome = System.nanoTime();
        Contato encontradoPorNome = porNome.pesquisar(
                new Contato(ultimoContato.getNome(), ""), new ContatoSomentePorNome());
        long pesquisaNome = System.nanoTime() - inicioPesquisaNome;

        if (encontradoPorTelefone == null || encontradoPorNome == null) {
            throw new IllegalStateException("O ultimo contato nao foi localizado em " + arquivo);
        }

        long inicioRemocao = System.nanoTime();
        boolean removeuTelefone = porTelefone.remover(encontradoPorTelefone);
        boolean removeuNome = porNome.remover(encontradoPorTelefone);
        long remocao = System.nanoTime() - inicioRemocao;

        if (!removeuTelefone || !removeuNome) {
            throw new IllegalStateException("O ultimo contato nao foi removido em " + arquivo);
        }

        System.out.printf(
                "%s;%d;%.3f;%.6f;%.6f;%.6f;%d%n",
                arquivo.getFileName(),
                quantidade,
                montagem / 1_000_000.0,
                pesquisaTelefone / 1_000_000.0,
                pesquisaNome / 1_000_000.0,
                remocao / 1_000_000.0,
                porTelefone.altura());
    }
}
