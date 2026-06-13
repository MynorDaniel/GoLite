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
public class NodoSwitch extends NodoInstruccion {

    private final NodoExpresion expresion;
    private final List<NodoCaso> casos;
    private final NodoCasoDefault casoDefault;

    public NodoSwitch(NodoExpresion e, List<NodoCaso> cs, NodoCasoDefault d, int linea, int columna) {
        super(linea, columna);
        this.expresion = e;
        this.casos = cs;
        this.casoDefault = d;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }

    public List<NodoCaso> getCasos() {
        return casos;
    }

    public NodoCasoDefault getCasoDefault() {
        return casoDefault;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
