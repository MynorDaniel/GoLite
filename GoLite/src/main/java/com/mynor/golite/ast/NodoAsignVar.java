/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.ast;

import com.mynor.golite.interprete.Visitor;

/**
 *
 * @author mynordma
 */
public class NodoAsignVar extends NodoDeclaracionGlobal {

    private final String identificador;
    private final NodoExpresion indice1;
    private final NodoExpresion indice2;
    private final String operador;
    private final NodoExpresion expresion;

    public NodoAsignVar(String id, NodoExpresion idx1, NodoExpresion idx2, String op, NodoExpresion e, int linea, int columna) {
        super(linea, columna);
        this.identificador = id;
        this.indice1 = idx1;
        this.indice2 = idx2;
        this.operador = op;
        this.expresion = e;
    }

    public String getIdentificador() {
        return identificador;
    }

    public NodoExpresion getIndice1() {
        return indice1;
    }

    public NodoExpresion getIndice2() {
        return indice2;
    }

    public String getOperador() {
        return operador;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}
