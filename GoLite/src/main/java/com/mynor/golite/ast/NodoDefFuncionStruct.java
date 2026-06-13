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
public class NodoDefFuncionStruct extends NodoDefFuncion {

    private final String structReceptor;
    private final String variableReceptor;

    public NodoDefFuncionStruct(String structRec, String varRec, String nombre, List<NodoParametro> ps, NodoTipo t, NodoTipoSlice ts, NodoBloque b, int linea, int columna) {
        super(nombre, ps, t, ts, false, b, linea, columna);
        this.structReceptor = structRec;
        this.variableReceptor = varRec;
    }

    public String getStructReceptor() {
        return structReceptor;
    }

    public String getVariableReceptor() {
        return variableReceptor;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
