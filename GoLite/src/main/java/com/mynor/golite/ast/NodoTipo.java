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
public class NodoTipo extends NodoAST {

    private final String nombreTipo;

    public NodoTipo(String nombreTipo, int linea, int columna) {
        super(linea, columna);
        this.nombreTipo = nombreTipo;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
