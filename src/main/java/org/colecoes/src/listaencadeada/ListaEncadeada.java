package org.colecoes.src.listaencadeada;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.colecoes.src.colecao.IColecao;

public class ListaEncadeada<T> implements IColecao<T>, Iterable<T> {
    private No<T> prim;
    private final Comparator<T> comparador;
    private final boolean ehOrdenada;

    public ListaEncadeada() {
        this(null, false);
    }

    public ListaEncadeada(Comparator<T> comparador, boolean ehOrdenada) {
        this.comparador = comparador;
        this.ehOrdenada = ehOrdenada;
    }


    @Override
    public boolean adicionar(T novoValor) {
        if (novoValor == null) return false;

        if (ehOrdenada) {
            return adicionarOrdenado(novoValor);
        }
        return adicionarNaoOrdenada(novoValor);
    }

    private boolean adicionarOrdenado(T novoValor) {
        No<T> novoNo = new No<>(novoValor);
        if (prim == null) {
            prim = novoNo;
            return true;
        }

        if (comparador.compare(novoValor, prim.getValor()) < 0) {
            novoNo.setProx(prim);
            prim = novoNo;
            return true;
        }

        No<T> atual = prim;
        while (atual.getProx() != null
                && comparador.compare(novoValor, atual.getProx().getValor()) >= 0) {
            atual = atual.getProx();
        }
        novoNo.setProx(atual.getProx());
        atual.setProx(novoNo);
        return true;
    }

    private boolean adicionarNaoOrdenada(T novoValor) {
        No<T> novoNo = new No<>(novoValor);
        if (prim == null) {
            prim = novoNo;
            return true;
        }

        No<T> ultimo = prim;
        while (ultimo.getProx() != null) ultimo = ultimo.getProx();
        ultimo.setProx(novoNo);
        return true;
    }

    @Override
    public T pesquisar(T valor) {
        if (valor == null) return null;
        No<T> atual = prim;
        while (atual != null) {
            int resultado = comparador.compare(atual.getValor(), valor);
            if (resultado == 0) return atual.getValor();
            if (ehOrdenada && resultado > 0) return null;
            atual = atual.getProx();
        }
        return null;
    }

    public void alterar(T valorAntigo, T novoValor) {
        if (valorAntigo == null || novoValor == null) return;
        No<T> atual = prim;
        while (atual != null) {
            int resultado = comparador.compare(atual.getValor(), valorAntigo);
            if (resultado == 0) {
                atual.setValor(novoValor);
                return;
            }
            if (ehOrdenada && resultado > 0) return;
            atual = atual.getProx();
        }
    }

    @Override
    public boolean remover(T valor) {
        if (valor == null || prim == null) return false;

            if (comparador.compare(prim.getValor(), valor) == 0) {
                prim = prim.getProx();
                return true;
            }

            No<T> anterior = prim;
            No<T> atual = prim.getProx();
            while (atual != null) {
                int resultado = comparador.compare(atual.getValor(), valor);
                if (resultado == 0) {
                    anterior.setProx(atual.getProx());
                    return true;
                }
                if (ehOrdenada && resultado > 0) return false;
                anterior = atual;
                atual = atual.getProx();
            }
            return false;
        }

    @Override
    public int quantidadeNos() {
        int quantidade = 0;
        for (No<T> atual = prim; atual != null; atual = atual.getProx()) {
            quantidade++;
        }
        return quantidade;
    }

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder("[");
        No<T> atual = prim;
        while (atual != null) {
            resultado.append(atual.getValor());
            if (atual.getProx() != null) resultado.append(',');
            atual = atual.getProx();
        }
        return resultado.append(']').toString();
    }

    // Override do método iterator() para permitir a iteração sobre a lista encadeada
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private No<T> atual = prim;

            @Override
            public boolean hasNext() {
                return atual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T valor = atual.getValor();
                atual = atual.getProx();
                return valor;
            }
        };
    }
}
