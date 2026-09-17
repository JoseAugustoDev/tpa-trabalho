import matplotlib.pyplot as plt

# ---------------------------------------------------------------------------
# Dados extraídos das tabelas (todos os tempos já convertidos para ms)
# ---------------------------------------------------------------------------

tamanhos = [100000, 200000, 300000, 400000]

dados = {
    "PC1 (Jose)": {
        "Leitura e Montagem da Lista": {
            "ordenada": [92404.833, 687298.685, 2161261.862, 4250734.401],
            "nao_ordenada": [28259.900, 107475.418, 231528.180, 566921.378],
        },
        "Pesquisar pelo telefone (último)": {
            "ordenada": [17.369, 19.768, 31.426, 41.572],
            "nao_ordenada": [8.151, 10.144, 14.682, 14.908],
        },
        "Pesquisar pelo nome (último)": {
            "ordenada": [14.199, 11.509, 20.213, 35.024],
            "nao_ordenada": [1.333, 1.039, 2.234, 1.679],
        },
        "Remover pelo telefone (último)": {
            "ordenada": [18.711, 39.200, 58.070, 64.008],
            "nao_ordenada": [10.272, 10.475, 21.512, 40.000],
        },
    },
    "PC2 (Leticia)": {
        "Leitura e Montagem da Lista": {
            "ordenada": [77381.263, 677503.294, 2460263.607, 5198727.219],
            "nao_ordenada": [21842.618, 96849.378, 396520.695, 861217.557],
        },
        "Pesquisar pelo telefone (último)": {
            "ordenada": [13.143, 17.312, 35.877, 53.550],
            "nao_ordenada": [10.109, 15.792, 16.077, 18.855],
        },
        "Pesquisar pelo nome (último)": {
            "ordenada": [5.471, 8.593, 40.366, 44.286],
            "nao_ordenada": [2.467, 1.574, 3.484, 3.158],
        },
        "Remover pelo telefone (último)": {
            "ordenada": [14.138, 23.549, 79.345, 72.439],
            "nao_ordenada": [8.577, 15.992, 12.564, 22.330],
        },
    },
}

operacoes = list(dados["PC1 (Jose)"].keys())

# ---------------------------------------------------------------------------
# Gera um gráfico por operação, com um subplot para cada PC
# (ordenada x não-ordenada em função do tamanho da lista)
# ---------------------------------------------------------------------------

for operacao in operacoes:
    fig, axs = plt.subplots(1, 2, figsize=(12, 5))
    fig.suptitle(f"{operacao} — Ordenada x Não-ordenada", fontsize=13)

    for ax, pc in zip(axs, dados.keys()):
        valores = dados[pc][operacao]
        ax.plot(tamanhos, valores["ordenada"], marker="o", label="Lista ordenada")
        ax.plot(tamanhos, valores["nao_ordenada"], marker="o", label="Lista não ordenada")
        ax.set_title(pc)
        ax.set_xlabel("Quantidade de contatos")
        ax.set_ylabel("Tempo (ms)")
        ax.set_xticks(tamanhos)
        ax.grid(True, linestyle="--", alpha=0.5)
        ax.legend()

    fig.tight_layout(rect=[0, 0, 1, 0.94])
    nome_arquivo = "grafico_" + operacao.lower()
    for ch, sub in [(" ", "_"), ("(", ""), (")", ""), ("á", "a"), ("ó", "o"),
                    ("ú", "u"), ("é", "e"), ("ê", "e"), ("ã", "a"), ("ç", "c")]:
        nome_arquivo = nome_arquivo.replace(ch, sub)
    fig.savefig(f"{nome_arquivo}.png", dpi=150)
    plt.close(fig)

# ---------------------------------------------------------------------------
# Gráfico extra: Leitura e Montagem da Lista em escala logarítmica
# (os tempos crescem muito e "escondem" as diferenças em escala linear)
# ---------------------------------------------------------------------------

fig, axs = plt.subplots(1, 2, figsize=(12, 5))
fig.suptitle("Leitura e Montagem da Lista — Escala Logarítmica", fontsize=13)

for ax, pc in zip(axs, dados.keys()):
    valores = dados[pc]["Leitura e Montagem da Lista"]
    ax.plot(tamanhos, valores["ordenada"], marker="o", label="Lista ordenada")
    ax.plot(tamanhos, valores["nao_ordenada"], marker="o", label="Lista não ordenada")
    ax.set_title(pc)
    ax.set_xlabel("Quantidade de contatos")
    ax.set_ylabel("Tempo (ms) — escala log")
    ax.set_yscale("log")
    ax.set_xticks(tamanhos)
    ax.grid(True, which="both", linestyle="--", alpha=0.5)
    ax.legend()

fig.tight_layout(rect=[0, 0, 1, 0.94])
fig.savefig("grafico_leitura_montagem_log.png", dpi=150)
plt.close(fig)

print("Gráficos gerados com sucesso na pasta atual.")
