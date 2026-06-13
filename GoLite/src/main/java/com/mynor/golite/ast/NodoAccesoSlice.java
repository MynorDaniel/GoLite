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
public class NodoAccesoSlice extends NodoExpresion {

    private final String identificador;
    private final NodoExpresion indice1;
    private final NodoExpresion indice2; // posible nil

    public NodoAccesoSlice(String id, NodoExpresion i1, NodoExpresion i2, int linea, int columna) {
        super(linea, columna);
        this.identificador = id;
        this.indice1 = i1;
        this.indice2 = i2;
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
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
