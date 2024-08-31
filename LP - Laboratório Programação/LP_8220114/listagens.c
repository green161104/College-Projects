/*
 * @file: listagens.c
 * @author: Sónia Oliveira
 * @date: 16/2/2023
 * @description: ficheiro com implementação de funções relacionadas com listagens
 */

#include "geral.h"
#include "input.h"
#include "mercados.h"
#include "vendedor.h"
#include "comissoes.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>


int compararMercados(const void *a, const void *b) {
    const Mercado *mercadoA = a;
    const Mercado *mercadoB = b;
    return mercadoB->contadorVendedores - mercadoA->contadorVendedores;
}

void imprimirNMercadosMaisPopulados(Mercados *mercados, int n) {
    if (mercados->contador == 0) {
        printf("Nao ha mercados registados.\n");
        return;
    }

    Mercado *tempArray = (Mercado*) malloc(mercados->contador * sizeof(Mercado));
    memcpy(tempArray, mercados->arrayMercados, mercados->contador * sizeof(Mercado));

    qsort(tempArray, mercados->contador, sizeof(Mercado), compararMercados);

    printf("Os %d mercados com mais vendedores ativos:\n", n);
    for (int i = 0; i < n && i < mercados->contador; i++) {
        printf("%d. %s (%d vendedores)\n", i+1, tempArray[i].nomeMercado, tempArray[i].contadorVendedores);
    }

    free(tempArray);
}

void listarVendedoresComMaisMercados(Vendedores *vendedores) {
    int i, j;
    Vendedor temp;
    
    for (i = 0; i < vendedores->contador; i++) {
        for (j = i+1; j < vendedores->contador; j++) {
            if (vendedores->arrayVendedores[i].contadorMercados < vendedores->arrayVendedores[j].contadorMercados) {
                temp = vendedores->arrayVendedores[i];
                vendedores->arrayVendedores[i] = vendedores->arrayVendedores[j];
                vendedores->arrayVendedores[j] = temp;
            }
        }
    }
    
    printf("Ranking dos vendedores por nº de mercados em que estao ativos:\n");
    for (i = 0; i < vendedores->contador; i++) {
        printf("%d. %s: %d mercados ativos\n", i+1, vendedores->arrayVendedores[i].nomeVendedor, vendedores->arrayVendedores[i].contadorMercados);
    }
}

void listarVendedoresMaiorComissao(Vendedores *vendedores) {
    int i, j;
    float maxComissao = 0;
    Vendedor *vendedoresMaiorComissao;
    int numVendedoresMaiorComissao = 0;
    
    vendedoresMaiorComissao = (Vendedor*) malloc(vendedores->contador * sizeof(Vendedor));

    for (i = 0; i < vendedores->contador; i++) {
        for (j = 0; j < vendedores->arrayVendedores[i].contadorMercados; j++) {
            if (vendedores->arrayVendedores[i].arrayMercadosDeVendedor[j].comissoesVendedor > maxComissao) {
                maxComissao = vendedores->arrayVendedores[i].arrayMercadosDeVendedor[j].comissoesVendedor;
            }
        }
    }


    for (i = 0; i < vendedores->contador; i++) {
        for (j = 0; j < vendedores->arrayVendedores[i].contadorMercados; j++) {
            if (vendedores->arrayVendedores[i].arrayMercadosDeVendedor[j].comissoesVendedor == maxComissao) {
                vendedoresMaiorComissao[numVendedoresMaiorComissao++] = vendedores->arrayVendedores[i];
                break;
            }
        }
    }

    printf("Vendedores com maior comissao: ");
    for (i = 0; i < numVendedoresMaiorComissao; i++) {
        printf("%s ", vendedoresMaiorComissao[i].nomeVendedor);
    }
    printf("\n");

    free(vendedoresMaiorComissao);
}
