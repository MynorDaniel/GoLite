/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.ast.analizadorsemantico;

/**
 *
 * @author mynordma
 */
public class TipoArreglo extends Tipo {
    
    private TipoEnum base;

    private int dimensiones; // 1 - 2

    public TipoArreglo(TipoEnum base, int dimensiones) {
        this.base = base;
        this.dimensiones = dimensiones;
    }

    public TipoEnum getBase() {
        return base;
    }

    public void setBase(TipoEnum base) {
        this.base = base;
    }

    public int getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(int dimensiones) {
        this.dimensiones = dimensiones;
    }
}
