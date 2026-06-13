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
public class NodoUnaria extends NodoExpresion {

    private final String operador;
    private final NodoExpresion expresion;

    public NodoUnaria(String op, NodoExpresion e, int linea, int columna) {
        super(linea, columna);
        this.operador = op;
        this.expresion = e;
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
