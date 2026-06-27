/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.analizadorsemantico;

import java.util.HashMap;
import java.util.Map;

public class Entorno {

    private final Map<String, Simbolo> tabla;
    private Entorno padre;

    public Entorno(Entorno padre) {
        this.tabla = new HashMap<>();
        this.padre = padre;
    }

    public boolean insertar(String id, Simbolo simbolo) {
        if (tabla.containsKey(id)) {
            return false; // ya existe en el ambito
        }
        tabla.put(id, simbolo);
        return true;
    }

    public Simbolo buscar(String id) {
        Entorno actual = this;

        while (actual != null) {
            Simbolo s = actual.tabla.get(id);
            if (s != null) {
                return s;
            }
            actual = actual.padre;
        }

        return null; // no encontrado
    }

    public Simbolo buscarActual(String id) {
        return tabla.get(id);
    }

    public boolean actualizar(String id, Simbolo nuevoValor) {
        Entorno actual = this;

        while (actual != null) {
            if (actual.tabla.containsKey(id)) {
                actual.tabla.put(id, nuevoValor);
                return true;
            }
            actual = actual.padre;
        }

        return false; // no existe
    }

    public boolean declarar(String id, Simbolo simbolo) {
        if (buscarActual(id) != null) {
            return false; // redeclaracion
        }
        tabla.put(id, simbolo);
        return true;
    }

    public Entorno getPadre() {
        return padre;
    }

    public void setPadre(Entorno padre) {
        this.padre = padre;
    }

    public Map<String, Simbolo> getTabla() {
        return tabla;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Entorno actual = this;
        int nivel = 0;

        while (actual != null) {

            sb.append("=== Entorno Nivel ")
                    .append(nivel)
                    .append(" ===\n");

            if (actual.tabla.isEmpty()) {
                sb.append("  (vacío)\n");
            } else {
                for (Map.Entry<String, Simbolo> entry : actual.tabla.entrySet()) {
                    sb.append("  ")
                            .append(entry.getKey())
                            .append(" -> ")
                            .append(entry.getValue())
                            .append("\n");
                }
            }

            actual = actual.padre;
            nivel++;
        }

        return sb.toString();
    }
}
