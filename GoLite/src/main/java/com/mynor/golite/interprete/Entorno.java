/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.interprete;

import com.mynor.golite.ast.NodoDefFuncion;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mynordma
 */
public class Entorno {

    private final Entorno padre;
    private final Map<String, Simbolo> tabla;
    private HashMap<String, SimboloFuncion> funciones;

    public Entorno(Entorno padre) {
        this.padre = padre;
        this.tabla = new HashMap<>();
    }

    public Entorno getPadre() {
        return padre;
    }

    public boolean declarar(String nombre, Simbolo sim) {
        if (tabla.containsKey(nombre)) {
            return false;
        }
        tabla.put(nombre, sim);
        return true;
    }

    public Simbolo buscar(String nombre) {
        Entorno actual = this;
        while (actual != null) {
            if (actual.tabla.containsKey(nombre)) {
                return actual.tabla.get(nombre);
            }
            actual = actual.padre;
        }
        return null;
    }
    
    public void declararFuncion(String nombre, NodoDefFuncion nodoFuncion, String tipoRetorno) {
        if (funciones.containsKey(nombre)) {
            System.err.println("Error Semántico: La función '" + nombre + "' ya ha sido definida.");
            return;
        }

        SimboloFuncion nuevoSimbolo = new SimboloFuncion(nombre, nodoFuncion, tipoRetorno);
        
        funciones.put(nombre, nuevoSimbolo);
    }
    
    public SimboloFuncion buscarFuncion(String nombre) {
        Entorno actual = this;
        while (actual != null) {
            if (actual.funciones.containsKey(nombre)) {
                return actual.funciones.get(nombre);
            }
            actual = actual.padre;
        }
        return null;
    }
}
