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
public class NodoBloque extends NodoInstruccion {

    private final List<NodoInstruccion> instrucciones;

    public NodoBloque(List<NodoInstruccion> instrucciones, int linea, int columna) {
        super(linea, columna);
        this.instrucciones = instrucciones;
    }

    public List<NodoInstruccion> getInstrucciones() {
        return instrucciones;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
