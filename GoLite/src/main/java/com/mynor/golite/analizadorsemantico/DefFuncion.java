/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.analizadorsemantico;

import com.mynor.golite.ast.NodoBloque;
import java.util.List;

/**
 *
 * @author mynordma
 */
public class DefFuncion {

    private String nombre;
    private List<ParametroFuncion> parametros;
    private Tipo retorno;
    private NodoBloque cuerpo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ParametroFuncion> getParametros() {
        return parametros;
    }

    public void setParametros(List<ParametroFuncion> parametros) {
        this.parametros = parametros;
    }

    public Tipo getRetorno() {
        return retorno;
    }

    public void setRetorno(Tipo retorno) {
        this.retorno = retorno;
    }

    public NodoBloque getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(NodoBloque cuerpo) {
        this.cuerpo = cuerpo;
    }

    @Override
    public String toString() {
        return "func " + nombre + "(" + parametros + ") : " + retorno;
    }

}
