/*
 * @file: comissoes.c
 * @author: Sonia Oliveira
 * @date: 14/2/2023
 * @description: ficheiro com implementaçao de funçoes relacionadas com mercados e vendedores
 */

#include "geral.h"
#include "input.h"
#include "mercados.h"
#include "vendedor.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void expandirArrayMercadosVendedor(Vendedores *vendedores) {
    int tam = (vendedores->arrayVendedores->capacidadeMercados) == 0 ? MERCADOS_ATRIBUIDOS_INICIAL : vendedores->arrayVendedores->capacidadeMercados * 2;
    MercadoAtribuido *temp = (MercadoAtribuido *) realloc(vendedores->arrayVendedores->arrayMercadosDeVendedor, sizeof (MercadoAtribuido) * tam);
    if (temp != NULL) {
        vendedores->arrayVendedores->capacidadeMercados = tam;
        vendedores->arrayVendedores->arrayMercadosDeVendedor = temp;
    }
}
int compararDatas(Data dataInicialNova, Data dataFinalNova, Data dataInicial, Data dataFinal) {
    if ((dataInicialNova.ano < dataFinal.ano || 
            (dataInicialNova.ano == dataFinal.ano && dataInicialNova.mes < dataFinal.mes) || 
            (dataInicialNova.ano == dataFinal.ano && dataInicialNova.mes == dataFinal.mes && dataInicialNova.dia <= dataFinal.dia))
        && (dataInicial.ano < dataFinalNova.ano || 
            (dataInicial.ano == dataFinalNova.ano && dataInicial.mes < dataFinalNova.mes) || 
            (dataInicial.ano == dataFinalNova.ano && dataInicial.mes == dataFinalNova.mes && dataInicial.dia <= dataFinalNova.dia))) {
        return 1; 
    } else {
        return 0; 
    }
}


int procurarMercadoAtribuido(Vendedores vendedores, char *codMercado, char *codVendedor) {

    int i;
    for (i = 0; i < vendedores.contador; i++) {
        if (strcmp(vendedores.arrayVendedores[i].codVendedor, codVendedor) == 0) {
            for (int j = 0; j < vendedores.arrayVendedores[i].contadorMercados; i++) {
                if (strcmp(vendedores.arrayVendedores[i].arrayMercadosDeVendedor[j].codigoMercado, codMercado) == 0) {
                    return j;
                }
            }
        }
        return -1;
    }
}

void atribuirMercado(Mercados *mercados, Vendedores *vendedores) {

    char codMercado[MAX_COD_MERCADO];
    char codVendedor[COD_VENDEDOR];
    int mercado = 0, vendedor = 0, i;
    float comissao;
    Data dataInicialNova, dataFinalNova;
    int contador;

    if (vendedores->arrayVendedores->contadorMercados == vendedores->arrayVendedores->capacidadeMercados) {
        expandirArrayMercadosVendedor(vendedores);
    }
    
    lerString(codMercado, MAX_COD_MERCADO, "Insira o codigo do mercado:\n");
    mercado = procurarMercado(mercados, codMercado);
    if (mercado == -1) {
        puts("O mercado não existe.\n");
        return;
    }

    lerString(codVendedor, COD_VENDEDOR, "Insira o codigo do vendedor:\n");
    vendedor = procurarVendedor(*vendedores, codVendedor);
    if (vendedor == -1) {
        puts("O vendedor não existe.\n");
        return;
    }
    int contadorMercados = vendedores->arrayVendedores[vendedor].contadorMercados;

    strcpy(vendedores->arrayVendedores[vendedor].arrayMercadosDeVendedor[contadorMercados].codigoMercado, codMercado);

    comissao = obterFloat(10, 90, "Insira o valor da comissao (percentagem):\n");
    vendedores->arrayVendedores[vendedor].arrayMercadosDeVendedor[mercado].comissoesVendedor = comissao;

    dataInicialNova.dia = obterInt(1, 31, "Dia de inicio:\n");
    dataInicialNova.mes = obterInt(1, 12, "Mes de inicio:\n");
    dataInicialNova.ano = obterInt(2023, 2025, "Ano de inicio:\n");

    dataFinalNova.dia = obterInt(1, 31, "Dia de fim:\n");
    dataFinalNova.mes = obterInt(1, 12, "Mes de fim:\n");
    dataFinalNova.ano = obterInt(2023, 2025, "Ano de fim:\n");

    contador = compararDatas(dataInicialNova, dataFinalNova, vendedores->arrayVendedores[vendedor].arrayMercadosDeVendedor[mercado].dataInicio,
            vendedores->arrayVendedores[vendedor].arrayMercadosDeVendedor[mercado].dataFinal);
    if (contador != 0) {
        printf("O vendedor ja esta associado a este mercado neste periodo de tempo.\n");
        return;
    }
    vendedores->arrayVendedores[vendedor].arrayMercadosDeVendedor[contadorMercados].dataInicio = dataInicialNova;
    vendedores->arrayVendedores[vendedor].arrayMercadosDeVendedor[contadorMercados].dataFinal = dataFinalNova;
    vendedores->arrayVendedores[vendedor].contadorMercados++;
    mercados->arrayMercados[mercado].contadorVendedores++;
    puts("Mercado atribuido com sucesso.");
}

void removerMercadoAtribuido(Mercados *mercados, Vendedores *vendedores) {

    char codMercado[MAX_COD_MERCADO];
    char codVendedor[COD_VENDEDOR];

    lerString(codMercado, MAX_COD_MERCADO, "Insira o codigo do mercado:\n");

    lerString(codVendedor, COD_VENDEDOR, "Insira o codigo do vendedor:\n");

    int vendedor = procurarVendedor(*vendedores, codVendedor);
    int mercadoAssociado = procurarMercadoAtribuido(*vendedores, codMercado, codVendedor);
    int mercado = procurarMercado(mercados, codMercado);
    if (mercadoAssociado != -1) {
        for (int i = mercadoAssociado; i < vendedores->arrayVendedores[vendedor].contadorMercados - 1; i++) {
            for (int j = i; j < vendedores->arrayVendedores[vendedor].contadorMercados - 1; j++) {
                vendedores->arrayVendedores[vendedor].arrayMercadosDeVendedor[j] = vendedores->arrayVendedores[vendedor].arrayMercadosDeVendedor[j + 1];
            }
        }
        vendedores->arrayVendedores[vendedor].contadorMercados--;
        mercados->arrayMercados[mercado].contadorVendedores--;
        puts("Mercado removido com sucesso.\n");
    } else {
        puts("Nao foi encontrado um mercado associado a este vendedor.\n");
    }
}
