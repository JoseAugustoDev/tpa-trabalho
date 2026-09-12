package org.colecoes.src.arquivo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.colecoes.dominio.Contato;

public final class GeradorArquivosOrdenados {
    private static final int[] QUANTIDADES = {100_000, 200_000, 300_000, 400_000};
    private static final Path DIRETORIO_DADOS =
            Path.of("src/main/java/org/colecoes/dominio/dados");
    private static final long SEMENTE = 2026L;
    private static final String[] PRENOMES = {
            "Ana", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda", "Gabriel", "Helena", "Isabela", "João",
            "Juliana", "Lucas", "Mariana", "Nathan", "Olivia", "Paulo", "Quésia", "Rafael", "Sofia", "Thiago",
            "Victor", "William", "Xavier", "Yasmin", "Zuleica", "Alfredo", "Beatriz", "Caio", "Denise", "Eliana",
            "Felipe", "Gustavo", "Heitor", "Igor", "Jéssica", "Kevin", "Larissa", "Mateus", "Natália", "Otávio",
            "Patrícia", "Renato", "Sandra", "Tadeu", "Ursula", "Vinícius", "Wellington", "Zilda", "Adriana", "Benício",
            "Cristina", "Davi", "Emanuel", "Flávia", "Geraldo", "Heloísa", "Ícaro", "Jaqueline", "Leonardo", "Marta",
            "Nelson", "Orlando", "Priscila", "Raquel", "Saulo", "Tatiane", "Ubirajara", "Vera", "Wesley", "Zenaide",
            "Alice", "Brenda", "Caetano", "Danilo", "Enzo", "Fabiana", "Gilberto", "Henrique", "Isadora", "José",
            "Kátia", "Lorena", "Maurício", "Natanael", "Osvaldo", "Pamela", "Regina", "Sandro", "Tânia", "Ulisses",
            "Vânia", "Wilson", "Yago", "Zélia", "Amélia", "Bernardo", "Celso", "Dulce", "Edson", "Fátima", "Gilmar",
            "Humberto", "Irene", "Jorge", "Kleber", "Luciana", "Marcelo", "Nadir", "Otacílio", "Paula", "Renata"
    };
    
    private static final String[] SOBRENOMES = {
            "Almeida", "Barbosa", "Campos", "Dias", "Evangelista", "Ferreira", "Gomes", "Henrique", "Iglesias",
            "Junqueira",
            "Klein", "Lima", "Medeiros", "Nascimento", "Oliveira", "Pereira", "Queiroz", "Rodrigues", "Silva",
            "Teixeira",
            "Uchoa", "Vasconcelos", "Watanabe", "Ximenes", "Yamamoto", "Zanetti", "Araújo", "Borges", "Coelho",
            "Dantas",
            "Esteves", "Farias", "Guimarães", "Holanda", "Ivo", "Jardim", "Krieger", "Lacerda", "Monteiro", "Neves",
            "Oliveira", "Porto", "Quintana", "Ramos", "Sanches", "Torrico", "Urbano", "Vieira", "Wanderley", "Xavier",
            "Yunes", "Zampieri", "Abreu", "Barreto", "Coutinho", "Delgado", "Elias", "França", "Godoy", "Haddad",
            "Ibrahim", "Jacob", "Lopes", "Moura", "Nogueira", "Ortega", "Pinto", "Quaresma", "Reis", "Souto",
            "Torres", "Ubaldo", "Valente", "Weber", "Ximenes", "Yamaguchi", "Zanella", "Alvarenga", "Bittencourt",
            "Carvalho",
            "Duarte", "Espíndola", "Freitas", "Gonçalves", "Herrera", "Ishikawa", "Junqueira", "Lacerda", "Mancini",
            "Noronha",
            "Orsini", "Paz", "Quevedo", "Rangel", "Souza", "Tavares", "Uchoa", "Vilela", "Werneck", "Xisto"
    };

    private GeradorArquivosOrdenados() {
    }

    public static void main(String[] args) {
        try {
            for (int quantidade : QUANTIDADES) {
                Path arquivo = DIRETORIO_DADOS.resolve("contatos-" + quantidade + ".txt");
                gerarArquivo(quantidade, arquivo, new Random(SEMENTE + quantidade));
                System.out.println("Arquivo " + arquivo + " gerado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao gerar arquivos: " + e.getMessage());
        }
    }

    public static List<Contato> gerarArquivo(int quantidade) throws IOException {
        return gerarArquivo(quantidade, LeitorArquivos.ARQUIVO_CONTATOS, new Random());
    }

    public static List<Contato> gerarArquivo(int quantidade, Path arquivo) throws IOException {
        return gerarArquivo(quantidade, arquivo, new Random());
    }

    private static List<Contato> gerarArquivo(
            int quantidade, Path arquivo, Random random) throws IOException {
        List<Contato> contatos = gerarContatos(quantidade, random);
        LeitorArquivos.salvarContatos(arquivo, contatos);
        return contatos;
    }

    public static List<Contato> gerarContatos(int quantidade) {
        return gerarContatos(quantidade, new Random());
    }

    private static List<Contato> gerarContatos(int quantidade, Random random) {
        if (quantidade <= 0 || quantidade > 99_999_999) {
            throw new IllegalArgumentException(
                    "A quantidade deve estar entre 1 e 99999999.");
        }

        List<Contato> contatos = new ArrayList<>(quantidade);
        for (int i = 1; i <= quantidade; i++) {
            String nome = PRENOMES[random.nextInt(PRENOMES.length)] + " "
                    + SOBRENOMES[random.nextInt(SOBRENOMES.length)];
            String telefone = String.format("(99) 9%04d-%04d", i / 10_000, i % 10_000);
            contatos.add(new Contato(nome, telefone));
        }
        Collections.shuffle(contatos, random);
        return contatos;
    }
}
