package org.colecoes.src.arvorebinaria;

import java.util.Comparator;

public class ArvoreAVL<T> extends ArvoreBinaria<T> {

    public ArvoreAVL(Comparator<T> comparador) {
        super(comparador);
    }

    @Override
    public boolean adicionar(T novoValor) {
        if (novoValor == null) {
            return false;
        }

        boolean[] adicionado = {false};
        raiz = adicionar(raiz, novoValor, adicionado);
        return adicionado[0];
    }

    private No<T> adicionar(No<T> noAtual, T novoValor, boolean[] adicionado) {
        if (noAtual == null) {
            adicionado[0] = true;
            return new No<>(novoValor);
        }

        int resultado = comparador.compare(novoValor, noAtual.valor);
        if (resultado < 0) {
            noAtual.esquerda = adicionar(noAtual.esquerda, novoValor, adicionado);
        } else if (resultado > 0) {
            noAtual.direita = adicionar(noAtual.direita, novoValor, adicionado);
        } else {
            return noAtual;
        }

        atualizarAltura(noAtual);
        return balancear(noAtual);
    }

    @Override
    public boolean remover(T valor) {
        if (valor == null) {
            return false;
        }

        boolean[] removido = {false};
        raiz = remover(raiz, valor, removido);
        return removido[0];
    }

    private No<T> remover(No<T> noAtual, T valor, boolean[] removido) {
        if (noAtual == null) {
            return null;
        }

        int resultado = comparador.compare(valor, noAtual.valor);
        if (resultado < 0) {
            noAtual.esquerda = remover(noAtual.esquerda, valor, removido);
        } else if (resultado > 0) {
            noAtual.direita = remover(noAtual.direita, valor, removido);
        } else {
            removido[0] = true;

            if (noAtual.esquerda == null) {
                return noAtual.direita;
            }
            if (noAtual.direita == null) {
                return noAtual.esquerda;
            }

            No<T> sucessor = menorNo(noAtual.direita);
            noAtual.valor = sucessor.valor;
            noAtual.direita = removerSucessor(noAtual.direita);
        }

        atualizarAltura(noAtual);
        return balancear(noAtual);
    }

    private No<T> removerSucessor(No<T> noAtual) {
        if (noAtual.esquerda == null) {
            return noAtual.direita;
        }

        noAtual.esquerda = removerSucessor(noAtual.esquerda);
        atualizarAltura(noAtual);
        return balancear(noAtual);
    }

    private No<T> menorNo(No<T> noAtual) {
        No<T> menor = noAtual;
        while (menor.esquerda != null) {
            menor = menor.esquerda;
        }
        return menor;
    }

    private No<T> balancear(No<T> noAtual) {
        int fator = fatorBalanceamento(noAtual);

        if (fator > 1) {
            if (fatorBalanceamento(noAtual.esquerda) < 0) {
                noAtual.esquerda = rotacaoEsquerda(noAtual.esquerda);
            }
            return rotacaoDireita(noAtual);
        }

        if (fator < -1) {
            if (fatorBalanceamento(noAtual.direita) > 0) {
                noAtual.direita = rotacaoDireita(noAtual.direita);
            }
            return rotacaoEsquerda(noAtual);
        }

        return noAtual;
    }

    private No<T> rotacaoDireita(No<T> noDesbalanceado) {
        No<T> novaRaiz = noDesbalanceado.esquerda;
        No<T> subarvoreTransferida = novaRaiz.direita;

        novaRaiz.direita = noDesbalanceado;
        noDesbalanceado.esquerda = subarvoreTransferida;

        atualizarAltura(noDesbalanceado);
        atualizarAltura(novaRaiz);
        return novaRaiz;
    }

    private No<T> rotacaoEsquerda(No<T> noDesbalanceado) {
        No<T> novaRaiz = noDesbalanceado.direita;
        No<T> subarvoreTransferida = novaRaiz.esquerda;

        novaRaiz.esquerda = noDesbalanceado;
        noDesbalanceado.direita = subarvoreTransferida;

        atualizarAltura(noDesbalanceado);
        atualizarAltura(novaRaiz);
        return novaRaiz;
    }

    private int fatorBalanceamento(No<T> noAtual) {
        if (noAtual == null) {
            return 0;
        }
        return alturaArmazenada(noAtual.esquerda) - alturaArmazenada(noAtual.direita);
    }

    private void atualizarAltura(No<T> noAtual) {
        noAtual.altura = 1 + Math.max(
                alturaArmazenada(noAtual.esquerda),
                alturaArmazenada(noAtual.direita));
    }

    private int alturaArmazenada(No<T> noAtual) {
        return noAtual == null ? -1 : noAtual.altura;
    }
}
