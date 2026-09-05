package org.colecoes.dominio;

public class Contato {
    private final String nome;
    private final String telefone;

    public Contato(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() { return nome; }

    public String getTelefone() { return telefone; }

    public String setNome(String nome) { return nome; }
    
    public String setTelefone(String telefone) { return telefone; }

    @Override
    public String toString() { return nome + "-" + telefone; }
}
