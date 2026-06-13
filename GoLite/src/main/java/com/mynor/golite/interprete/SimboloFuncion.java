/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.interprete;

import com.mynor.golite.ast.NodoDefFuncion;

/**
 *
 * @author mynordma
 */
public class SimboloFuncion {

    private String nombre;
    private NodoDefFuncion nodo;
    private String tipoRetorno;

    public SimboloFuncion(String nombre, NodoDefFuncion nodo, String tipoRetorno) {
        this.nombre = nombre;
        this.nodo = nodo;
        this.tipoRetorno = tipoRetorno;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public NodoDefFuncion getNodo() {
        return nodo;
    }

    public String getTipoRetorno() {
        return tipoRetorno;
    }
}
