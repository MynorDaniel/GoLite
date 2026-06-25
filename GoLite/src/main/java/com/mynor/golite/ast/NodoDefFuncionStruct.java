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
    private final String nombre;
    private final List<NodoParametro> ps;
    private final NodoTipo t;
    private final NodoTipoSlice ts;
    private final NodoBloque b;

    public NodoDefFuncionStruct(String structRec, String varRec, String nombre, List<NodoParametro> ps, NodoTipo t, NodoTipoSlice ts, NodoBloque b, int linea, int columna) {
        super(nombre, ps, t, ts, false, b, linea, columna);
        this.structReceptor = structRec;
        this.variableReceptor = varRec;
        this.nombre = nombre;
        this.ps = ps;
        this.t = t;
        this.ts = ts;
        this.b = b;
    }

    public String getStructReceptor() {
        return structReceptor;
    }

    public String getVariableReceptor() {
        return variableReceptor;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public List<NodoParametro> getPs() {
        return ps;
    }

    public NodoTipo getT() {
        return t;
    }

    public NodoTipoSlice getTs() {
        return ts;
    }

    public NodoBloque getB() {
        return b;
    }
    
    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
