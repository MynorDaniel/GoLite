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
public class NodoDeclVar extends NodoDeclaracionGlobal {

    private final String identificador;
    private final NodoTipo tipo;
    private final NodoTipoSlice tipoSlice;
    private final NodoExpresion expresion;

    public NodoDeclVar(String id, NodoTipo t, NodoTipoSlice ts, NodoExpresion e, int linea, int columna) {
        super(linea, columna);
        this.identificador = id;
        this.tipo = t;
        this.tipoSlice = ts;
        this.expresion = e;
    }

    public String getIdentificador() {
        return identificador;
    }

    public NodoTipo getTipo() {
        return tipo;
    }

    public NodoTipoSlice getTipoSlice() {
        return tipoSlice;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
