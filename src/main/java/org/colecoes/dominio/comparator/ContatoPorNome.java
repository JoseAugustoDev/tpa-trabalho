package org.colecoes.dominio.comparator;

import java.util.Comparator;

import org.colecoes.dominio.Contato;

public class ContatoPorNome implements Comparator<Contato> {
     @Override
     public int compare(Contato c1, Contato c2) {
          return c1.getNome().compareToIgnoreCase(c2.getNome());
     }

}
