/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mynor.golite.lexer;

/**
 *
 * @author mynordma
 */
public enum TipoToken {
    RUNE_LIT,
    CADENA,
    ENTERO,
    FLOTANTE,
    IDENTIFICADOR,
    
    INT,
    FLOAT64,
    STRING,
    BOOL,
    NIL,
    
    TRUE,
    FALSE,
    
    FUNC,
    MAIN,
    VAR,
    IF,
    ELSE,
    SWITCH,
    CASE,
    DEFAULT,
    FOR,
    RANGE,
    BREAK,
    CONTINUE,
    RETURN,
    STRUCT,
    
    APPEND,
    LEN,
    
    FMT,
    PRINTLN,
    SLICES,
    INDEX,
    STRINGS,
    JOIN,
    STRCONV,
    ATOI,
    PARSEFLOAT,
    REFLECT,
    TYPEOF,
    
    PLUS_ASIG,
    MINUS_ASIG,
    IGUAL,
    NO_IGUAL,
    MAYOR_IGUAL,
    MENOR_IGUAL,
    AND,
    OR,
    DECL_ASIG,
    
    ASIG,
    MAS,
    MENOS,
    MULT,
    DIV,
    MODULO,
    NOT,
    MAYOR,
    MENOR,
    
    DOS_PUNTOS,
    PUNTO,
    COMA,
    PUNTO_COMA,
    PAR_IZQ,
    PAR_DER,
    COR_IZQ,
    COR_DER,
    LLA_IZQ,
    LLA_DER,
    
    ERROR
}
