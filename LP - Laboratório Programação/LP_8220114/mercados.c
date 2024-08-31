/*
 * @file: mercados.c
 * @author: Sónia Oliveira
 * @date: 11/2/2023
 * @description: ficheiro com implementações de funções relacionadas com mercados
 */

#include "geral.h"
#include "input.h"
#include "mercados.h"
#include "vendedor.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void carregarMercados(Mercados *mercados, char *ficheiro) {
    int i, sucesso = 0;

    FILE *apontadorFicheiro = fopen(ficheiro, "rb");
    if (apontadorFicheiro != NULL) {

        fread(&mercados->contador, sizeof (int), 1, apontadorFicheiro);

        if (mercados->contador > 0) {

            mercados->capacidade = mercados->contador;
            mercados->arrayMercados = (Mercado*) malloc(mercados->contador * sizeof (Mercado));

            for (i = 0; i < mercados->contador; i++) {
                fread(&mercados->arrayMercados[i], sizeof (Mercado), 1, apontadorFicheiro);

            }
            sucesso = 1;
        }
        fclose(apontadorFicheiro);
    }

    if (sucesso != 1) {
        apontadorFicheiro = fopen(ficheiro, "wb");

        if (apontadorFicheiro != NULL) {
            mercados->arrayMercados = (Mercado*) malloc(TAM_INICIAL_MERCADOS * sizeof (Mercado));
            mercados->contador = 0;
            mercados->capacidade = TAM_INICIAL_MERCADOS;

            fclose(apontadorFicheiro);
        }
    }
}

void imprimirMercado(Mercado mercado) {
    printf("Nome: %s\nCodigo: %s\n", mercado.nomeMercado, mercado.codigoMercado);
     if (mercado.estadoMercado == 0) {
        printf("Estado do Mercado: ATIVO\n");
    } else {
        printf("Estado do Mercado: INATIVO\n");
    };
}

int procurarMercado(Mercados *mercados, char* cod) {
    int i;
    for (i = 0; i < mercados->contador; i++) {
        if (strcmp(mercados->arrayMercados[i].codigoMercado, cod) == 0) {
            return i;
        }
    }
    return -1;
}



void expandirMercados(Mercados *mercados) {
    int tam = (mercados->capacidade) == 0 ? TAM_INICIAL_MERCADOS : mercados->capacidade * 2;
    Mercado *temp = (Mercado*) realloc(mercados->arrayMercados, sizeof (Mercado)*(tam));
    if (temp != NULL) {
        mercados->capacidade = tam;
        mercados->arrayMercados = temp;
    }
}

void criarMercado(Mercados *mercados) {

    lerString(mercados->arrayMercados[mercados->contador].codigoMercado, MAX_COD_MERCADO, "Insira o codigo do mercado (três letras):\n");

    if (procurarMercado(mercados, mercados->arrayMercados[mercados->contador].codigoMercado) == -1) {

        lerString(mercados->arrayMercados[mercados->contador].nomeMercado, MAX_NOME_MERC, "Insira o nome do mercado:\n");

        mercados->arrayMercados->estadoMercado = INATIVO;

        mercados->contador++;

    } else {
        criarMercado(mercados);
    }

}

void inserirMercado(Mercados *mercados) {
    if (mercados->contador == mercados->capacidade) {
        expandirMercados(mercados);
    }

    if (mercados->contador < mercados->capacidade) {
        criarMercado(mercados);
    }

}

void listarMercados(Mercados mercados) {
    int i;

    if (mercados.contador > 0) {
        for (i = 0; i < mercados.contador; i++) {
            imprimirMercado(mercados.arrayMercados[i]);
        }
    } else {
        puts("Nao esistem mercados a listar.\n");
    }
}

void removerMercado(Mercados *mercados, Vendedores *vendedores) {
    
    char codMercado[MAX_COD_MERCADO];
    lerString(codMercado, MAX_COD_MERCADO, "Insira o codigo do mercado:\n");

    int mercado = procurarMercado(mercados, codMercado);

    if (mercado != -1) {
        
        int numVendedores = verificarVendedoresDeMercado(*vendedores, codMercado);
        if (numVendedores > 0) {
            puts("Nao e possivel remover o mercado, pois existem vendedores associados a ele.");
            return;
        }
        
        for (int i = mercado; i < mercados->contador - 1; i++) {
            mercados->arrayMercados[i] = mercados->arrayMercados[i + 1];
        }
        mercados->contador--;
        puts("Mercado removido com sucesso.");
    } else {
        puts("Mercado não encontrado.");
    }
}

void libertarMercados(Mercados *mercados) {
    if (mercados->arrayMercados) {
        free(mercados->arrayMercados);
        mercados->arrayMercados = NULL;
    }
}

void atualizarMercados(Mercados *mercados) {
    int i;
    char* codMercado = malloc(MAX_COD_MERCADO * sizeof(char));
    lerString(codMercado, MAX_COD_MERCADO, "Insira o codigo do mercado:\n");
    int mercado = procurarMercado(mercados, codMercado);
    int op;
    printf("Deseja alterar o estado do mercado?\n");
    printf("1 - Sim\n 2 - Nao\n");

    op = obterInt(1, 2, "Opcao:\n");

    switch (op) {
        case 1:
            if (mercados->arrayMercados[mercado].estadoMercado == ATIVO) {
                mercados->arrayMercados[mercado].estadoMercado = INATIVO;
            } else {
                mercados->arrayMercados[mercado].estadoMercado = ATIVO;
            }
            break;

        case 2:
            break;
    }

    free(codMercado);
}

void guardarMercados(Mercados *mercados, char *ficheiro) {
    int i;

    FILE *apontadorFicheiro = fopen(ficheiro, "wb");

    if (apontadorFicheiro == NULL) {
        printf("Nao foi possivel guardar.\n");
        exit(EXIT_FAILURE);
    }

    fwrite(&mercados->contador, sizeof (int), 1, apontadorFicheiro);

    for (i = 0; i < mercados->contador; i++) {
        fwrite(&mercados->arrayMercados[i], sizeof (Mercado), 1, apontadorFicheiro);
    }

    fclose(apontadorFicheiro);
}

