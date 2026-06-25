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
public class NodoStringsJoin extends NodoExpresion {

    private final NodoExpresion slice;
    private final NodoExpresion separador;

    public NodoStringsJoin(NodoExpresion slice, NodoExpresion sep, int linea, int columna) {
        super(linea, columna);
        this.slice = slice;
        this.separador = sep;
    }

    public NodoExpresion getSlice() {
        return slice;
    }

    public NodoExpresion getSeparador() {
        return separador;
    }
    
    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
