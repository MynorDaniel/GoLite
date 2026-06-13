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
public class NodoError extends NodoDeclaracionGlobal {

    private final String mensajeError;

    public NodoError(String msg, int linea, int columna) {
        super(linea, columna);
        this.mensajeError = msg;
    }

    public String getMensajeError() {
        return mensajeError;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
