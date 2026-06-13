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
public class NodoCampoStruct extends NodoAST {

    private final String campo;
    private final NodoExpresion expresion;

    public NodoCampoStruct(String campo, NodoExpresion e, int linea, int columna) {
        super(linea, columna);
        this.campo = campo;
        this.expresion = e;
    }

    public String getCampo() {
        return campo;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
