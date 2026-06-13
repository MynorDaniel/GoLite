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
public class NodoAtoi extends NodoExpresion {

    private final NodoExpresion expresion;

    public NodoAtoi(NodoExpresion e, int linea, int columna) {
        super(linea, columna);
        this.expresion = e;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
