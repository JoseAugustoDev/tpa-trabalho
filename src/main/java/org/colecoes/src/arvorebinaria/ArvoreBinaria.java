package org.colecoes.src.arvorebinaria;

import java.util.Comparator;
import java.util.Objects;

public class ArvoreBinaria<T> extends ArvoreBinariaBase<T> {
    private No<T> raiz;

    public ArvoreBinaria(Comparator<T> comparador) {
        super(Objects.requireNonNull(comparador, "O comparador não pode ser nulo."));
    }

    @Override
    public boolean adicionar(T novoValor) {
        if (novoValor == null) {
            return false;
        }

        if (raiz == null) {
            raiz = new No<>(novoValor);
            return true;
        }

        return adicionar(raiz, novoValor);
    }

    private boolean adicionar(No<T> noAtual, T novoValor) {
        int resultado = comparador.compare(novoValor, noAtual.valor);
        if (resultado == 0) {
            return false;
        }

        if (resultado < 0) {
            if (noAtual.esquerda == null) {
                noAtual.esquerda = new No<>(novoValor);
                return true;
            }
            return adicionar(noAtual.esquerda, novoValor);
        }

        if (noAtual.direita == null) {
            noAtual.direita = new No<>(novoValor);
            return true;
        }
        return adicionar(noAtual.direita, novoValor);
    }

    @Override
    public T pesquisar(T valor) {
        return pesquisar(valor, comparador);
    }

    public T pesquisar(T valor, Comparator<T> comparadorPesquisa) {
        if (valor == null || comparadorPesquisa == null) {
            return null;
        }
        return pesquisar(raiz, valor, comparadorPesquisa);
    }

    private T pesquisar(No<T> noAtual, T valor, Comparator<T> comparadorPesquisa) {
        if (noAtual == null) {
            return null;
        }

        int resultado = comparadorPesquisa.compare(valor, noAtual.valor);
        if (resultado == 0) {
            return noAtual.valor;
        }

        if (resultado < 0) {
            return pesquisar(noAtual.esquerda, valor, comparadorPesquisa);
        }
        return pesquisar(noAtual.direita, valor, comparadorPesquisa);
    }

    @Override
    public boolean remover(T valor) {
        if (valor == null || pesquisar(valor) == null) {
            return false;
        }

        raiz = remover(raiz, valor);
        return true;
    }

    private No<T> remover(No<T> noAtual, T valor) {
        int resultado = comparador.compare(valor, noAtual.valor);

        if (resultado < 0) {
            noAtual.esquerda = remover(noAtual.esquerda, valor);
        } else if (resultado > 0) {
            noAtual.direita = remover(noAtual.direita, valor);
        } else {
            if (noAtual.esquerda == null) {
                return noAtual.direita;
            }
            if (noAtual.direita == null) {
                return noAtual.esquerda;
            }

            No<T> sucessor = menorNo(noAtual.direita);
            noAtual.valor = sucessor.valor;
            noAtual.direita = remover(noAtual.direita, sucessor.valor);
        }

        return noAtual;
    }

    private No<T> menorNo(No<T> noAtual) {
        if (noAtual.esquerda == null) {
            return noAtual;
        }
        return menorNo(noAtual.esquerda);
    }

    @Override
    public int quantidadeNos() {
        return quantidadeNos(raiz);
    }

    private int quantidadeNos(No<T> noAtual) {
        if (noAtual == null) {
            return 0;
        }
        return 1 + quantidadeNos(noAtual.esquerda) + quantidadeNos(noAtual.direita);
    }

    @Override
    public int altura() {
        return altura(raiz);
    }

    private int altura(No<T> noAtual) {
        if (noAtual == null) {
            return -1;
        }
        return 1 + Math.max(
                altura(noAtual.esquerda),
                altura(noAtual.direita));
    }

    @Override
    public String caminharEmNivel() {
        if (raiz == null) {
            return "[]";
        }

        StringBuilder resultado = new StringBuilder("[");
        int altura = altura();
        caminharNiveis(0, altura, resultado);
        return resultado.append(']').toString();
    }

    private void caminharNiveis(
            int nivel,
            int altura,
            StringBuilder resultado) {
        if (nivel > altura) {
            return;
        }

        caminharEmNivel(raiz, nivel, resultado);
        resultado.setLength(resultado.length() - 2);
        if (nivel < altura) {
            resultado.append(System.lineSeparator());
        }
        caminharNiveis(nivel + 1, altura, resultado);
    }

    private void caminharEmNivel(
            No<T> noAtual,
            int nivel,
            StringBuilder resultado) {
        if (noAtual == null) {
            return;
        }

        if (nivel == 0) {
            resultado.append(noAtual.valor).append(", ");
            return;
        }

        caminharEmNivel(noAtual.esquerda, nivel - 1, resultado);
        caminharEmNivel(noAtual.direita, nivel - 1, resultado);
    }

    @Override
    public String caminharEmOrdem() {
        StringBuilder resultado = new StringBuilder("[");
        caminharEmOrdem(raiz, resultado);

        if (resultado.length() > 1) {
            resultado.setLength(resultado.length() - 2);
        }
        return resultado.append(']').toString();
    }

    private void caminharEmOrdem(No<T> noAtual, StringBuilder resultado) {
        if (noAtual == null) {
            return;
        }

        caminharEmOrdem(noAtual.esquerda, resultado);
        resultado.append(noAtual.valor).append(", ");
        caminharEmOrdem(noAtual.direita, resultado);
    }

    private static final class No<T> {
        private T valor;
        private No<T> esquerda;
        private No<T> direita;

        private No(T valor) {
            this.valor = valor;
        }
    }
}
