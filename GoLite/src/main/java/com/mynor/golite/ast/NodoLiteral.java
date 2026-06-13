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
public class NodoLiteral extends NodoExpresion {

    private final String tipoLiteral; // "int", "float64", "string", "bool", "rune", "nil"
    private final Object valor;

    public NodoLiteral(String tipo, Object valor, int linea, int columna) {
        super(linea, columna);
        this.tipoLiteral = tipo;
        this.valor = valor;
    }

    public String getTipoLiteral() {
        return tipoLiteral;
    }

    public Object getValor() {
        return valor;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
