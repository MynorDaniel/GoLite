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
public class NodoTipoSlice extends NodoAST {

    private final NodoTipo tipoBase;
    private final int dimensiones;

    public NodoTipoSlice(NodoTipo tipoBase, int dimensiones, int linea, int columna) {
        super(linea, columna);
        this.tipoBase = tipoBase;
        this.dimensiones = dimensiones;
    }

    public NodoTipo getTipoBase() {
        return tipoBase;
    }

    public int getDimensiones() {
        return dimensiones;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
