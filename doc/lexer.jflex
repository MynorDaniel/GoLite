package com.mynor.golite.lexer;
import java_cup.runtime.Symbol;
import java.util.ArrayList;
import com.mynor.golite.parser.Terminal;
%%

%{
    ArrayList<Token> tokens = new ArrayList<>();

    public void addToken(int linea, int columna, String lexema, TipoToken tipo) {
        tokens.add(new Token(linea, columna, lexema, tipo));
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }
%}

%class LexerGLT
%public
%cupsym Terminal
%cup
%char
%column
%line
%unicode

%init{
    yyline   = 0;
    yycolumn = 0;
%init}

ESPACIOS         = [ \t\r\n\f]+
IDENTIFICADOR    = [a-zA-Z_][a-zA-Z0-9_]*
ENTERO           = [0-9]+
FLOTANTE         = [0-9]+"."[0-9]+
COMENTARIO_SL    = "//"[^\n]*
COMENTARIO_ML    = "/*" !([^]* "*/" [^]*) ("*/")?
RUNE_LIT         = '([^\\'\r\n]|\\.)'  
CADENA           = \"([^\"\\\r\n]|\\.)*\"
CADENA_INCOMPLETA = \"([^\"\\\r\n]|\\.)*
RUNE_INCOMPLETO  = '([^\\'\r\n]|\\.)*

%%

{ESPACIOS}         { }
{COMENTARIO_SL}    { }
{COMENTARIO_ML}    { }

{RUNE_LIT}         { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.RUNE_LIT); return new Symbol(Terminal.RUNE_LIT, yyline, yycolumn, yytext().charAt(1)); }
{CADENA}           { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.CADENA); return new Symbol(Terminal.CADENA, yyline, yycolumn, yytext().substring(1, yytext().length() - 1)); }

{CADENA_INCOMPLETA} { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.ERROR); }
{RUNE_INCOMPLETO}   { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.ERROR); }

"int"           { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.INT); return new Symbol(Terminal.INT,      yyline, yycolumn); }
"float64"       { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.FLOAT64); return new Symbol(Terminal.FLOAT64,  yyline, yycolumn); }
"string"        { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.STRING); return new Symbol(Terminal.STRING,   yyline, yycolumn); }
"bool"          { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.BOOL); return new Symbol(Terminal.BOOL,     yyline, yycolumn); }
"nil"           { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.NIL); return new Symbol(Terminal.NIL,      yyline, yycolumn); }
"true"          { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.TRUE); return new Symbol(Terminal.TRUE,     yyline, yycolumn); }
"false"         { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.FALSE); return new Symbol(Terminal.FALSE,    yyline, yycolumn); }
"func"          { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.FUNC); return new Symbol(Terminal.FUNC,     yyline, yycolumn); }
"main"          { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.MAIN); return new Symbol(Terminal.MAIN,     yyline, yycolumn); }
"var"           { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.VAR); return new Symbol(Terminal.VAR,      yyline, yycolumn); }
"if"            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.IF); return new Symbol(Terminal.IF,       yyline, yycolumn); }
"else"          { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.ELSE); return new Symbol(Terminal.ELSE,     yyline, yycolumn); }
"switch"        { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.SWITCH); return new Symbol(Terminal.SWITCH,   yyline, yycolumn); }
"case"          { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.CASE); return new Symbol(Terminal.CASE,     yyline, yycolumn); }
"default"       { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.DEFAULT); return new Symbol(Terminal.DEFAULT,  yyline, yycolumn); }
"for"           { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.FOR); return new Symbol(Terminal.FOR,      yyline, yycolumn); }
"range"         { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.RANGE); return new Symbol(Terminal.RANGE,    yyline, yycolumn); }
"break"         { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.BREAK); return new Symbol(Terminal.BREAK,    yyline, yycolumn); }
"continue"      { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.CONTINUE); return new Symbol(Terminal.CONTINUE, yyline, yycolumn); }
"return"        { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.RETURN); return new Symbol(Terminal.RETURN,   yyline, yycolumn); }
"struct"        { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.STRUCT); return new Symbol(Terminal.STRUCT,   yyline, yycolumn); }
"append"        { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.APPEND); return new Symbol(Terminal.APPEND,   yyline, yycolumn); }
"len"           { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.LEN); return new Symbol(Terminal.LEN,      yyline, yycolumn); }

"fmt"           { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.FMT); return new Symbol(Terminal.FMT,        yyline, yycolumn); }
"Println"       { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.PRINTLN); return new Symbol(Terminal.PRINTLN,    yyline, yycolumn); }
"slices"        { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.SLICES); return new Symbol(Terminal.SLICES,     yyline, yycolumn); }
"Index"         { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.INDEX); return new Symbol(Terminal.INDEX,      yyline, yycolumn); }
"strings"       { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.STRINGS); return new Symbol(Terminal.STRINGS,    yyline, yycolumn); }
"Join"          { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.JOIN); return new Symbol(Terminal.JOIN,       yyline, yycolumn); }
"strconv"       { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.STRCONV); return new Symbol(Terminal.STRCONV,    yyline, yycolumn); }
"Atoi"          { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.ATOI); return new Symbol(Terminal.ATOI,       yyline, yycolumn); }
"ParseFloat"    { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.PARSEFLOAT); return new Symbol(Terminal.PARSEFLOAT, yyline, yycolumn); }
"reflect"       { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.REFLECT); return new Symbol(Terminal.REFLECT,    yyline, yycolumn); }
"TypeOf"        { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.TYPEOF); return new Symbol(Terminal.TYPEOF,     yyline, yycolumn); }

"+="            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.PLUS_ASIG); return new Symbol(Terminal.PLUS_ASIG,   yyline, yycolumn); }
"-="            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.MINUS_ASIG); return new Symbol(Terminal.MINUS_ASIG,  yyline, yycolumn); }
"=="            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.IGUAL); return new Symbol(Terminal.IGUAL,       yyline, yycolumn); }
"!="            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.NO_IGUAL); return new Symbol(Terminal.NO_IGUAL,    yyline, yycolumn); }
">="            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.MAYOR_IGUAL); return new Symbol(Terminal.MAYOR_IGUAL, yyline, yycolumn); }
"<="            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.MENOR_IGUAL); return new Symbol(Terminal.MENOR_IGUAL, yyline, yycolumn); }
"&&"            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.AND); return new Symbol(Terminal.AND,         yyline, yycolumn); }
"||"            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.OR); return new Symbol(Terminal.OR,          yyline, yycolumn); }
":="            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.DECL_ASIG); return new Symbol(Terminal.DECL_ASIG,   yyline, yycolumn); }

"="             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.ASIG); return new Symbol(Terminal.ASIG,       yyline, yycolumn); }
"+"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.MAS); return new Symbol(Terminal.MAS,        yyline, yycolumn); }
"++"            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.INCREMENTO); return new Symbol(Terminal.INCREMENTO, yyline, yycolumn); }
"--"            { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.DECREMENTO); return new Symbol(Terminal.DECREMENTO, yyline, yycolumn); }
"-"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.MENOS); return new Symbol(Terminal.MENOS,      yyline, yycolumn); }
"*"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.MULT); return new Symbol(Terminal.MULT,       yyline, yycolumn); }
"/"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.DIV); return new Symbol(Terminal.DIV,        yyline, yycolumn); }
"%"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.MODULO); return new Symbol(Terminal.MODULO,     yyline, yycolumn); }
"!"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.NOT); return new Symbol(Terminal.NOT,        yyline, yycolumn); }
">"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.MAYOR); return new Symbol(Terminal.MAYOR,      yyline, yycolumn); }
"<"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.MENOR); return new Symbol(Terminal.MENOR,      yyline, yycolumn); }
":"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.DOS_PUNTOS); return new Symbol(Terminal.DOS_PUNTOS, yyline, yycolumn); }
"."             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.PUNTO); return new Symbol(Terminal.PUNTO,      yyline, yycolumn); }
","             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.COMA); return new Symbol(Terminal.COMA,       yyline, yycolumn); }
";"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.PUNTO_COMA); return new Symbol(Terminal.PUNTO_COMA, yyline, yycolumn); }
"("             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.PAR_IZQ); return new Symbol(Terminal.PAR_IZQ,    yyline, yycolumn); }
")"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.PAR_DER); return new Symbol(Terminal.PAR_DER,    yyline, yycolumn); }
"["             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.COR_IZQ); return new Symbol(Terminal.COR_IZQ,    yyline, yycolumn); }
"]"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.COR_DER); return new Symbol(Terminal.COR_DER,    yyline, yycolumn); }
"{"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.LLA_IZQ); return new Symbol(Terminal.LLA_IZQ,    yyline, yycolumn); }
"}"             { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.LLA_DER); return new Symbol(Terminal.LLA_DER,    yyline, yycolumn); }

{FLOTANTE}      { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.FLOTANTE); return new Symbol(Terminal.FLOTANTE,     yyline, yycolumn, Double.parseDouble(yytext())); }
{ENTERO}        { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.ENTERO); return new Symbol(Terminal.ENTERO,       yyline, yycolumn, Integer.parseInt(yytext())); }

{IDENTIFICADOR} { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.IDENTIFICADOR); return new Symbol(Terminal.IDENTIFICADOR, yyline, yycolumn, yytext()); }

<<EOF>>         { return new Symbol(Terminal.EOF, yyline, yycolumn); }
.               { addToken(yyline + 1, yycolumn + 1, yytext(), TipoToken.ERROR); }
