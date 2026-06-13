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
public class NodoBinaria extends NodoExpresion {

    private final String operador;
    private final NodoExpresion izquierdo;
    private final NodoExpresion derecho;

    public NodoBinaria(String op, NodoExpresion l, NodoExpresion r, int linea, int columna) {
        super(linea, columna);
        this.operador = op;
        this.izquierdo = l;
        this.derecho = r;
    }

    public String getOperador() {
        return operador;
    }

    public NodoExpresion getIzquierdo() {
        return izquierdo;
    }

    public NodoExpresion getDerecho() {
        return derecho;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
