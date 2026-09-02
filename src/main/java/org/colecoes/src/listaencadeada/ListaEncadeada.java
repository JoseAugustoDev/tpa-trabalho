package org.colecoes.src.listaencadeada;


import org.colecoes.src.colecao.IColecao;

import java.util.Comparator;

public class ListaEncadeada<T> implements IColecao<T> {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionar'");
    }

    @Override
    public T pesquisar(T valor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pesquisar'");
    }

    @Override
    public boolean remover(T valor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remover'");
    }

    @Override
    public int quantidadeNos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'quantidadeNos'");
    }


}