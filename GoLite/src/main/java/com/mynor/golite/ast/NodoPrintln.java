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
public class NodoPrintln extends NodoExpresion {

    private final List<NodoExpresion> argumentos;

    public NodoPrintln(List<NodoExpresion> args, int linea, int columna) {
        super(linea, columna);
        this.argumentos = args;
    }

    public List<NodoExpresion> getArgumentos() {
        return argumentos;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
