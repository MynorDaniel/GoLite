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
public class NodoPrograma extends NodoAST {

    private final List<NodoDeclaracionGlobal> globales;

    public NodoPrograma(List<NodoDeclaracionGlobal> globales, int linea, int columna) {
        super(linea, columna);
        this.globales = globales;
    }

    public List<NodoDeclaracionGlobal> getGlobales() {
        return globales;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
    
    
}
