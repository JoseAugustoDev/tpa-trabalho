"""Gera gráficos comparativos dos experimentos realizados."""

from pathlib import Path

import matplotlib.pyplot as plt
from matplotlib.ticker import FuncFormatter


TAMANHOS = [100_000, 200_000, 300_000, 400_000]

RESULTADOS = {
    "Leitura e montagem da lista": {
        "Ordenada": [92_404.833, 687_298.685, 2_161_261.862, 4_250_734.401],
        "Não ordenada": [28_259.900, 107_475.418, 231_528.180, 566_921.378],
    },
    "Pesquisa pelo telefone": {
        "Ordenada": [17.369, 19.768, 31.426, 41.572],
        "Não ordenada": [8.151, 10.144, 14.682, 14.908],
    },
    "Pesquisa pelo nome": {
        "Ordenada": [14.199, 11.509, 20.213, 35.024],
        "Não ordenada": [1.333, 1.039, 2.234, 1.679],
    },
    "Remoção pelo telefone": {
        "Ordenada": [18.711, 39.200, 58.070, 64.008],
        "Não ordenada": [10.272, 10.475, 21.512, 40.000],
    },
}


def formatar_inteiro_br(valor, _posicao):
    """Formata os valores do eixo usando ponto como separador de milhar."""
    return f"{valor:,.0f}".replace(",", ".")


def criar_graficos():
    figura, eixos = plt.subplots(2, 2, figsize=(13, 9), sharex=True)
    eixos = eixos.flatten()

    estilos = {
        "Ordenada": {"color": "#d62728", "marker": "o"},
        "Não ordenada": {"color": "#1f77b4", "marker": "s"},
    }

    for eixo, (operacao, series) in zip(eixos, RESULTADOS.items()):
        for tipo_lista, tempos in series.items():
            eixo.plot(
                TAMANHOS,
                tempos,
                linewidth=2,
                markersize=6,
                label=tipo_lista,
                **estilos[tipo_lista],
            )

        eixo.set_title(operacao)
        eixo.set_xlabel("Quantidade de contatos")
        eixo.set_ylabel("Tempo (ms)")
        eixo.set_xticks(TAMANHOS)
        eixo.xaxis.set_major_formatter(FuncFormatter(formatar_inteiro_br))
        eixo.yaxis.set_major_formatter(FuncFormatter(formatar_inteiro_br))
        eixo.grid(True, linestyle="--", alpha=0.35)
        eixo.legend()

    figura.suptitle(
        "Desempenho da Lista Encadeada",
        fontsize=16,
    )
    figura.tight_layout(rect=(0, 0, 1, 0.96))

    arquivo_saida = Path(__file__).with_name("graficos_resultados_pc1.png")
    figura.savefig(arquivo_saida, dpi=300, bbox_inches="tight")
    print(f"Gráfico salvo em: {arquivo_saida.resolve()}")
    plt.show()


if __name__ == "__main__":
    criar_graficos()
