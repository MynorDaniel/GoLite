/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.ast;

/**
 *
 * @author mynordma
 */
public abstract class NodoAST {
    public int linea;
    public int columna;

    public NodoAST(int linea, int columna) {
        this.linea = linea;
        this.columna = columna;
    }
}
