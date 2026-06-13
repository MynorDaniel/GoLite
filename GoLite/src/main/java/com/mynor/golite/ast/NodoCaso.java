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
public class NodoCaso extends NodoAST {

    private final NodoExpresion expresion;
    private final List<NodoInstruccion> instrucciones;

    public NodoCaso(NodoExpresion e, List<NodoInstruccion> is, int linea, int columna) {
        super(linea, columna);
        this.expresion = e;
        this.instrucciones = is;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }

    public List getInstrucciones() {
        return instrucciones;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
