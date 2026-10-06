"""Gera os gráficos da Seção 3 a partir dos dados do doc-auxiliar.pdf.

Todos os tempos são expressos em milissegundos. Os valores originalmente
registrados em nanossegundos para o PC 2 foram convertidos conforme o próprio
documento auxiliar. As medições ausentes permanecem como NaN.
"""

from __future__ import annotations

import math
from pathlib import Path

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt


TAMANHOS = [100_000, 200_000, 300_000, 400_000]

DADOS = {
    "PC 1": {
        "leitura_montagem": {
            "ordenados": [132_040.716, 853_573.791, 1_935_898.296, 3_370_027.067],
            "balanceados": [190.251, 469.195, 784.829, 1_566.045],
        },
        "pesquisa_telefone": {
            "ordenados": [8.065, 20.381, 22.198, 26.690],
            "balanceados": [0.017, 0.030, 0.016, 0.016],
        },
        "pesquisa_nome": {
            "ordenados": [0.999, 0.856, 0.867, 1.053],
            "balanceados": [0.870, 0.732, 0.847, 1.255],
        },
        "remocao_telefone": {
            "ordenados": [12.916, 27.242, 39.395, 47.590],
            "balanceados": [0.026, 0.057, 0.052, 0.040],
        },
    },
    "PC 2": {
        "leitura_montagem": {
            "ordenados": [71_927.264, 650_416.663, 1_970_469.635, math.nan],
            "balanceados": [207.748, 515.193, 903.801, 1_606.020],
        },
        "pesquisa_telefone": {
            "ordenados": [8.493, 17.174, 26.750, math.nan],
            "balanceados": [0.019, 0.019, 0.020, 0.020],
        },
        "pesquisa_nome": {
            "ordenados": [1.410, 1.438, 1.412, math.nan],
            "balanceados": [1.218, 1.294, 1.465, 1.115],
        },
        "remocao_telefone": {
            "ordenados": [10.927, 29.566, 44.937, math.nan],
            "balanceados": [0.027, 0.032, 0.038, 0.042],
        },
    },
}

OPERACOES = {
    "leitura_montagem": {
        "titulo": "Leitura do arquivo e montagem da árvore",
        "arquivo": "grafico_leitura_montagem.png",
        "escala_log": True,
    },
    "pesquisa_telefone": {
        "titulo": "Pesquisa pelo telefone do último contato",
        "arquivo": "grafico_pesquisa_telefone.png",
        "escala_log": True,
    },
    "pesquisa_nome": {
        "titulo": "Pesquisa pelo nome do último contato",
        "arquivo": "grafico_pesquisa_nome.png",
        "escala_log": False,
    },
    "remocao_telefone": {
        "titulo": "Remoção pelo telefone do último contato",
        "arquivo": "grafico_remocao_telefone.png",
        "escala_log": True,
    },
}


def gerar_graficos(diretorio_saida: str | Path) -> dict[str, Path]:
    """Gera um gráfico por operação e retorna seus caminhos."""

    saida = Path(diretorio_saida)
    saida.mkdir(parents=True, exist_ok=True)

    plt.rcParams.update(
        {
            "font.family": "DejaVu Sans",
            "font.size": 9,
            "axes.titleweight": "bold",
            "axes.edgecolor": "#4b5563",
            "axes.labelcolor": "#1f2937",
            "xtick.color": "#374151",
            "ytick.color": "#374151",
        }
    )

    eixos_x = [valor // 1_000 for valor in TAMANHOS]
    gerados: dict[str, Path] = {}

    for chave, configuracao in OPERACOES.items():
        figura, eixos = plt.subplots(1, 2, figsize=(11, 5.7), sharey=False)
        figura.patch.set_facecolor("white")
        figura.suptitle(configuracao["titulo"], fontsize=14, fontweight="bold")

        for eixo, (pc, dados_pc) in zip(eixos, DADOS.items()):
            valores = dados_pc[chave]
            eixo.plot(
                eixos_x,
                valores["ordenados"],
                color="#1f4e78",
                marker="o",
                linewidth=2.1,
                markersize=5,
                label="Contatos ordenados",
            )
            eixo.plot(
                eixos_x,
                valores["balanceados"],
                color="#2e7d32",
                marker="s",
                linewidth=2.1,
                markersize=5,
                label="Contatos balanceados",
            )
            eixo.set_title(pc, fontsize=11)
            eixo.set_xlabel("Quantidade de contatos (mil)")
            eixo.set_ylabel("Tempo (ms)")
            eixo.set_xticks(eixos_x)
            eixo.grid(True, which="both", linestyle="--", alpha=0.35)
            eixo.legend(frameon=False, fontsize=8, loc="best")

            if configuracao["escala_log"]:
                eixo.set_yscale("log")
                eixo.set_ylabel("Tempo (ms) - escala logarítmica")

            if pc == "PC 2" and math.isnan(valores["ordenados"][-1]):
                eixo.text(
                    0.98,
                    0.96,
                    "Ordenados / 400 mil: N/D",
                    transform=eixo.transAxes,
                    ha="right",
                    va="top",
                    fontsize=7.5,
                    color="#6b7280",
                    bbox={"facecolor": "white", "edgecolor": "#d1d5db", "pad": 3},
                )

        figura.tight_layout(rect=(0, 0.02, 1, 0.94), w_pad=2.0)
        caminho = saida / configuracao["arquivo"]
        figura.savefig(caminho, dpi=220, bbox_inches="tight", facecolor="white")
        plt.close(figura)
        gerados[chave] = caminho

    return gerados


if __name__ == "__main__":
    arquivos = gerar_graficos(Path("output") / "graficos")
    for nome, caminho in arquivos.items():
        print(f"{nome}: {caminho}")
