package org.colecoes.dominio.comparator;

import java.util.Comparator;

import org.colecoes.dominio.Contato;

public class ContatoPorNome implements Comparator<Contato> {
     @Override
     public int compare(Contato c1, Contato c2) {
          int resultadoNome = c1.getNome().compareToIgnoreCase(c2.getNome());
          if (resultadoNome != 0) return resultadoNome;
          return c1.getTelefone().compareToIgnoreCase(c2.getTelefone());
     }

}
