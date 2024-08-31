/*
 * @file: vendedores.c
 * @author: Sónia Oliveira
 * @date: 12/2/2023
 * @description: ficheiro com implementação de funções relacionadas com vendedores
 */


#include "geral.h"
#include "input.h"
#include "mercados.h"
#include "vendedor.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void carregarVendedores(Vendedores *vendedores, char *ficheiro) {

    int i, sucesso = 0;

    FILE *apontadorFicheiro = fopen(ficheiro, "rb");

    if (apontadorFicheiro != NULL) {

        fread(&vendedores->contador, sizeof (int), 1, apontadorFicheiro);

        if (vendedores->contador > 0) {
            vendedores->capacidade = vendedores->contador;
            vendedores->arrayVendedores = (Vendedor *) malloc(vendedores->capacidade * sizeof (Vendedor));


            for (i = 0; i < vendedores->contador; i++) {
                fread(&vendedores->arrayVendedores[i], sizeof (Vendedor), 1, apontadorFicheiro);
                vendedores->arrayVendedores[i].arrayMercadosDeVendedor =
                        malloc(vendedores->arrayVendedores[i].capacidadeMercados * sizeof (MercadoAtribuido));
                fread(vendedores->arrayVendedores[i].arrayMercadosDeVendedor,
                        sizeof (MercadoAtribuido), vendedores->arrayVendedores[i].contadorMercados, apontadorFicheiro);
            }
            sucesso = 1;
        }


    }
    if (sucesso != 1) {
        apontadorFicheiro = fopen(ficheiro, "wb");
        if (apontadorFicheiro != NULL) {
            vendedores->arrayVendedores = (Vendedor *) malloc(VENDEDORES_INICIAL * sizeof (Vendedor));
            vendedores->contador = 0;
            vendedores->capacidade = VENDEDORES_INICIAL;

        }
    }
    fclose(apontadorFicheiro);
}

void imprimirVendedor(Vendedor vendedor) {
    int i;
    printf("Codigo:%s\nNome:%s\nE-mail:%s\nNumero de Telefone:%ld\n", vendedor.codVendedor, vendedor.nomeVendedor, vendedor.email, vendedor.telefone);
    if (vendedor.estadoVendedor == 0) {
        printf("Estado do Vendedor: ATIVO\n");
    } else {
        printf("Estado do Vendedor: INATIVO\n");
    };
    printf("Mercados:\n");
    for (i = 0; i < vendedor.contadorMercados; i++) {
        printf("%s (Comissao de %2.2f%%)\n\n", vendedor.arrayMercadosDeVendedor[i].codigoMercado, vendedor.arrayMercadosDeVendedor[i].comissoesVendedor);
    }
    puts("-----------------------------");
}

int procurarVendedor(Vendedores vendedores, char *codVendedor) {
    int i;
    for (i = 0; i < vendedores.contador; i++) {
        if (strcmp(vendedores.arrayVendedores[i].codVendedor, codVendedor) == 0) {
            return i;
        }
    }
    return -1;
}

int criarVendedor(Vendedores *vendedores) {
    long int telefone;
    lerString(vendedores->arrayVendedores[vendedores->contador].nomeVendedor, MAX_NOME_VENDEDOR,
            "Insira o nome do vendedor:\n");
    lerString(vendedores->arrayVendedores[vendedores->contador].codVendedor, COD_VENDEDOR,
            "Insira o codigo do vendedor (4 dígitos):\n");
    lerString(vendedores->arrayVendedores[vendedores->contador].email, MAX_NOME_VENDEDOR,
            "Insira o email do vendedor:\n");
    telefone = obterInt(900000000, 999999999, "Número de Telefone:");
    vendedores->arrayVendedores[vendedores->contador].telefone = telefone;
    cleanInputBuffer();

    if (procurarVendedor(*vendedores, vendedores->arrayVendedores[vendedores->contador].codVendedor) == -1) {
        vendedores->arrayVendedores[vendedores->contador].estadoVendedor = INATIVO;

        vendedores->arrayVendedores[vendedores->contador].contadorMercados = 0;
        vendedores->arrayVendedores[vendedores->contador].capacidadeMercados = TAM_INICIAL_MERCADOS;
        vendedores->arrayVendedores[vendedores->contador].arrayMercadosDeVendedor = (MercadoAtribuido *) malloc(
                TAM_INICIAL_MERCADOS * sizeof (MercadoAtribuido));
        return vendedores->contador++;
    } else {
        return -1;
    }
}

void expandirVendedores(Vendedores *vendedores) {
    int tam = (vendedores->capacidade) == 0 ? VENDEDORES_INICIAL : vendedores->capacidade * 2;
    Vendedor *temp = (Vendedor *) realloc(vendedores->arrayVendedores, sizeof (Vendedor) * tam);
    if (temp != NULL) {
        vendedores->capacidade = tam;
        vendedores->arrayVendedores = temp;
    }
}

void inserirVendedores(Vendedores *vendedores) {
    if (vendedores->contador == vendedores->capacidade) {
        expandirVendedores(vendedores);
    }

    if (vendedores->contador < vendedores->capacidade) {
        if (criarVendedor(vendedores) == -1) {
            puts("Vendedor ja existe.\n");
        }
    } else {
        puts("Nao e possivel registar um novo vendedor.\n");
    }
}

void listarVendedores(Vendedores vendedores) {
    int i;

    if (vendedores.contador > 0) {
        for (i = 0; i < vendedores.contador; i++) {
            imprimirVendedor(vendedores.arrayVendedores[i]);
        }
    } else {
        puts("Lista vazia.\n");
    }
}

void atualizarVendedor(Vendedores *vendedores) {

    char* codVendedor = malloc(COD_VENDEDOR * sizeof (char));
    char* nomeVendedor = malloc(MAX_NOME_VENDEDOR * sizeof (char));
    char* email = malloc(MAX_NOME_VENDEDOR * sizeof (char));
    long int telefone;
    Estado estado;
    lerString(codVendedor, COD_VENDEDOR, "Insira o codigo do vendedor:\n");
    int vendedor = procurarVendedor(*vendedores, codVendedor);

    if (vendedor != -1) {
        int op1;
        printf("Deseja alterar as informações pessoais do vendedor?\n1 - Sim\n2 - Nao");
        op1 = obterInt(1, 2, "Opção:\n");
        switch (op1) {
            case 1:
                lerString(nomeVendedor, MAX_NOME_VENDEDOR, "Insira o nome do vendedor:\n");
                lerString(email, MAX_NOME_VENDEDOR, "Insira o email do vendedor:\n");
                telefone = obterInt(900000000, 999999999, "Número de Telefone:");
                strcpy(vendedores->arrayVendedores[vendedor].nomeVendedor, nomeVendedor);
                strcpy(vendedores->arrayVendedores[vendedor].email, email);
                vendedores->arrayVendedores[vendedor].telefone = telefone;
                break;
            case 2:
                break;
        }
        int op2;

        printf("Deseja alterar o estado do vendedor?\n");
        printf("1 - Sim\n2 - Não\n");

        op2 = obterInt(1, 2, "Opção:\n");

        switch (op2) {
            case 1:
                if (vendedores->arrayVendedores[vendedor].estadoVendedor == ATIVO) {
                    vendedores->arrayVendedores[vendedor].estadoVendedor = INATIVO;
                } else {
                    vendedores->arrayVendedores[vendedor].estadoVendedor = ATIVO;
                }
                printf("Estado alterado com sucesso.\n");
                break;

            case 2:
                break;
        }
    }
    free(codVendedor);
    free(nomeVendedor);
    free(email);
}

void libertarVendedores(Vendedores *vendedores) {
    int i;
    for (i = 0; i < vendedores->contador; i++) {
        free(vendedores->arrayVendedores[i].arrayMercadosDeVendedor);
    }
    free(vendedores->arrayVendedores);
    vendedores->arrayVendedores = NULL;
    vendedores->contador = 0;
    vendedores->capacidade = 0;
}

void removerVendedor(Vendedores *vendedores) {
    int i, vendedor;

    char codVendedor[COD_VENDEDOR];
    lerString(codVendedor, COD_VENDEDOR, "Insira o codigo do vendedor:\n");

    vendedor = procurarVendedor(*vendedores, codVendedor);

    if (vendedor != -1) {
        if (vendedores->arrayVendedores[vendedor].contadorMercados > 0) {
            printf("Não é possivel remover o vendedor, pois ele ainda está associado a mercado(s).\n");
            return;
        }

        free(vendedores->arrayVendedores[vendedor].arrayMercadosDeVendedor);
        for (i = vendedor; i < vendedores->contador - 1; i++) {
            vendedores->arrayVendedores[i] = vendedores->arrayVendedores[i + 1];
        }
        vendedores->contador--;
    } else {
        puts("Vendedor não existe.\n");
    }
}

void guardarVendedores(Vendedores *vendedores, char *ficheiro) {

    int i;

    FILE *apontadorFicheiro = fopen(ficheiro, "wb");

    if (apontadorFicheiro != NULL) {
        fwrite(&vendedores->contador, sizeof (int), 1, apontadorFicheiro);

        if (vendedores->contador > 0) {
            for (i = 0; i < vendedores->contador; i++) {
                fwrite(&vendedores->arrayVendedores[i], sizeof (Vendedor), 1, apontadorFicheiro);
                fwrite(vendedores->arrayVendedores[i].arrayMercadosDeVendedor, sizeof (MercadoAtribuido),
                        vendedores->arrayVendedores[i].contadorMercados, apontadorFicheiro);
            }
        }
        fclose(apontadorFicheiro);
    } else {
        puts("Erro ao guardar vendedores.");
    }
    puts("Guardado com sucesso.\n");
}

int verificarVendedoresDeMercado(Vendedores vendedores, char *codMercado) {
    int numVendedores = 0;
    for (int i = 0; i < vendedores.contador; i++) {
        for (int j = 0; j < vendedores.arrayVendedores[i].contadorMercados; j++) {
            if (strcmp(codMercado, vendedores.arrayVendedores[i].arrayMercadosDeVendedor[j].codigoMercado) == 0) {
                numVendedores++;
            }
        }
    }
    return numVendedores;
}



