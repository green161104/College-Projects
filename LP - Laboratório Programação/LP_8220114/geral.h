/*
 * @file: geral.h
 * @author: Sónia Oliveira
 * @date: 10/2/2023
 * @description: ficheiro com declarações de funções e estruturas relacionadas com todo o programa
 */

#ifndef GERAL_H
#define GERAL_H

#define MERCADOS_F "mercados.bin"
#define VENDEDORES_F "vendedores.bin"

typedef enum{
    ATIVO, INATIVO
}Estado; //estrutura que descreve o estado, de um vendedor ou mercado

typedef struct{
    int dia, mes, ano;
}Data;

/**
 * apresenta o menu de opções de gestão de vendedores
 */
void menuVendedor();

/**
 * apresenta o menu de opções de gestão de mercados
 */
void menuMercados();

/**
 * apresenta as opções de listagens
 */
void listagens();

/**
 * apresenta o menu de opções de gestão de atribuição de mercados e vendedores
 */
void menuMercadosEVendedores();

/**
 * apresenta todos os menus ao utilizador
 */
void apresentarMenus();


#endif /* GERAL_H */

