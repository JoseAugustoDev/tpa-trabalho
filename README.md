# Trabalho 2 de TPA — Lista e Arvore

Alunos: Jose Augusto e Letícia Comissário.

## Requisitos

- JDK 26;
- Apache Maven 3.9 ou superior;
- terminal aberto na raiz deste repositório.


## Organização do código

- `dominio`: entidade `Contato`;
- `dominio/comparator`: critérios de comparação por nome e por telefone;
- `dominio/dados`: arquivos de entrada com diversos tamanhos;
- `src/arquivo`: leitura e geração de arquivos de contatos;
- `src/colecao`: interface `IColecao` fornecida pelo professor;
- `src/listaencadeada`: implementação da lista encadeada genérica;
- `src/arvorebinaria`: classe base fornecida e implementação da árvore binária
  de busca genérica.

A árvore recebe um `Comparator<T>` no construtor. Assim, o aplicativo mantém
duas árvores do mesmo tipo `Contato`: uma indexada por nome e outra por
telefone.

## Formato dos arquivos

O programa carrega os dados exclusivamente do arquivo `entrada.txt`, localizado na raiz do projeto. Os demais arquivos desse diretório
podem ser usados como base para criar esse arquivo de entrada.

A primeira linha informa a quantidade de contatos e as demais seguem o formato `nome;telefone`:

```text
3
Ana Almeida;(99) 90000-0001
Bruno Lima;(99) 90000-0002
Carla Souza;(99) 90000-0003
```

## Geradores para os testes da árvore

Os geradores criam, por padrão, arquivos com 100.000, 200.000, 300.000 e
400.000 contatos no diretório `dominio/dados/arvore-contatos`.

- `GeradorArquivosOrdenados`: grava os telefones em ordem crescente. A
  inserção em uma árvore indexada por telefone produz uma árvore degenerada;
- `GeradorArquivosBalanceados`: grava primeiro o elemento central do intervalo
  e repete o processo nas metades esquerda e direita. A inserção produz uma
  árvore de altura mínima.
```

## Como compilar e executar

Na raiz do projeto, compile com:

```bash
mvn clean compile
```

Depois, inicie a aplicação:

```bash
java -cp target/classes org.colecoes.Main
```

Ao iniciar, escolha entre lista não ordenada, lista ordenada ou árvore binária.
Antes de selecionar a opção de carregamento no menu, coloque o arquivo desejado na raíz do projeto com o nome `entrada.txt`.
