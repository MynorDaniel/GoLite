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
public class NodoLlamadaMetodo extends NodoExpresion {

    private final String objeto;
    private final String metodo;
    private final List<NodoExpresion> argumentos;

    public NodoLlamadaMetodo(String obj, String met, List<NodoExpresion> args, int linea, int columna) {
        super(linea, columna);
        this.objeto = obj;
        this.metodo = met;
        this.argumentos = args;
    }

    public String getObjeto() {
        return objeto;
    }

    public String getMetodo() {
        return metodo;
    }

    public List<NodoExpresion> getArgumentos() {
        return argumentos;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
