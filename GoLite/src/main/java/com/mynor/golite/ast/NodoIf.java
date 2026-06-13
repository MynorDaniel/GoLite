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
public class NodoIf extends NodoInstruccion {

    private final NodoExpresion condicion;
    private final NodoBloque bloqueThen;
    private final NodoBloque bloqueElse;
    private final NodoIf elseIfSiguiente;

    public NodoIf(NodoExpresion cond, NodoBloque thenB, NodoBloque elseB, NodoIf elseIf, int linea, int columna) {
        super(linea, columna);
        this.condicion = cond;
        this.bloqueThen = thenB;
        this.bloqueElse = elseB;
        this.elseIfSiguiente = elseIf;
    }

    public NodoExpresion getCondicion() {
        return condicion;
    }

    public NodoBloque getBloqueThen() {
        return bloqueThen;
    }

    public NodoBloque getBloqueElse() {
        return bloqueElse;
    }

    public NodoIf getElseIfSiguiente() {
        return elseIfSiguiente;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
