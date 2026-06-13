/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.ast;

import com.mynor.golite.interprete.Visitor;
import java.util.List;

/**
 *
 * @author mynordma
 */
public class NodoLiteralSlice extends NodoExpresion {

    private final NodoTipo tipoBase;
    private final List<NodoLiteralSlice> filas; // 2d
    private final List<NodoExpresion> expresiones; // 1d
    private final int dimensiones;

    public NodoLiteralSlice(NodoTipo t, List<NodoLiteralSlice> fs, List<NodoExpresion> es, int dims, int linea, int columna) {
        super(linea, columna);
        this.tipoBase = t;
        this.filas = fs;
        this.expresiones = es;
        this.dimensiones = dims;
    }

    public NodoTipo getTipoBase() {
        return tipoBase;
    }

    public List<NodoLiteralSlice> getFilas() {
        return filas;
    }

    public List<NodoExpresion> getExpresiones() {
        return expresiones;
    }

    public int getDimensiones() {
        return dimensiones;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
