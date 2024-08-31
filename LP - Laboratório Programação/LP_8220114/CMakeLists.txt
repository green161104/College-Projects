cmake_minimum_required(VERSION 3.24)
project(LP_8220114)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(LP_8220114
        nbproject/private/c_standard_headers_indexer.c
        nbproject/private/cpp_standard_headers_indexer.cpp
        geral.h
        input.c
        input.h
        main.c
        menus.c
        mercados.c
        mercados.h
        vendedor.h
        vendedores.c comissoes.c)
