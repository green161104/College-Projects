/*
 * @file: comissoes.h
 * @author: Sónia Oliveira
 * @date: 14/2/2023
 * @description: ficheiro com declarações de funções relacionadas com mercados e vendedores
 */

#ifndef COMISSOES_H
#define COMISSOES_H

#include "mercados.h"
#include "vendedor.h"
#include "geral.h"

/**
 * 
 * @param dataInicialNova nova data inicial para o inicio de atividade num mercado
 * @param dataFinalNova nova data final para o fim de atividade num mercado
 * @param dataInicial data inicial da atividade nesse vendedor no mercado, já definida
 * @param dataFinal data inicial da atividade nesse vendedor no mercado, já definida
 * @return contador, que indica se os períodos coincidem ou não
 */
int compararDatas(Data dataInicialNova, Data dataFinalNova, Data dataInicial, Data dataFinal);

/**
 * 
 * @param vendedores recebe a estrutura Vendedores
 * @param codMercado código do mercado a que o vendedor pode estar atribuido
 * @param codVendedor código do vendedor
 * @return j, o índice do mercado no array de mercados de vendedor, caso o vendedor em causa lhe esteja atribuido
 */
int procurarMercadoAtribuido(Vendedores vendedores, char *codMercado, char *codVendedor);

/**
 * 
 * @param mercados array de mercados
 * @param vendedores array de vendedores
 */
void atribuirMercado(Mercados *mercados, Vendedores *vendedores);

/**
 * 
 * @param mercados apontador para array de mercados
 * @param vendedores apontador para array de vendedores
 */
void removerMercadoAtribuido(Mercados *mercados, Vendedores *vendedores);

#endif /* COMISSOES_H */

