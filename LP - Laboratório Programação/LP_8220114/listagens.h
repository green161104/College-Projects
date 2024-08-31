/*
 * @file: listagens.h
 * @author: Sonia Oliveira
 * @date: 16/2/2023
 * @description: ficheiro com declaraçoes de funçoes relacionadas com listagens
 */

#ifndef LISTAGENS_H
#define LISTAGENS_H

#include "mercados.h"
#include "vendedor.h"
#include "comissoes.h"
#include "listagens.h"

/**
 * 
 * @param a apontador do tipo void
 * @param b apontador do tipo void
 * @return a comparação entre o número de vendedores armazenados no contador de vendedores em cada mercado
 * 
 * esta função é utilizada para servir de parâmentro à função qsort, que irá ordenar os mercados por 
 * ordem decrescente de acordo com o número de vendedores a si associados, na função 
 * imprimirNMercadosMaisPopulados.
 */
int compararMercados(const void *a, const void *b);

/**
 * 
 * @param mercados array de mercados
 * @param n número de mercados a imprimir
 * 
 */
void imprimirNMercadosMaisPopulados(Mercados *mercados, int n);

/**
 * 
 * @param vendedores array de vendedores
 * lista os vendededores com mais mercados associados
 */
void listarVendedoresComMaisMercados(Vendedores *vendedores);

/**
 * 
 * @param vendedores array de vendededores
 * encontra a maior comissao de entre os vendededores e lista os que a têm
 */
void listarVendedoresMaiorComissao(Vendedores *vendedores);


#endif /* LISTAGENS_H */

