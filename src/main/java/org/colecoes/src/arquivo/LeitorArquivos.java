package org.colecoes.src.arquivo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.colecoes.dominio.Contato;

public final class LeitorArquivos {
    public static final Path ARQUIVO_CONTATOS = Path.of("contatos.txt");

    private LeitorArquivos() {
    }

    public static List<Contato> lerContatos() throws IOException {
        if (!Files.exists(ARQUIVO_CONTATOS)) return new ArrayList<>();

        List<Contato> contatos = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(ARQUIVO_CONTATOS, StandardCharsets.UTF_8)) {
            String primeiraLinha = reader.readLine();
            if (primeiraLinha == null) return contatos;

            int quantidadeEsperada;
            try {
                quantidadeEsperada = Integer.parseInt(primeiraLinha.trim());
            } catch (NumberFormatException e) {
                throw new IOException("Cabecalho invalido em " + ARQUIVO_CONTATOS, e);
            }

            String linha;
            int numeroLinha = 1;
            while ((linha = reader.readLine()) != null) {
                numeroLinha++;
                if (linha.isBlank()) continue;
                String[] partes = linha.split(";", -1);
                if (partes.length != 2 || partes[0].isBlank() || partes[1].isBlank()) {
                    throw new IOException("Contato invalido na linha " + numeroLinha);
                }
                contatos.add(new Contato(partes[0], partes[1]));
            }

            if (contatos.size() != quantidadeEsperada) {
                throw new IOException("O cabecalho informa " + quantidadeEsperada
                        + " contatos, mas foram lidos " + contatos.size());
            }
        }
        validarContatos(contatos);
        return contatos;
    }

    private static void validarContatos(List<Contato> contatos) throws IOException {
        Set<String> nomes = new HashSet<>();
        Set<String> telefones = new HashSet<>();
        String nomeAnterior = null;
        for (Contato contato : contatos) {
            String nome = contato.getNome().toLowerCase(Locale.ROOT);
            if (!nomes.add(nome)) throw new IOException("Nome duplicado: " + contato.getNome());
            if (!telefones.add(contato.getTelefone())) {
                throw new IOException("Telefone duplicado: " + contato.getTelefone());
            }
            if (nomeAnterior != null && nomeAnterior.compareTo(nome) > 0) {
                throw new IOException("O arquivo de contatos nao esta ordenado por nome.");
            }
            nomeAnterior = nome;
        }
    }

    public static void salvarContatos(Iterable<Contato> contatos) throws IOException {
        List<Contato> ordenados = new ArrayList<>();
        contatos.forEach(ordenados::add);
        ordenados.sort(Comparator.comparing(Contato::getNome, String.CASE_INSENSITIVE_ORDER));

        Path temporario = ARQUIVO_CONTATOS.resolveSibling(ARQUIVO_CONTATOS.getFileName() + ".tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(temporario, StandardCharsets.UTF_8)) {
            writer.write(Integer.toString(ordenados.size()));
            writer.newLine();
            for (Contato contato : ordenados) {
                writer.write(contato.getNome());
                writer.write(';');
                writer.write(contato.getTelefone());
                writer.newLine();
            }
        }

        try {
            Files.move(temporario, ARQUIVO_CONTATOS, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporario, ARQUIVO_CONTATOS, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
