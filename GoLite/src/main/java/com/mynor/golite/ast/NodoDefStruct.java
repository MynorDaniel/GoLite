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
public class NodoDefStruct extends NodoDeclaracionGlobal {

    private final String nombreStruct;

    public String getNombreStruct() {
        return nombreStruct;
    }

    public List<NodoAtributoStruct> getAtributos() {
        return atributos;
    }
    private final List<NodoAtributoStruct> atributos;

    public NodoDefStruct(String nombre, List<NodoAtributoStruct> attrs, int linea, int columna) {
        super(linea, columna);
        this.nombreStruct = nombre;
        this.atributos = attrs;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
