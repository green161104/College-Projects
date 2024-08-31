#include "geral.h"
#include "input.h"
#include "mercados.h"
#include "vendedor.h"
#include "comissoes.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

Mercados mercados;
Vendedores vendedores;

int main() {
    
    carregarVendedores(&vendedores, VENDEDORES_F);
    carregarMercados(&mercados, MERCADOS_F);

    apresentarMenus();
    return 0;
}
