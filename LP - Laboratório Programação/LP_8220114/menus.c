
/*
 * @file: menus.c
 * @author: Sónia Oliveira
 * @date: 10/2/2023
 * @description: ficheiro com declarações de funções relacionadas com menus de utilizador
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "geral.h"
#include "input.h"
#include "mercados.h"
#include "vendedor.h"
#include "comissoes.h"
#include "listagens.h"

extern Mercados mercados;
extern Vendedores vendedores;

void menuVendedor() {
    int op;
    do {
        printf("\n-----------------Menu Vendedor-----------------\n");
        printf("\n1 - Criar Vendedor\n");
        printf("\n2 - Atualizar Vendedor\n");
        printf("\n3 - Remover Vendedor\n");
        printf("\n4 - Listar Vendedores\n");
        printf("\n0 - Voltar\n");

        op = obterInt(0, 4, "Opcao:\n");

        switch (op) {
            case 1:
                inserirVendedores(&vendedores);
                break;

            case 2:
                atualizarVendedor(&vendedores);
                break;

            case 3:
                removerVendedor(&vendedores);
                break;

            case 4:
                listarVendedores(vendedores);
                break;

            case 0:
                break;
        }
    } while (op != 0);
}

void menuMercados() {
    int op;

    do {
        printf("\n-----------------Menu Mercados-----------------\n");
        printf("\n1 - Criar Mercado\n");
        printf("\n2 - Atualizar Mercado\n");
        printf("\n3 - Remover Mercado\n");
        printf("\n4 - Listar Mercados\n");
        printf("\n0 - Voltar\n");

        op = obterInt(0, 4, "Opcao:\n");
        switch (op) {
            case 1:
                inserirMercado(&mercados);
                break;

            case 2:
                atualizarMercados(&mercados);
                break;

            case 3:
                removerMercado(&mercados, &vendedores);
                break;

            case 4:
                listarMercados(mercados);

            case 0:
                break;
        }
    } while (op != 0);
}

void listagens() {
    int op;
    do {
        printf("\n-----------------Listagens-----------------\n");
        printf("\n1 - Vendedores ordenados por número de mercados\n");
        printf("\n2 - Mercados mais populados\n");
        printf("\n3 - Comissoes mais altas por vendedor\n");
        printf("\n0 - Voltar\n");

        op = obterInt(0, 3, "Opcao:\n");
        switch (op) {
            case 1:
                listarVendedoresComMaisMercados(&vendedores);
                break;

            case 2:
                int n;
                printf("Quantos mercados quer listar?\n");
                scanf("%d", &n);
                imprimirNMercadosMaisPopulados(&mercados, n);
                break;

            case 3:
                listarVendedoresMaiorComissao(&vendedores);
                break;

            case 0:
                break;
        }
    } while (op != 0);
}

void menuMercadosEVendedores() {
    int op;
    do {
        printf("\n-----------------Mercados e Vendedores-----------------\n");
        printf("\n1 - Associar mercado a um vendedor\n");
        printf("\n2 - Desassociar mercado a um vendedor\n");
        printf("\n0 - Voltar\n");

        op = obterInt(0, 2, "Opcao:\n");
        switch (op) {
            case 1:
                atribuirMercado(&mercados, &vendedores);
                break;

            case 2:
                removerMercadoAtribuido(&mercados, &vendedores);
                break;

            case 0:
                break;
        }
    } while (op != 0);
}

void apresentarMenus() {
    int op;
    do {
        printf("\n--------------------Bem Vindo---------------------\n");
        printf("\n1 - Gerir Vendedores\n");
        printf("\n2 - Gerir Mercados\n");
        printf("\n3 - Mercados e vendedores\n");
        printf("\n4 - Listagens\n");
        printf("\n5 - Guardar\n");
        printf("\n0 - Sair\n");
        printf("\n--------------------------------------------------\n");

        op = obterInt(0, 5, "Opcao:\n");
        switch (op) {
            case 1:
                menuVendedor();
                break;
            case 2:
                menuMercados();
                break;
            case 3:
                menuMercadosEVendedores();
                break;
            case 4:
                listagens();
                break;
            case 5:
                guardarVendedores(&vendedores, VENDEDORES_F);
                guardarMercados(&mercados, MERCADOS_F);
                break;

            case 0:
                libertarMercados(&mercados);
                libertarVendedores(&vendedores);
                break;

        }

    } while (op != 0);

}