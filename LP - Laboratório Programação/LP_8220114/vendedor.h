/*
 * @file: vendedores.h
 * @author: Sónia Oliveira
 * @date: 12/2/2023
 * @description: ficheiro com declarações de funções e estruturas relacionadas com vendedores
 */

#ifndef VENDEDOR_H
#define VENDEDOR_H

#define MAX_NOME_VENDEDOR 50
#define MAX_COD_MERCADO 4
#define VENDEDORES_INICIAL 5
#define MERCADOS_ATRIBUIDOS_INICIAL 5
#define COD_VENDEDOR 5
#define MAX_TEL 999999999
#define MIN_TEL 900000000
#include "geral.h"

typedef struct{
    char codigoMercado[MAX_COD_MERCADO];
    float comissoesVendedor;
    Data dataInicio;
    Data dataFinal;
}MercadoAtribuido; //informação relacionada com um dterminado mercadoa tribuido a um vendedor

typedef struct{
    char codVendedor[COD_VENDEDOR];
    char nomeVendedor[MAX_NOME_VENDEDOR];
    int long telefone;
    char email[MAX_NOME_VENDEDOR];
    MercadoAtribuido *arrayMercadosDeVendedor; //malloc para mercados de vendedor
    int contadorMercados;
    int capacidadeMercados;
    Estado estadoVendedor;
}Vendedor; //informações relacionadas com o vendedor 

typedef struct{
    int contador;
    int capacidade;
    Vendedor *arrayVendedores;
}Vendedores; //estrutura para armazenar o array de vendedores
 
/**
 * 
 * @param vendedores array de vendedores
 * @param ficheiro onde estão armazenadas as informações dos vendedores
 */
void carregarVendedores(Vendedores *vendedores, char *ficheiro);

/**
 * 
 * @param vendedor estrutura do vendedor
 * imprime as informações do vendededor desejado
 */
void imprimirVendedor(Vendedor vendedor);

/**
 * 
 * @param vendedores estrutura de vendedores
 * @param codVendedor codigo do vendedor
 * @return o indice do vendedor no array
 */
int procurarVendedor(Vendedores vendedores, char* codVendedor);

/**
 * 
 * @param vendedores apontador para o array vendedores
 * @return -1 se não conseguir criar
 */
int criarVendedor(Vendedores *vendedores);

/**
 * 
 * @param vendedores apontador para o array de vendedores
 * realoca a memoria para o array de vendedores caso este atinja a capacidade maxima
 */
void expandirVendedores(Vendedores *vendedores);

/**
 * 
 * @param vendedores apontador para o array de vendedores
 * expande o array caso necessário e insere o vendedor no array 
 */
void inserirVendedores(Vendedores *vendedores);

/**
 * 
 * @param vendedores estrutura de vendedores
 * lista os vendedores resgistados
 */
void listarVendedores(Vendedores vendedores);

/**
 * 
 * @param vendedores apontador para o array de vendedores
 * pede as informações a atualizar e pergunta se deseja alterar o estado
 */
void atualizarVendedor(Vendedores *vendedores);

/**
 * 
 * @param vendedores apontador para  array de vendedores
 * liberta a memoria alocada para os vendedores
 */
void libertarVendedores(Vendedores *vendedores);

/**
 * 
 * @param vendedores apontador para o array de vendedores
 * procura o vendedor e verifica que não tem mercados associados, e apaga o vendedor
 */
void removerVendedor(Vendedores *vendedores);

/**
 * 
 * @param vendedores apontador para o array de vendedores
 * @param ficheiro onde as informações dos vendedores são escritas
 */
void guardarVendedores(Vendedores *vendedores, char *ficheiro);

/**
 * 
 * @param vendedores estrutura de array dos vendedores
 * @param codMercado codigo do mercado
 * @return numVendedores, o número de vendedores listados num determeinado mercado
 */
int verificarVendedoresDeMercado(Vendedores vendedores, char *codMercado);

#endif /* VENDEDOR_H */

