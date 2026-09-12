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
import java.util.HashSet;
import java.util.List;
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
        Set<String> telefones = new HashSet<>();
        for (Contato contato : contatos) {
            if (!telefones.add(contato.getTelefone())) {
                throw new IOException("Telefone duplicado: " + contato.getTelefone());
            }
        }
    }

    public static void salvarContatos(Iterable<Contato> contatos) throws IOException {
        salvarContatos(ARQUIVO_CONTATOS, contatos);
    }

    public static void salvarContatos(Path arquivo, Iterable<Contato> contatos) throws IOException {
        List<Contato> listaContatos = new ArrayList<>();
        contatos.forEach(listaContatos::add);

        Path arquivoAbsoluto = arquivo.toAbsolutePath();
        Path diretorio = arquivoAbsoluto.getParent();
        if (diretorio != null) Files.createDirectories(diretorio);

        Path temporario = arquivoAbsoluto.resolveSibling(arquivoAbsoluto.getFileName() + ".tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(temporario, StandardCharsets.UTF_8)) {
            writer.write(Integer.toString(listaContatos.size()));
            writer.newLine();
            for (Contato contato : listaContatos) {
                writer.write(contato.getNome());
                writer.write(';');
                writer.write(contato.getTelefone());
                writer.newLine();
            }
        }

        try {
            Files.move(temporario, arquivoAbsoluto, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporario, arquivoAbsoluto, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
