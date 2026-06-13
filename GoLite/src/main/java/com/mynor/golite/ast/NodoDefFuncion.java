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
public class NodoDefFuncion extends NodoDeclaracionGlobal {

    private final String nombre;
    private final List<NodoParametro> parametros;
    private final NodoTipo tipoRetorno;
    private final NodoTipoSlice sliceRetorno;
    private final boolean esMain;
    private final NodoBloque bloque;

    public NodoDefFuncion(String nombre, List<NodoParametro> ps, NodoTipo t, NodoTipoSlice ts, boolean esMain, NodoBloque b, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.parametros = ps;
        this.tipoRetorno = t;
        this.sliceRetorno = ts;
        this.esMain = esMain;
        this.bloque = b;
    }

    public String getNombre() {
        return nombre;
    }

    public List<NodoParametro> getParametros() {
        return parametros;
    }

    public NodoTipo getTipoRetorno() {
        return tipoRetorno;
    }

    public NodoTipoSlice getSliceRetorno() {
        return sliceRetorno;
    }

    public boolean isEsMain() {
        return esMain;
    }

    public NodoBloque getBloque() {
        return bloque;
    }
    
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
