/*
 * @file: input.c
 * @author: Sonia Oliveira
 * @date: 10/2/2023
 * @description: ficheiro com implentacoes de funcoes e estruturas relacionadas com obtencao de dados do utilizador
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define VALOR_INVALIDO "O valor inserido é invalido.\n"

void cleanInputBuffer() {
    int c;
    while ((c = getchar()) != '\n' && c != EOF) { }
}

int obterInt(int minValor, int maxValor, char *msg){
    int valor;
    puts (msg);
    while(scanf("%d", &valor)!= 1|| valor < minValor || valor> maxValor){
        puts(VALOR_INVALIDO);
        cleanInputBuffer();
        puts(msg);
    }
    cleanInputBuffer();
    return valor;
}

void lerString(char *string, int max, char *msg) {

    printf(msg);

    if (fgets(string, max, stdin) != NULL) {
        unsigned int len = strlen(string) - 1;
        if (string[len] == '\n') {
            string[len] = '\0';
        } else {
            cleanInputBuffer();
        }
    };
}

float obterFloat(float minValor, float maxValor, char *msg){
    float valor;
    printf(msg);
    while (scanf("%f", &valor)!= 1||valor < minValor || valor > maxValor){
        puts("Valor invalido");
        cleanInputBuffer();
        printf(msg);
    }
    cleanInputBuffer();
    return valor;
}

