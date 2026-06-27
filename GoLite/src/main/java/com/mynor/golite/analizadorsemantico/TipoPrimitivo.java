/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.analizadorsemantico;

/**
 *
 * @author mynordma
 */
public class TipoPrimitivo extends Tipo {

    private final TipoEnum base;

    public TipoPrimitivo(TipoEnum base) {
        this.base = base;
    }

    public TipoEnum getBase() {
        return base;
    }
}
