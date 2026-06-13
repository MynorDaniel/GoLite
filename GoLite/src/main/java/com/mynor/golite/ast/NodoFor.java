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
public class NodoFor extends NodoInstruccion {

    private final NodoInstruccion init;
    private final NodoExpresion condicion;
    private final NodoInstruccion post;
    private final String rangeIdx;
    private final String rangeVal;
    private final NodoBloque bloque;
    private final boolean esRange;

    public NodoFor(NodoInstruccion init, NodoExpresion cond, NodoInstruccion post, String idx, String val, NodoBloque b, boolean esRange, int linea, int columna) {
        super(linea, columna);
        this.init = init;
        this.condicion = cond;
        this.post = post;
        this.rangeIdx = idx;
        this.rangeVal = val;
        this.bloque = b;
        this.esRange = esRange;
    }

    public NodoInstruccion getInit() {
        return init;
    }

    public NodoExpresion getCondicion() {
        return condicion;
    }

    public NodoInstruccion getPost() {
        return post;
    }

    public String getRangeIdx() {
        return rangeIdx;
    }

    public String getRangeVal() {
        return rangeVal;
    }

    public NodoBloque getBloque() {
        return bloque;
    }

    public boolean isEsRange() {
        return esRange;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
