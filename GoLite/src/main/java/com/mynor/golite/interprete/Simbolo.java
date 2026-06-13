/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.interprete;

/**
 *
 * @author mynordma
 */
public class Simbolo {

    private final String nombre;
    private final String tipo; // "int", "float64", "string", "bool", "rune"
    private final boolean esGlobal;

    public Simbolo(String nombre, String tipo, boolean esGlobal) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.esGlobal = esGlobal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isEsGlobal() {
        return esGlobal;
    }
}
