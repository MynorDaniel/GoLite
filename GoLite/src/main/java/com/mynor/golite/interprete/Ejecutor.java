/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.interprete;

import com.mynor.golite.ast.*;
import com.mynor.golite.ast.analizadorsemantico.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mynordma
 */
public class Ejecutor implements Visitor<Object> {

    private Entorno entornoActual;
    private final Map<String, DefFuncion> funciones = new HashMap<>();
    private final Map<String, DefStruct> structs = new HashMap<>();
    private final Deque<Entorno> callStack = new ArrayDeque<>();

    private final StringBuilder salida = new StringBuilder();

    public String getSalida() {
        return salida.toString();
    }

    private String procesarEscapes(String s) {
        return s
            .replace("\\n", "\n")
            .replace("\\t", "\t")
            .replace("\\r", "\r")
            .replace("\\\"", "\"")
            .replace("\\'", "'")
            .replace("\\\\", "\\");
    }

    private String valorAString(Object val) {
        if (val == null) {
            return "nil";
        }
        if (val instanceof Boolean b) {
            return b ? "true" : "false";
        }
        if (val instanceof Double d) {
            if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15) {
                long l = d.longValue();
                return l + "";
            }
            return d.toString();
        }
        if (val instanceof Character c) {
            return String.valueOf(c);
        }
        if (val instanceof List<?> list) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(" ");
                sb.append(valorAString(list.get(i)));
            }
            sb.append("]");
            return sb.toString();
        }
        if (val instanceof Map<?, ?> map) {
            StringBuilder sb = new StringBuilder("{");
            boolean primero = true;
            for (Map.Entry<?, ?> e : map.entrySet()) {
                if (!primero) sb.append(" ");
                sb.append(e.getKey()).append(":").append(valorAString(e.getValue()));
                primero = false;
            }
            sb.append("}");
            return sb.toString();
        }
        return val.toString();
    }

    @Override
    public Object visit(NodoPrograma nodo) {

        Entorno global = new Entorno(null);
        entornoActual = global;

        for (NodoDeclaracionGlobal decl : nodo.getGlobales()) {
            decl.accept(this);
        }

        DefFuncion main = funciones.get("main");
        if (main == null) {
            throw new RuntimeException("No se encontró la función main");
        }

        ejecutarFuncion(main, new ArrayList<>());

        return null;
    }

    private Object ejecutarFuncion(DefFuncion f, List<Object> args) {

        callStack.push(entornoActual);
        entornoActual = new Entorno(entornoActual);

        try {
            List<ParametroFuncion> params = f.getParametros();

            for (int i = 0; i < params.size(); i++) {
                ParametroFuncion p = params.get(i);
                Simbolo s = new Simbolo();
                s.setId(p.getNombre());
                s.setTipo(p.getTipo());
                s.setValor(args.get(i));
                entornoActual.insertar(p.getNombre(), s);
            }

            Object resultado = f.getCuerpo().accept(this);

            if (resultado instanceof ReturnControl rc) {
                return rc.getValor();
            }

            return null;

        } finally {
            entornoActual = callStack.pop();
        }
    }

    private Object ejecutarMetodo(DefFuncion f, List<Object> args, Map<String, Object> instancia) {

        callStack.push(entornoActual);
        entornoActual = new Entorno(entornoActual);

        try {
            List<ParametroFuncion> params = f.getParametros();

            if (!params.isEmpty()) {
                ParametroFuncion pReceptor = params.get(0);
                Simbolo sReceptor = new Simbolo();
                sReceptor.setId(pReceptor.getNombre());
                sReceptor.setTipo(pReceptor.getTipo());
                sReceptor.setValor(instancia);   // referencia directa al Map
                entornoActual.insertar(pReceptor.getNombre(), sReceptor);
            }

            for (int i = 1; i < params.size(); i++) {
                ParametroFuncion p = params.get(i);
                Simbolo s = new Simbolo();
                s.setId(p.getNombre());
                s.setTipo(p.getTipo());
                s.setValor(args.get(i - 1));
                entornoActual.insertar(p.getNombre(), s);
            }

            Object resultado = f.getCuerpo().accept(this);

            if (resultado instanceof ReturnControl rc) {
                return rc.getValor();
            }

            return null;

        } finally {
            entornoActual = callStack.pop();
        }
    }

    @Override
    public Object visit(NodoDeclVar nodo) {

        String id = nodo.getIdentificador();

        Object valor = null;

        if (nodo.getExpresion() != null) {
            valor = nodo.getExpresion().accept(this);
        } else if (nodo.getTipo() != null) {
            valor = valorDefecto((Tipo) nodo.getTipo().accept(this));
        } else if (nodo.getTipoSlice() != null) {
            valor = new ArrayList<>();
        }

        Tipo tipo = null;
        if (nodo.getTipo() != null) {
            tipo = (Tipo) nodo.getTipo().accept(this);
        } else if (nodo.getTipoSlice() != null) {
            tipo = (Tipo) nodo.getTipoSlice().accept(this);
        } else {
            tipo = inferirTipo(valor);
        }

        if (tipo instanceof TipoPrimitivo tp
                && tp.getBase() == TipoEnum.FLOAT64
                && valor instanceof Integer iv) {
            valor = iv.doubleValue();
        }

        Simbolo s = new Simbolo();
        s.setId(id);
        s.setTipo(tipo);
        s.setValor(valor);
        s.setLinea(nodo.getLinea());
        s.setColumna(nodo.getColumna());

        entornoActual.insertar(id, s);

        return null;
    }

    private Object valorDefecto(Tipo tipo) {
        if (tipo instanceof TipoPrimitivo tp) {
            return switch (tp.getBase()) {
                case INT -> 0;
                case FLOAT64 -> 0.0;
                case STRING -> "";
                case BOOL -> false;
                case RUNE -> (char) 0;
                default -> null;
            };
        }
        if (tipo instanceof TipoArreglo) {
            return new ArrayList<>();
        }
        return null;
    }

    @Override
    public Object visit(NodoAsignVar nodo) {

        String id = nodo.getIdentificador();
        String op = nodo.getOperador();

        Simbolo s = entornoActual.buscar(id);
        if (s == null) {
            throw new RuntimeException("Variable no declarada: " + id);
        }

        NodoExpresion indiceNodo = nodo.getIndice1(); // puede ser null

        if (indiceNodo != null) {
            Object idx1Obj = indiceNodo.accept(this);
            int idx1 = toInt(idx1Obj);

            Object coleccion = s.getValor();
            if (!(coleccion instanceof List outerList)) {
                throw new RuntimeException("La variable " + id + " no es un slice");
            }

            if (nodo.getIndice2() != null) {
                Object idx2Obj = nodo.getIndice2().accept(this);
                int idx2 = toInt(idx2Obj);

                Object filaObj = outerList.get(idx1);
                if (!(filaObj instanceof List innerList)) {
                    throw new RuntimeException("El elemento [" + idx1 + "] de " + id + " no es un slice");
                }
                if (nodo.getExpresion() != null) {
                    Object nuevoVal = nodo.getExpresion().accept(this);
                    innerList.set(idx2, nuevoVal);
                }
                return null;
            }

            if (nodo.getExpresion() != null) {
                Object nuevoVal = nodo.getExpresion().accept(this);
                nuevoVal = coercionar(nuevoVal, s.getTipo());
                outerList.set(idx1, nuevoVal);
            }

            return null;
        }

        // Operadores ++ / --
        if ("++".equals(op) || "--".equals(op)) {
            Object actual = s.getValor();
            Object nuevo;
            if (actual instanceof Integer i) {
                nuevo = "++".equals(op) ? i + 1 : i - 1;
            } else if (actual instanceof Double d) {
                nuevo = "++".equals(op) ? d + 1.0 : d - 1.0;
            } else {
                throw new RuntimeException("Operador " + op + " no aplicable a " + actual);
            }
            s.setValor(nuevo);
            entornoActual.actualizar(id, s);
            return null;
        }

        Object exprVal = nodo.getExpresion().accept(this);
        Object actual = s.getValor();

        Object resultado = switch (op) {
            case "=" -> exprVal;
            case "+=" -> sumar(actual, exprVal);
            case "-=" -> restar(actual, exprVal);
            case "*=" -> multiplicar(actual, exprVal);
            case "/=" -> dividir(actual, exprVal);
            default -> throw new RuntimeException("Operador de asignación no soportado: " + op);
        };

        resultado = coercionar(resultado, s.getTipo());
        s.setValor(resultado);
        entornoActual.actualizar(id, s);

        return null;
    }

    @Override
    public Object visit(NodoAsignCampo nodo) {

        String nombreObj = nodo.getObjeto();
        String campo = nodo.getCampo();

        Simbolo s = entornoActual.buscar(nombreObj);
        if (s == null) {
            throw new RuntimeException("Variable no declarada: " + nombreObj);
        }

        Object instancia = s.getValor();
        if (!(instancia instanceof Map map)) {
            throw new RuntimeException("La variable " + nombreObj + " no es un struct");
        }

        Object nuevoVal = nodo.getExpresion().accept(this);
        map.put(campo, nuevoVal);

        return null;
    }

    @Override
    public Object visit(NodoDefFuncion nodo) {

        DefFuncion def = new DefFuncion();
        def.setNombre(nodo.getNombre());

        List<ParametroFuncion> params = new ArrayList<>();
        for (NodoParametro p : nodo.getParametros()) {
            ParametroFuncion pf = new ParametroFuncion();
            pf.setNombre(p.getIdentificador());
            Tipo t = (p.getTipo() != null)
                    ? (Tipo) p.getTipo().accept(this)
                    : (Tipo) p.getTipoSlice().accept(this);
            pf.setTipo(t);
            params.add(pf);
        }
        def.setParametros(params);

        Tipo retorno;
        if (nodo.getTipoRetorno() != null) {
            retorno = (Tipo) nodo.getTipoRetorno().accept(this);
        } else if (nodo.getSliceRetorno() != null) {
            retorno = (Tipo) nodo.getSliceRetorno().accept(this);
        } else {
            retorno = new TipoPrimitivo(TipoEnum.VOID);
        }
        def.setRetorno(retorno);
        def.setCuerpo(nodo.getBloque());

        funciones.put(def.getNombre(), def);

        return null;
    }

    @Override
    public Object visit(NodoDefFuncionStruct nodo) {

        String structName = nodo.getStructReceptor();
        String varReceiver = nodo.getVariableReceptor();
        String funcName = nodo.getNombre();
        String key = structName + "." + funcName;

        DefFuncion def = new DefFuncion();
        def.setNombre(key);

        List<ParametroFuncion> params = new ArrayList<>();

        ParametroFuncion receptor = new ParametroFuncion();
        receptor.setNombre(varReceiver);
        receptor.setTipo(new TipoStruct(structName));
        params.add(receptor);

        for (NodoParametro p : nodo.getPs()) {
            ParametroFuncion pf = new ParametroFuncion();
            pf.setNombre(p.getIdentificador());
            Tipo t = (p.getTipo() != null)
                    ? (Tipo) p.getTipo().accept(this)
                    : (Tipo) p.getTipoSlice().accept(this);
            pf.setTipo(t);
            params.add(pf);
        }
        def.setParametros(params);

        Tipo retorno;
        if (nodo.getT() != null) {
            retorno = (Tipo) nodo.getT().accept(this);
        } else if (nodo.getTs() != null) {
            retorno = (Tipo) nodo.getTs().accept(this);
        } else {
            retorno = new TipoPrimitivo(TipoEnum.VOID);
        }
        def.setRetorno(retorno);
        def.setCuerpo(nodo.getB());

        funciones.put(key, def);

        return null;
    }

    @Override
    public Object visit(NodoDefStruct nodo) {

        String nombre = nodo.getNombreStruct();
        DefStruct def = new DefStruct(nombre);

        for (NodoAtributoStruct attr : nodo.getAtributos()) {
            Tipo tipoCampo;
            if (attr.getTipo() != null) {
                tipoCampo = (Tipo) attr.getTipo().accept(this);
            } else {
                tipoCampo = (Tipo) attr.getTipoSlice().accept(this);
            }
            def.agregarCampo(attr.getIdentificador(), tipoCampo);
        }

        structs.put(nombre, def);

        return null;
    }

    @Override
    public Object visit(NodoBloque nodo) {

        Entorno anterior = entornoActual;
        entornoActual = new Entorno(anterior);

        Object resultado = null;

        for (NodoInstruccion ins : nodo.getInstrucciones()) {
            resultado = ins.accept(this);

            if (resultado instanceof ReturnControl
                    || resultado instanceof BreakControl
                    || resultado instanceof ContinueControl) {
                break;
            }
        }

        entornoActual = anterior;
        return resultado;
    }

    @Override
    public Object visit(NodoIf nodo) {

        Object cond = nodo.getCondicion().accept(this);

        if (!(cond instanceof Boolean)) {
            throw new RuntimeException("Condición del if no es booleana");
        }

        if ((Boolean) cond) {
            return nodo.getBloqueThen().accept(this);
        }

        // else if encadenado
        if (nodo.getElseIfSiguiente() != null) {
            return nodo.getElseIfSiguiente().accept(this);
        }

        // else
        if (nodo.getBloqueElse() != null) {
            return nodo.getBloqueElse().accept(this);
        }

        return null;
    }

    @Override
    public Object visit(NodoFor nodo) {

        Entorno anterior = entornoActual;
        entornoActual = new Entorno(anterior);

        Object resultado = null;

        if (!nodo.isEsRange()) {

            if (nodo.getInit() != null) {
                nodo.getInit().accept(this);
            }

            while (true) {

                if (nodo.getCondicion() != null) {
                    Object cond = nodo.getCondicion().accept(this);
                    if (!(cond instanceof Boolean)) {
                        throw new RuntimeException("Condición del for no es booleana");
                    }
                    if (!(Boolean) cond) break;
                }

                Object r = nodo.getBloque().accept(this);

                if (r instanceof BreakControl) break;

                if (r instanceof ContinueControl) {
                    if (nodo.getPost() != null) nodo.getPost().accept(this);
                    continue;
                }

                if (r instanceof ReturnControl) {
                    entornoActual = anterior;
                    return r;
                }

                if (nodo.getPost() != null) nodo.getPost().accept(this);
            }

        } else {

            // for range
            Object iterable = nodo.getIterable().accept(this);

            if (iterable instanceof List<?> list) {

                int idx = 0;
                for (Object val : list) {

                    if (nodo.getRangeIdx() != null) {
                        Simbolo sIdx = entornoActual.buscar(nodo.getRangeIdx());
                        if (sIdx == null) {
                            sIdx = new Simbolo();
                            sIdx.setId(nodo.getRangeIdx());
                            sIdx.setTipo(new TipoPrimitivo(TipoEnum.INT));
                            entornoActual.insertar(sIdx.getId(), sIdx);
                        }
                        sIdx.setValor(idx);
                        entornoActual.actualizar(sIdx.getId(), sIdx);
                    }

                    if (nodo.getRangeVal() != null) {
                        Simbolo sVal = entornoActual.buscar(nodo.getRangeVal());
                        if (sVal == null) {
                            sVal = new Simbolo();
                            sVal.setId(nodo.getRangeVal());
                            sVal.setTipo(inferirTipo(val));
                            entornoActual.insertar(sVal.getId(), sVal);
                        }
                        sVal.setValor(val);
                        entornoActual.actualizar(sVal.getId(), sVal);
                    }

                    Object r = nodo.getBloque().accept(this);

                    if (r instanceof BreakControl) break;
                    if (r instanceof ContinueControl) { idx++; continue; }
                    if (r instanceof ReturnControl) {
                        entornoActual = anterior;
                        return r;
                    }

                    idx++;
                }

            } else if (iterable instanceof String str) {

                int idx = 0;
                for (char c : str.toCharArray()) {

                    if (nodo.getRangeIdx() != null) {
                        Simbolo sIdx = entornoActual.buscar(nodo.getRangeIdx());
                        if (sIdx == null) {
                            sIdx = new Simbolo();
                            sIdx.setId(nodo.getRangeIdx());
                            sIdx.setTipo(new TipoPrimitivo(TipoEnum.INT));
                            entornoActual.insertar(sIdx.getId(), sIdx);
                        }
                        sIdx.setValor(idx);
                        entornoActual.actualizar(sIdx.getId(), sIdx);
                    }

                    if (nodo.getRangeVal() != null) {
                        Simbolo sVal = entornoActual.buscar(nodo.getRangeVal());
                        if (sVal == null) {
                            sVal = new Simbolo();
                            sVal.setId(nodo.getRangeVal());
                            sVal.setTipo(new TipoPrimitivo(TipoEnum.RUNE));
                            entornoActual.insertar(sVal.getId(), sVal);
                        }
                        sVal.setValor(c);
                        entornoActual.actualizar(sVal.getId(), sVal);
                    }

                    Object r = nodo.getBloque().accept(this);

                    if (r instanceof BreakControl) break;
                    if (r instanceof ContinueControl) { idx++; continue; }
                    if (r instanceof ReturnControl) {
                        entornoActual = anterior;
                        return r;
                    }

                    idx++;
                }
            }
        }

        entornoActual = anterior;
        return resultado;
    }

    @Override
    public Object visit(NodoSwitch nodo) {

        Object valorSwitch = nodo.getExpresion().accept(this);

        for (NodoCaso caso : nodo.getCasos()) {
            Object valorCaso = caso.getExpresion().accept(this);

            if (sonIguales(valorSwitch, valorCaso)) {
                for (NodoInstruccion ins : caso.getInstrucciones()) {
                    Object r = ins.accept(this);
                    if (r instanceof ReturnControl
                            || r instanceof BreakControl
                            || r instanceof ContinueControl) {
                        return r instanceof BreakControl ? null : r;
                    }
                }
                return null; 
            }
        }

        if (nodo.getCasoDefault() != null) {
            for (NodoInstruccion ins : nodo.getCasoDefault().getInstrucciones()) {
                Object r = ins.accept(this);
                if (r instanceof ReturnControl
                        || r instanceof BreakControl
                        || r instanceof ContinueControl) {
                    return r instanceof BreakControl ? null : r;
                }
            }
        }

        return null;
    }

    private boolean sonIguales(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        if (a instanceof Number na && b instanceof Number nb) {
            return na.doubleValue() == nb.doubleValue();
        }
        return a.equals(b);
    }

    @Override
    public Object visit(NodoReturn nodo) {
        Object valor = null;
        if (nodo.getExpresion() != null) {
            valor = nodo.getExpresion().accept(this);
        }
        return new ReturnControl(valor);
    }

    @Override
    public Object visit(NodoBreak nodo) {
        return new BreakControl();
    }

    @Override
    public Object visit(NodoContinue nodo) {
        return new ContinueControl();
    }

    @Override
    public Object visit(NodoBinaria nodo) {

        String op = nodo.getOperador();

        if ("&&".equals(op)) {
            Object izq = nodo.getIzquierdo().accept(this);
            if (!(Boolean) izq) return false;
            return nodo.getDerecho().accept(this);
        }
        if ("||".equals(op)) {
            Object izq = nodo.getIzquierdo().accept(this);
            if ((Boolean) izq) return true;
            return nodo.getDerecho().accept(this);
        }

        Object izq = nodo.getIzquierdo().accept(this);
        Object der = nodo.getDerecho().accept(this);

        return switch (op) {
            case "+" -> sumar(izq, der);
            case "-" -> restar(izq, der);
            case "*" -> multiplicar(izq, der);
            case "/" -> dividir(izq, der);
            case "%" -> modulo(izq, der);
            case "==" -> sonIguales(izq, der);
            case "!=" -> !sonIguales(izq, der);
            case "<"  -> comparar(izq, der) < 0;
            case ">"  -> comparar(izq, der) > 0;
            case "<=" -> comparar(izq, der) <= 0;
            case ">=" -> comparar(izq, der) >= 0;
            default -> throw new RuntimeException("Operador binario no soportado: " + op);
        };
    }

    @Override
    public Object visit(NodoUnaria nodo) {

        Object val = nodo.getExpresion().accept(this);
        String op = nodo.getOperador();

        return switch (op) {
            case "!" -> !(Boolean) val;
            case "-" -> {
                if (val instanceof Integer i) yield -i;
                if (val instanceof Double d) yield -d;
                throw new RuntimeException("Operador - unario no aplicable a " + val);
            }
            default -> throw new RuntimeException("Operador unario no soportado: " + op);
        };
    }

    @Override
    public Object visit(NodoAgrupacion nodo) {
        return nodo.getExpresion().accept(this);
    }

    @Override
    public Object visit(NodoLiteral nodo) {

        String tipo = nodo.getTipoLiteral();
        String rawVal = nodo.getValor().toString();

        return switch (tipo) {
            case "int" -> Integer.parseInt(rawVal);
            case "float64" -> Double.parseDouble(rawVal);
            case "bool" -> Boolean.parseBoolean(rawVal);
            case "nil" -> null;
            case "rune" -> {
                String r = procesarEscapes(rawVal);
                yield r.isEmpty() ? (char) 0 : r.charAt(0);
            }
            case "string" -> procesarEscapes(rawVal);
            default -> throw new RuntimeException("Tipo literal no soportado: " + tipo);
        };
    }

    @Override
    public Object visit(NodoIdentificador nodo) {

        Simbolo s = entornoActual.buscar(nodo.getNombre());
        if (s == null) {
            throw new RuntimeException("Variable no declarada: " + nodo.getNombre());
        }
        return s.getValor();
    }

    @Override
    public Object visit(NodoLlamadaFuncion nodo) {

        String id = nodo.getIdentificador();

        if (id.contains(".")) {
            int punto = id.indexOf('.');
            String nombreObj = id.substring(0, punto);
            String metodo    = id.substring(punto + 1);

            Simbolo s = entornoActual.buscar(nombreObj);
            if (s == null) {
                throw new RuntimeException("Objeto no declarado: " + nombreObj);
            }

            Object instanciaObj = s.getValor();
            if (!(instanciaObj instanceof Map<?, ?> rawMap)) {
                throw new RuntimeException("La variable " + nombreObj + " no es una instancia de struct");
            }
            Map<String, Object> instancia = (Map<String, Object>) rawMap;

            String nombreStruct;
            if (s.getTipo() instanceof TipoStruct ts) {
                nombreStruct = ts.getNombre();
            } else {
                throw new RuntimeException("No se pudo determinar el tipo struct de: " + nombreObj);
            }

            String key = nombreStruct + "." + metodo;
            DefFuncion def = funciones.get(key);
            if (def == null) {
                throw new RuntimeException("Método no definido: " + key);
            }

            List<Object> args = new ArrayList<>();
            for (NodoExpresion arg : nodo.getArgumentos()) {
                args.add(arg.accept(this));
            }

            return ejecutarMetodo(def, args, instancia);
        }

        // Llamada a función normal
        DefFuncion def = funciones.get(id);
        if (def == null) {
            throw new RuntimeException("Función no definida: " + id);
        }

        List<Object> args = new ArrayList<>();
        for (NodoExpresion arg : nodo.getArgumentos()) {
            args.add(arg.accept(this));
        }

        return ejecutarFuncion(def, args);
    }

    @Override
    public Object visit(NodoLlamadaMetodo nodo) {

        String nombreObj = nodo.getObjeto();
        String metodo    = nodo.getMetodo();

        Simbolo s = entornoActual.buscar(nombreObj);
        if (s == null) {
            throw new RuntimeException("Objeto no declarado: " + nombreObj);
        }

        Object instanciaObj = s.getValor();
        if (!(instanciaObj instanceof Map<?, ?> rawMap)) {
            throw new RuntimeException("La variable " + nombreObj + " no es una instancia de struct");
        }
        Map<String, Object> instancia = (Map<String, Object>) rawMap;

        String nombreStruct;
        if (s.getTipo() instanceof TipoStruct ts) {
            nombreStruct = ts.getNombre();
        } else {
            throw new RuntimeException("No se pudo determinar el tipo struct de: " + nombreObj);
        }

        String key = nombreStruct + "." + metodo;
        DefFuncion def = funciones.get(key);
        if (def == null) {
            throw new RuntimeException("Método no definido: " + key);
        }

        List<Object> args = new ArrayList<>();
        for (NodoExpresion arg : nodo.getArgumentos()) {
            args.add(arg.accept(this));
        }

        return ejecutarMetodo(def, args, instancia);
    }

    @Override
    public Object visit(NodoAccesoCampo nodo) {

        Simbolo s = entornoActual.buscar(nodo.getObjeto());
        if (s == null) {
            throw new RuntimeException("Variable no declarada: " + nodo.getObjeto());
        }

        Object instancia = s.getValor();
        if (!(instancia instanceof Map map)) {
            throw new RuntimeException("La variable " + nodo.getObjeto() + " no es un struct");
        }

        return map.get(nodo.getCampo());
    }

    @Override
    public Object visit(NodoAccesoSlice nodo) {

        Simbolo s = entornoActual.buscar(nodo.getIdentificador());
        if (s == null) {
            throw new RuntimeException("Variable no declarada: " + nodo.getIdentificador());
        }

        Object coleccion = s.getValor();
        Object idx1Obj = nodo.getIndice1().accept(this);
        int idx1 = toInt(idx1Obj);

        // Acceso 2D: mtx[i][j] 
        if (nodo.getIndice2() != null) {
            Object idx2Obj = nodo.getIndice2().accept(this);
            int idx2 = toInt(idx2Obj);

            if (!(coleccion instanceof List outerList)) {
                throw new RuntimeException("La variable " + nodo.getIdentificador() + " no es una matriz");
            }
            Object fila = outerList.get(idx1);
            if (!(fila instanceof List innerList)) {
                throw new RuntimeException("El elemento [" + idx1 + "] de " + nodo.getIdentificador() + " no es un slice");
            }
            return innerList.get(idx2);
        }

        // Acceso 1D: arr[i]
        if (coleccion instanceof List list) {
            return list.get(idx1);
        }
        if (coleccion instanceof String str) {
            return str.charAt(idx1);
        }

        throw new RuntimeException("La variable " + nodo.getIdentificador() + " no es indexable");
    }

    @Override
    public Object visit(NodoInstanciaStruct nodo) {

        String nombreTipo = nodo.getTipo();
        String nombreVar = nodo.getNombreStruct();

        Map<String, Object> instancia = new LinkedHashMap<>();

        DefStruct def = structs.get(nombreTipo);
        if (def != null) {
            for (Map.Entry<String, Tipo> campo : def.getAtributos().entrySet()) {
                instancia.put(campo.getKey(), valorDefecto(campo.getValue()));
            }
        }

        for (NodoCampoStruct campo : nodo.getCampos()) {
            Object val = campo.getExpresion().accept(this);
            instancia.put(campo.getCampo(), val);
        }

        Simbolo s = new Simbolo();
        s.setId(nombreVar);
        s.setTipo(new TipoStruct(nombreTipo));
        s.setValor(instancia);
        s.setLinea(nodo.getLinea());
        s.setColumna(nodo.getColumna());

        entornoActual.insertar(nombreVar, s);

        return instancia;
    }

    @Override
    public Object visit(NodoLiteralSlice nodo) {

        if (nodo.getDimensiones() == 1) {
            List<Object> lista = new ArrayList<>();
            for (NodoExpresion expr : nodo.getExpresiones()) {
                lista.add(expr.accept(this));
            }
            return lista;
        }

        // 2D: lista de listas
        List<Object> matriz = new ArrayList<>();
        for (NodoLiteralSlice fila : nodo.getFilas()) {
            matriz.add(fila.accept(this));
        }
        return matriz;
    }

    @Override
    public Object visit(NodoLen nodo) {

        Object val = nodo.getExpresion().accept(this);

        if (val instanceof List<?> list) return list.size();
        if (val instanceof String str) return str.length();

        throw new RuntimeException("len() no aplicable a: " + val);
    }

    @Override
    public Object visit(NodoAppend nodo) {

        Object sliceObj = nodo.getSlice().accept(this);
        Object valor = nodo.getValor().accept(this);

        if (!(sliceObj instanceof List list)) {
            throw new RuntimeException("append() requiere un slice");
        }

        List<Object> nueva = new ArrayList<>(list);
        nueva.add(valor);
        return nueva;
    }

    @Override
    public Object visit(NodoAtoi nodo) {

        Object val = nodo.getExpresion().accept(this);
        if (!(val instanceof String str)) {
            throw new RuntimeException("atoi() requiere string");
        }

        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException("atoi(): no se puede convertir \"" + str + "\" a int");
        }
    }

    @Override
    public Object visit(NodoParsefloat nodo) {

        Object val = nodo.getExpresion().accept(this);
        if (!(val instanceof String str)) {
            throw new RuntimeException("parseFloat() requiere string");
        }

        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException("parseFloat(): no se puede convertir \"" + str + "\" a float64");
        }
    }

    @Override
    public Object visit(NodoTypeof nodo) {

        Object val = nodo.getExpresion().accept(this);

        if (val instanceof Integer) return "int";
        if (val instanceof Double) return "float64";
        if (val instanceof Boolean) return "bool";
        if (val instanceof String) return "string";
        if (val instanceof Character) return "rune";
        if (val instanceof List) return "slice";
        if (val instanceof Map) return "struct";
        if (val == null) return "nil";

        return val.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public Object visit(NodoSlicesIndex nodo) {

        Object sliceObj = nodo.getSlice().accept(this);
        Object valor = nodo.getValor().accept(this);

        if (!(sliceObj instanceof List list)) {
            throw new RuntimeException("slices.Index() requiere un slice");
        }

        for (int i = 0; i < list.size(); i++) {
            if (sonIguales(list.get(i), valor)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object visit(NodoStringsJoin nodo) {

        Object sliceObj = nodo.getSlice().accept(this);
        Object sepObj = nodo.getSeparador().accept(this);

        if (!(sliceObj instanceof List list)) {
            throw new RuntimeException("strings.Join() requiere un slice");
        }

        String sep = sepObj != null ? sepObj.toString() : "";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(sep);
            sb.append(valorAString(list.get(i)));
        }

        return sb.toString();
    }

    @Override
    public Object visit(NodoPrintln nodo) {

        StringBuilder linea = new StringBuilder();

        List<NodoExpresion> args = nodo.getArgumentos();
        for (int i = 0; i < args.size(); i++) {
            if (i > 0) linea.append(" ");
            Object val = args.get(i).accept(this);
            linea.append(valorAString(val));
        }

        salida.append(linea).append("\n");

        return null;
    }

    @Override
    public Object visit(NodoParametro nodo) {
        if (nodo.getTipo() != null) return nodo.getTipo().accept(this);
        if (nodo.getTipoSlice() != null) return nodo.getTipoSlice().accept(this);
        return null;
    }

    @Override
    public Object visit(NodoCampoStruct nodo) {
        if (nodo.getExpresion() == null) return null;
        return nodo.getExpresion().accept(this);
    }

    @Override
    public Object visit(NodoAtributoStruct nodo) {
        if (nodo.getTipo() != null) return nodo.getTipo().accept(this);
        if (nodo.getTipoSlice() != null) return nodo.getTipoSlice().accept(this);
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

        return switch (nodo.getNombreTipo()) {
            case "int"     -> new TipoPrimitivo(TipoEnum.INT);
            case "string"  -> new TipoPrimitivo(TipoEnum.STRING);
            case "bool"    -> new TipoPrimitivo(TipoEnum.BOOL);
            case "float64" -> new TipoPrimitivo(TipoEnum.FLOAT64);
            case "rune"    -> new TipoPrimitivo(TipoEnum.RUNE);
            default        -> structs.containsKey(nodo.getNombreTipo())
                               ? new TipoStruct(nodo.getNombreTipo())
                               : new TipoPrimitivo(TipoEnum.ERROR);
        };
    }

    @Override
    public Object visit(NodoTipoSlice nodo) {
        Tipo base = (Tipo) nodo.getTipoBase().accept(this);
        if (base instanceof TipoPrimitivo tp) {
            return new TipoArreglo(tp.getBase(), nodo.getDimensiones());
        }
        return new TipoArreglo(TipoEnum.ERROR, nodo.getDimensiones());
    }

    @Override
    public Object visit(NodoError nodo) {
        return null;
    }

    private Object sumar(Object a, Object b) {
        if (a instanceof String sa && b instanceof String sb) return sa + sb;
        if (a instanceof String sa) return sa + valorAString(b);
        if (a instanceof Double || b instanceof Double) {
            return toDouble(a) + toDouble(b);
        }
        return toInt(a) + toInt(b);
    }

    private Object restar(Object a, Object b) {
        if (a instanceof Double || b instanceof Double) {
            return toDouble(a) - toDouble(b);
        }
        return toInt(a) - toInt(b);
    }

    private Object multiplicar(Object a, Object b) {
        if (a instanceof Double || b instanceof Double) {
            return toDouble(a) * toDouble(b);
        }
        return toInt(a) * toInt(b);
    }

    private Object dividir(Object a, Object b) {
        if (a instanceof Double || b instanceof Double) {
            double divisor = toDouble(b);
            if (divisor == 0.0) throw new RuntimeException("División por cero");
            return toDouble(a) / divisor;
        }
        int divisor = toInt(b);
        if (divisor == 0) throw new RuntimeException("División por cero");
        return toInt(a) / divisor;
    }

    private Object modulo(Object a, Object b) {
        int divisor = toInt(b);
        if (divisor == 0) throw new RuntimeException("Módulo por cero");
        return toInt(a) % divisor;
    }

    private int comparar(Object a, Object b) {
        if (a instanceof String sa && b instanceof String sb) {
            return sa.compareTo(sb);
        }
        if (a instanceof Character ca && b instanceof Character cb) {
            return Character.compare(ca, cb);
        }
        double da = toDouble(a);
        double db = toDouble(b);
        return Double.compare(da, db);
    }

    private int toInt(Object v) {
        if (v instanceof Integer i) return i;
        if (v instanceof Double d) return d.intValue();
        if (v instanceof Character c) return (int) c;
        if (v instanceof Boolean b) return b ? 1 : 0;
        throw new RuntimeException("No se puede convertir a int: " + v);
    }

    private double toDouble(Object v) {
        if (v instanceof Double d) return d;
        if (v instanceof Integer i) return i.doubleValue();
        if (v instanceof Character c) return (double) c;
        throw new RuntimeException("No se puede convertir a double: " + v);
    }

    private Object coercionar(Object val, Tipo tipo) {
        if (tipo instanceof TipoPrimitivo tp
                && tp.getBase() == TipoEnum.FLOAT64
                && val instanceof Integer i) {
            return i.doubleValue();
        }
        return val;
    }

    private Tipo inferirTipo(Object val) {
        if (val instanceof Integer) return new TipoPrimitivo(TipoEnum.INT);
        if (val instanceof Double)  return new TipoPrimitivo(TipoEnum.FLOAT64);
        if (val instanceof Boolean) return new TipoPrimitivo(TipoEnum.BOOL);
        if (val instanceof String)  return new TipoPrimitivo(TipoEnum.STRING);
        if (val instanceof Character) return new TipoPrimitivo(TipoEnum.RUNE);
        if (val instanceof List)    return new TipoArreglo(TipoEnum.INDEFINIDO, 1);
        return new TipoPrimitivo(TipoEnum.VOID);
    }
}