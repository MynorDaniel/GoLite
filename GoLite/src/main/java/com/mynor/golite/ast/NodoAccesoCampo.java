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
public class NodoAccesoCampo extends NodoExpresion {

    private final String objeto;
    private final String campo;

    public NodoAccesoCampo(String obj, String campo, int linea, int columna) {
        super(linea, columna);
        this.objeto = obj;
        this.campo = campo;
    }

    public String getObjeto() {
        return objeto;
    }

    public String getCampo() {
        return campo;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
