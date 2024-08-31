/*
 * @file: mercados.h
 * @author: Sónia Oliveira
 * @date: 11/2/2023
 * @description: ficheiro com declarações de funções e estruturas relacionadas com mercados
 */

#ifndef MERCADOS_H
#define MERCADOS_H

#include "vendedor.h"
#include "geral.h"
#define MAX_NOME_MERC 50
#define MAX_COD_MERCADO 4
#define TAM_INICIAL_MERCADOS 10

typedef struct{
    char nomeMercado[MAX_NOME_MERC];
    char codigoMercado[MAX_COD_MERCADO];
    Estado estadoMercado;
    int contadorVendedores;
}Mercado; //informações referentes a cada mercado

typedef struct{
    int contador;
    int capacidade;
    Mercado *arrayMercados;
}Mercados; //informações referentes ao array de mercados


/**
 * 
 * @param mercados array de mercados
 * @param ficheiro ficheiro onde se encontram armazenadas as informações referentes
 * a mercados
 * a função lê a info dos ficheiros e com ela popula a estrutura de mercados
 */
void carregarMercados(Mercados *mercados, char *ficheiro); 
        
/**
 * 
 * @param mercados array de mercados
 * pede ao user as informações do mercado e insere-o no array
 */
void criarMercado(Mercados *mercados); 


/**
 * 
 * @param mercados array de mercados
 * altera o estado do mercado para ativo ou inativo
 */
void atualizarMercados(Mercados *mercados); 


/**
 * 
 * @param mercados array de mercados
 * @param vendedores array de vendedores
 * verifica se não há vendedores associados ao mercado e depois remove-o
 */
void removerMercado(Mercados *mercados, Vendedores *vendedores);


/**
 * 
 * @param mercados array de mercados
 * @param cod codigo de mercado
 * @return o indice do mercado no array
 */
int procurarMercado(Mercados *mercados, char* cod); 

/**
 * 
 * @param mercados estrutura dos mercados
 * percorre o array de mercados e imprime as infos de cada mercado
 */
void listarMercados(Mercados mercados); 

/**
 * 
 * @param mercado estrutura do mercado
 * imprime as informações do mercado especificado
 */
void imprimirMercado(Mercado mercado); 

/**
 * 
 * @param mercados arrayd de mercados
 * realoca memoria para o array caso este atinja a sua capacidade máxima
 */
void expandirMercados(Mercados *mercados); 

/**
 * 
 * @param mercados array de mercados
 * verifica se existe espaço no array, se não, realoca a memória, e insere o mercado no array
 */
void inserirMercado(Mercados *mercados);

/**
 * 
 * @param mercados array de mercados
 * liberta a memóri alocada para os mercados
 */
void libertarMercados(Mercados *mercados);

/**
 * 
 * @param mercados array de mercados
 * @param ficheiro ficheiro onde escreve as infromações dos mercados
 */
void guardarMercados(Mercados *mercados, char *ficheiro);




#endif /* MERCADOS_H */

