/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.interprete;

import com.mynor.golite.ast.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mynordma
 */
public class Interprete implements Visitor<Object> {

    private final Map<String, Object> tablaSimbolos = new HashMap<>();

    private final List<String> errores = new ArrayList<>();
    private final StringBuilder consola = new StringBuilder();

    public List<String> getErrores() {
        return errores;
    }

    public String getConsola() {
        return consola.toString();
    }

    private void error(String msg, int linea, int columna) {
        errores.add(msg + " " + linea + "|" + columna);
    }

    private String tipoDe(Object val) {
        if (val == null) {
            return "nil";
        }
        if (val instanceof Integer) {
            return "int";
        }
        if (val instanceof Double) {
            return "float64";
        }
        if (val instanceof String) {
            return "string";
        }
        if (val instanceof Boolean) {
            return "bool";
        }
        if (val instanceof List<?>) {
            return "slice";
        }
        return "nil";
    }

    @Override
    public Object visit(NodoPrograma nodo) {
        // Buscamos directamente la función 'main' en las declaraciones globales
        NodoDefFuncion nodoMain = null;
        for (NodoDeclaracionGlobal g : nodo.getGlobales()) {
            if (g instanceof NodoDefFuncion fn && "main".equals(fn.getNombre())) {
                nodoMain = fn;
                break;
            }
        }

        if (nodoMain == null) {
            error("No se encontró la función 'main'.", 0, 0);
        } else {
            nodoMain.getBloque().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(NodoBloque nodo) {
        for (NodoInstruccion inst : nodo.getInstrucciones()) {
            inst.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(NodoDeclVar nodo) {
        String nombre = nodo.getIdentificador();

        if (tablaSimbolos.containsKey(nombre)) {
            error("La variable '" + nombre + "' ya fue declarada.", nodo.getLinea(), nodo.getColumna());
            return null;
        }

        Object val = null;
        if (nodo.getExpresion() != null) {
            val = nodo.getExpresion().accept(this);
        } else {
            String tipo = nodo.getTipo() != null ? nodo.getTipo().getNombreTipo() : "slice";
            val = switch (tipo) {
                case "int" ->
                    0;
                case "float64" ->
                    0.0;
                case "string" ->
                    "";
                case "bool" ->
                    false;
                default ->
                    new ArrayList<>();
            };
        }

        tablaSimbolos.put(nombre, val);
        return null;
    }

    @Override
    public Object visit(NodoAsignVar nodo) {
        String nombre = nodo.getIdentificador();
        String op = nodo.getOperador();

        if (!tablaSimbolos.containsKey(nombre)) {
            error("Variable '" + nombre + "' no declarada.", nodo.getLinea(), nodo.getColumna());
            return null;
        }

        Object actual = tablaSimbolos.get(nombre);

        // Operadores de incremento/decremento (x++ / x--)
        if ("++".equals(op) || "--".equals(op)) {
            Object nuevo = "++".equals(op)
                    ? (actual instanceof Integer i ? i + 1 : (Double) actual + 1.0)
                    : (actual instanceof Integer i ? i - 1 : (Double) actual - 1.0);
            tablaSimbolos.put(nombre, nuevo);
            return null;
        }

        if (nodo.getIndice1() != null) {
            Object idxObj = nodo.getIndice1().accept(this);
            if (idxObj instanceof Integer idx && actual instanceof List<?>) {
                List<Object> lista = (List<Object>) actual;
                Object valExpr = nodo.getExpresion().accept(this);
                if (idx >= 0 && idx < lista.size()) {
                    lista.set(idx, valExpr);
                } else {
                    error("Índice fuera de rango.", nodo.getLinea(), nodo.getColumna());
                }
            }
            return null;
        }

        // Asignación común y operaciones compuestas (=, +=, -=)
        Object valExpr = nodo.getExpresion().accept(this);
        Object nuevoValor = switch (op) {
            case "=" ->
                valExpr;
            case "+=" ->
                (actual instanceof Integer i) ? i + (Integer) valExpr : (Double) actual + (Double) valExpr;
            case "-=" ->
                (actual instanceof Integer i) ? i - (Integer) valExpr : (Double) actual - (Double) valExpr;
            default ->
                valExpr;
        };

        tablaSimbolos.put(nombre, nuevoValor);
        return null;
    }

    @Override
    public Object visit(NodoBinaria nodo) {
        try {
            Object izq = nodo.getIzquierdo().accept(this);
            Object der = nodo.getDerecho().accept(this);
            String op = nodo.getOperador();

            return switch (op) {
                case "+" ->
                    (izq instanceof String s) ? s + der
                    : (izq instanceof Integer i) ? i + (Integer) der : (Double) izq + (Double) der;
                case "-" ->
                    (izq instanceof Integer i) ? i - (Integer) der : (Double) izq - (Double) der;
                case "*" ->
                    (izq instanceof Integer i) ? i * (Integer) der : (Double) izq * (Double) der;
                case "/" ->
                    (izq instanceof Integer i) ? i / (Integer) der : (Double) izq / (Double) der;
                case "==" ->
                    izq.equals(der);
                case "!=" ->
                    !izq.equals(der);
                case ">" ->
                    ((Number) izq).doubleValue() > ((Number) der).doubleValue();
                case "<" ->
                    ((Number) izq).doubleValue() < ((Number) der).doubleValue();
                case ">=" ->
                    ((Number) izq).doubleValue() >= ((Number) der).doubleValue();
                case "<=" ->
                    ((Number) izq).doubleValue() <= ((Number) der).doubleValue();
                case "&&" ->
                    (Boolean) izq && (Boolean) der;
                case "||" ->
                    (Boolean) izq || (Boolean) der;
                default ->
                    null;
            };
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object visit(NodoUnaria nodo) {
        Object val = nodo.getExpresion().accept(this);
        String op = nodo.getOperador();
        return "-".equals(op) ? (val instanceof Integer i ? -i : -(Double) val) : !(Boolean) val;
    }

    @Override
    public Object visit(NodoIdentificador nodo) {
        String nombre = nodo.getNombre();
        if (!tablaSimbolos.containsKey(nombre)) {
            error("Variable '" + nombre + "' no definida.", nodo.getLinea(), nodo.getColumna());
            return null;
        }
        return tablaSimbolos.get(nombre);
    }

    @Override
    public Object visit(NodoLiteral nodo) {
        return nodo.getValor();
    }

    @Override
    public Object visit(NodoAgrupacion nodo) {
        return nodo.getExpresion().accept(this);
    }

    @Override
    public Object visit(NodoIf nodo) {
        Object cond = nodo.getCondicion().accept(this);
        if (cond instanceof Boolean b && b) {
            nodo.getBloqueThen().accept(this);
        } else if (nodo.getElseIfSiguiente() != null) {
            nodo.getElseIfSiguiente().accept(this);
        } else if (nodo.getBloqueElse() != null) {
            nodo.getBloqueElse().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(NodoFor nodo) {
        if (nodo.getInit() != null) {
            nodo.getInit().accept(this);
        }

        while (true) {
            if (nodo.getCondicion() != null) {
                Object cond = nodo.getCondicion().accept(this);
                if (cond instanceof Boolean b && !b) {
                    break;
                }
            }

            nodo.getBloque().accept(this);

            if (nodo.getPost() != null) {
                nodo.getPost().accept(this);
            }
            if (nodo.getCondicion() == null && nodo.getInit() == null) {
                break; // Evita ciclos infinitos si es for vacío simple
            }
        }
        return null;
    }

    @Override
    public Object visit(NodoPrintln nodo) {
        List<NodoExpresion> args = nodo.getArgumentos();
        for (int i = 0; i < args.size(); i++) {
            Object val = args.get(i).accept(this);
            consola.append(val instanceof List<?> l ? l.toString() : String.valueOf(val));
            if (i < args.size() - 1) {
                consola.append(" ");
            }
        }
        consola.append("\n");
        return null;
    }

    @Override
    public Object visit(NodoLen nodo) {
        Object val = nodo.getExpresion().accept(this);
        if (val instanceof String s) {
            return s.length();
        }
        if (val instanceof List<?> l) {
            return l.size();
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visit(NodoAppend nodo) {
        Object sliceObj = nodo.getSlice().accept(this);
        Object elemento = nodo.getValor().accept(this);
        if (sliceObj instanceof List<?>) {
            List<Object> nuevaLista = new ArrayList<>((List<Object>) sliceObj);
            nuevaLista.add(elemento);
            return nuevaLista;
        }
        return new ArrayList<>();
    }

    @Override
    public Object visit(NodoLiteralSlice nodo) {
        List<Object> elementos = new ArrayList<>();
        if (nodo.getExpresiones() != null) {
            for (NodoExpresion expr : nodo.getExpresiones()) {
                elementos.add(expr.accept(this));
            }
        }
        return elementos;
    }

    @Override
    public Object visit(NodoAccesoSlice nodo) {
        Object obj = tablaSimbolos.get(nodo.getIdentificador());
        if (obj instanceof List<?> lista) {
            Object idx = nodo.getIndice1().accept(this);
            if (idx instanceof Integer i && i >= 0 && i < lista.size()) {
                return lista.get(i);
            }
        }
        return null;
    }

    @Override
    public Object visit(NodoDefFuncion n) {
        return null;
    }

    @Override
    public Object visit(NodoDefFuncionStruct n) {
        return null;
    }

    @Override
    public Object visit(NodoAsignCampo n) {
        return null;
    }

    @Override
    public Object visit(NodoBreak n) {
        return null;
    }

    @Override
    public Object visit(NodoContinue n) {
        return null;
    }

    @Override
    public Object visit(NodoReturn n) {
        return null;
    }

    @Override
    public Object visit(NodoLlamadaFuncion n) {
        return null;
    }

    @Override
    public Object visit(NodoLlamadaMetodo n) {
        return null;
    }

    @Override
    public Object visit(NodoSwitch n) {
        return null;
    }

    @Override
    public Object visit(NodoAtoi n) {
        return null;
    }

    @Override
    public Object visit(NodoParsefloat n) {
        return null;
    }

    @Override
    public Object visit(NodoTypeof n) {
        return null;
    }

    @Override
    public Object visit(NodoSlicesIndex n) {
        return null;
    }

    @Override
    public Object visit(NodoStringsJoin n) {
        return null;
    }

    @Override
    public Object visit(NodoDefStruct nodo) {
        return null;
    }

    @Override
    public Object visit(NodoAccesoCampo nodo) {
        return null;
    }

    @Override
    public Object visit(NodoInstanciaStruct nodo) {
        return null;
    }

    @Override
    public Object visit(NodoParametro nodo) {
        return null;
    }

    @Override
    public Object visit(NodoCampoStruct nodo) {
        return null;
    }

    @Override
    public Object visit(NodoAtributoStruct nodo) {
        return null;
    }

    @Override
    public Object visit(NodoCaso nodo) {
        return null;
    }

    @Override
    public Object visit(NodoCasoDefault nodo) {
        return null;
    }

    @Override
    public Object visit(NodoTipo nodo) {
        return null;
    }

    @Override
    public Object visit(NodoTipoSlice nodo) {
        return null;
    }

    @Override
    public Object visit(NodoError nodo) {
        return null;
    }
}
