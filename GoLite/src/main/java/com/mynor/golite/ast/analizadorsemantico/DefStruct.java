/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.ast.analizadorsemantico;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mynordma
 */
public class DefStruct {

    private final String nombre;

    private final Map<String, Tipo> atributos;

    public DefStruct(String nombre) {
        this.nombre = nombre;
        this.atributos = new HashMap<>();
    }

    public boolean agregarCampo(String nombre, Tipo tipo) {

        if (atributos.containsKey(nombre)) {
            return false;
        }

        atributos.put(nombre, tipo);
        return true;
    }

    public String getNombre() {
        return nombre;
    }

    public Map<String, Tipo> getAtributos() {
        return atributos;
    }

    public Tipo buscarCampo(String nombre) {
        return atributos.get(nombre);
    }

    public boolean existeCampo(String nombreCampo) {
        return atributos.containsKey(nombreCampo);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("struct ")
                .append(nombre)
                .append(" { ");

        boolean primero = true;

        for (Map.Entry<String, Tipo> campo : atributos.entrySet()) {

            if (!primero) {
                sb.append(", ");
            }

            sb.append(campo.getKey())
                    .append(": ")
                    .append(campo.getValue());

            primero = false;
        }

        sb.append(" }");

        return sb.toString();
    }
}
