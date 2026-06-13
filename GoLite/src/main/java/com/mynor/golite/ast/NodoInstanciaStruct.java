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
public class NodoInstanciaStruct extends NodoExpresion {

    private final String nombreStruct;
    private final List<NodoCampoStruct> campos;

    public NodoInstanciaStruct(String nombre, List<NodoCampoStruct> campos, int linea, int columna) {
        super(linea, columna);
        this.nombreStruct = nombre;
        this.campos = campos;
    }

    public String getNombreStruct() {
        return nombreStruct;
    }

    public List<NodoCampoStruct> getCampos() {
        return campos;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
