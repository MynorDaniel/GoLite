/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mynor.golite.interprete;

import com.mynor.golite.ast.*;

/**
 *
 * @author mynordma
 */
public interface Visitor<T> {

    T visit(NodoPrograma nodo);

    T visit(NodoDeclVar nodo);
    T visit(NodoAsignVar nodo);
    T visit(NodoAsignCampo nodo);

    T visit(NodoDefFuncion nodo);
    T visit(NodoDefFuncionStruct nodo);
    T visit(NodoDefStruct nodo);

    T visit(NodoBloque nodo);
    T visit(NodoIf nodo);
    T visit(NodoFor nodo);
    T visit(NodoSwitch nodo);

    T visit(NodoReturn nodo);
    T visit(NodoBreak nodo);
    T visit(NodoContinue nodo);

    T visit(NodoBinaria nodo);
    T visit(NodoUnaria nodo);
    T visit(NodoAgrupacion nodo);

    T visit(NodoLiteral nodo);
    T visit(NodoIdentificador nodo);

    T visit(NodoLlamadaFuncion nodo);
    T visit(NodoLlamadaMetodo nodo);

    T visit(NodoAccesoCampo nodo);
    T visit(NodoAccesoSlice nodo);
    T visit(NodoSlicesIndex nodo);

    T visit(NodoInstanciaStruct nodo);
    T visit(NodoLiteralSlice nodo);

    T visit(NodoLen nodo);
    T visit(NodoAppend nodo);
    T visit(NodoAtoi nodo);
    T visit(NodoParsefloat nodo);
    T visit(NodoTypeof nodo);
    T visit(NodoStringsJoin nodo);
    T visit(NodoPrintln nodo);

    T visit(NodoParametro nodo);

    T visit(NodoCampoStruct nodo);
    T visit(NodoAtributoStruct nodo);

    T visit(NodoCaso nodo);
    T visit(NodoCasoDefault nodo);

    T visit(NodoTipo nodo);
    T visit(NodoTipoSlice nodo);

    T visit(NodoError nodo);
}