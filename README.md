# Trabalho 1 de TPA — Lista Encadeada Genérica

Alunos: Jose Augusto e Letícia Comissário.

## Requisitos

- JDK 26;
- Apache Maven 3.9 ou superior;
- terminal aberto na raiz deste repositório.


## Organização do código

Pacotes {
  dominio: Contem a entidade contato; 
  dominio/comparator: realiza as comparações de elementos por nome e/ou telefone;
  dominio/dados: contem os arquivos txt de entrada, com diversos tamanhos.;
  src/arquivo: Classes LeitorArquivos e GeradorArquivos, utilizadas para gerar os dados de contatos de forma randomizada e permitir ler e extrair o conteudo do txt;
  src/colecao: Interface fornecida pelo professor, na qual tivemos que seguir;
  src/listaencadeada: Encontra-se a implementação da ListaEncadeada e do Nó. 
}

## Formato dos arquivos

A primeira linha informa a quantidade de contatos e as demais seguem o formato `nome;telefone`:

```text
3
Ana Almeida;(99) 90000-0001
Bruno Lima;(99) 90000-0002
Carla Souza;(99) 90000-0003
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