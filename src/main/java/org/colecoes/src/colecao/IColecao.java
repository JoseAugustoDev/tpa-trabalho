package org.colecoes.src.colecao;


public interface IColecao<T> {

    /**
     * Método para adicionar um elemento à estrutura.
     * @param novoValor - Elemento do Tipo T a ser armazenado na estrutura.
     * @return true caso o elemento tenha sido adicionado. Caso contrário retorna false.
     *
     */
    public boolean adicionar(T novoValor);


    /**
     * Método para pesquisar por um elemento na estrutura e retorná-lo.
     * @param valor - será utilizado para passar o valor da chave a ser buscada. Por exemplo, se for um estrutura de Alunos indexada por nome, deve-se passar um objeto do tipo aluno com o nome que se deseja buscar.
     * @return caso tenha sido encontrado um elemento com o valor buscado, o mesmo será retornado. Caso contrário retorna null.
     */
    public T pesquisar(T valor);

    /**
     * Método que busca por um elemento na estrutura e, caso encontre, o remove da estrutura e o retorna
     * @param valor - será utilizado para passar o valor da chave a ser buscada. Por exemplo, se for um estrutura de Alunos indexada por nome, deve-se passar um objeto do tipo aluno com o nome que se deseja buscar.
     * @return caso tenha sido encontrado um elemento com o valor buscado, o elemento será removido da estrutura e retorna true. Caso contrário retorna false.
     */
    public boolean remover(T valor);


    /**
     * Método que retorna a quantidade de nós da estrutura
     * @return Retorna a quantidade de nós da estrutura
     */
    public int quantidadeNos();


}
