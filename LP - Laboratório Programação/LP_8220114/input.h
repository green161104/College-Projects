/*
 * @file: input.h
 * @author: Sonia Oliveira
 * @date: 10/2/2023
 * @description: ficheiro com declaraçoes de funçoes e estruturas relacionadas com obtençao de dados do utilizador
 */

#ifndef INPUT_H
#define INPUT_H

/**
 * uma função utilizada para limpar quaisquer caracteres que ainda estejam presentes
 * no buffer de input depois da sua ultima utilização
 */
void cleanInputBuffer();

/**
 * 
 * @param minValor valor minimo que o utilizador pode introduzir
 * @param maxValor valor maximo que o utilizador pode introduzir
 * @param msg mensagem apresentada ao utilizador a pedir a informação
 * @return o valor introduzido pelo utilizador
 */
int obterInt(int minValor, int maxValor, char *msg);

/**
 * 
 * @param string string dada pelo utilizador
 * @param max maximo de caracteres na string
 * @param msg mensagem apresentada ao utilizador a pedir a informação
 */
void lerString(char *string, int max, char *msg);

/**
 * 
 * @param minValor valor minimo que o utilizador pode introduzir
 * @param maxValor valor maximo que o utilizador pode introduzir
 * @param msg mensagem apresentada ao utilizador a pedir a informação
 * @return o valor introduzido pelo utilizador
 */
float obterFloat(float minValor, float maxValor, char *msg);
#endif /* INPUT_H */

