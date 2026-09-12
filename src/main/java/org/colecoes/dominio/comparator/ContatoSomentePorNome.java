package org.colecoes.dominio.comparator;

import java.util.Comparator;

import org.colecoes.dominio.Contato;

public class ContatoSomentePorNome implements Comparator<Contato> {
    @Override
    public int compare(Contato contato1, Contato contato2) {
        return contato1.getNome().compareToIgnoreCase(contato2.getNome());
    }
}
