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
public class NodoCasoDefault extends NodoAST {

    private final List<NodoInstruccion> instrucciones;

    public NodoCasoDefault(List<NodoInstruccion> is, int linea, int columna) {
        super(linea, columna);
        this.instrucciones = is;
    }

    public List<NodoInstruccion> getInstrucciones() {
        return instrucciones;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
