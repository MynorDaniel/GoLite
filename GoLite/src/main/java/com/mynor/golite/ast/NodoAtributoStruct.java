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
public class NodoAtributoStruct extends NodoAST {

    private final String identificador;
    private final NodoTipo tipo;
    private final NodoTipoSlice tipoSlice;

    public NodoAtributoStruct(String id, NodoTipo t, NodoTipoSlice ts, int linea, int columna) {
        super(linea, columna);
        this.identificador = id;
        this.tipo = t;
        this.tipoSlice = ts;
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
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
