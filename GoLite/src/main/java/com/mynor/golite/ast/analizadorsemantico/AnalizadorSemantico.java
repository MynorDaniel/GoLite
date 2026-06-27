/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.ast.analizadorsemantico;

import com.mynor.golite.ast.*;
import com.mynor.golite.interprete.Visitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mynordma
 */
public class AnalizadorSemantico implements Visitor<Tipo> {

    private Entorno entornoActual;
    private final Map<String, DefStruct> structs = new HashMap<>();
    private final Map<String, DefFuncion> funciones = new HashMap<>();
    private final List<String[]> errores = new ArrayList(); //[error, ln, col]

    @Override
    public Tipo visit(NodoPrograma nodo) {

        entornoActual = new Entorno(null);

        for (NodoDeclaracionGlobal d : nodo.getGlobales()) {
            if (d != null) {
                try {
                    d.accept(this);

                } catch (Exception e) {
                    e.printStackTrace();
                    agregarError("Error en instrucción", d.getLinea(), d.getColumna());
                }

            }
        }

        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoDeclVar nodo) {

        String id = nodo.getIdentificador();

        if (entornoActual.buscarActual(id) != null) {
            agregarError("Variable '" + id + "' ya declarada", nodo.getLinea(), nodo.getColumna());
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoDeclarado = null;

        if (nodo.getTipo() != null) {
            tipoDeclarado = nodo.getTipo().accept(this);
        } else if (nodo.getTipoSlice() != null) {
            tipoDeclarado = nodo.getTipoSlice().accept(this);
        }

        Tipo tipoExpresion = null;

        if (nodo.getExpresion() != null) {
            tipoExpresion = nodo.getExpresion().accept(this);
        }

        if (tipoDeclarado instanceof TipoPrimitivo tDecl
                && tipoExpresion instanceof TipoPrimitivo tExpr) {

            if (tDecl.getBase() == TipoEnum.FLOAT64
                    && tExpr.getBase() == TipoEnum.INT) {

                tipoExpresion = new TipoPrimitivo(TipoEnum.FLOAT64);
            }
        }

        if (tipoDeclarado != null && tipoExpresion != null) {
            if (!sonCompatibles(tipoDeclarado, tipoExpresion)) {
                agregarError(
                        "Tipos incompatibles en " + id,
                        nodo.getLinea(),
                        nodo.getColumna()
                );
            }
        }

        Tipo tipoFinal;

        if (tipoDeclarado != null) {
            tipoFinal = tipoDeclarado;
        } else {
            tipoFinal = tipoExpresion;
        }

        Simbolo s = new Simbolo();
        s.setId(id);
        s.setTipo(tipoFinal);
        s.setValor(0);
        s.setLinea(nodo.getLinea());
        s.setColumna(nodo.getColumna());

        entornoActual.insertar(id, s);

        return new TipoPrimitivo(TipoEnum.VOID);
    }

    private boolean sonCompatibles(Tipo a, Tipo b) {

        if (a instanceof TipoPrimitivo ta && b instanceof TipoPrimitivo tb) {

            if (tb.getBase() == TipoEnum.NIL) {
                return (ta.getBase() == TipoEnum.NIL)
                        || (a instanceof TipoStruct)
                        || (a instanceof TipoArreglo);
            }

            if (ta.getBase() == TipoEnum.NIL) {
                return (b instanceof TipoStruct)
                        || (b instanceof TipoArreglo);
            }

            return ta.getBase() == tb.getBase();
        }

        if (a instanceof TipoArreglo ta && b instanceof TipoArreglo tb) {
            return ta.getBase() == tb.getBase()
                    && ta.getDimensiones() == tb.getDimensiones();
        }

        if (a instanceof TipoStruct ta && b instanceof TipoStruct tb) {
            return ta.getNombre().equals(tb.getNombre());
        }

        if (b instanceof TipoPrimitivo tb) {
            if (tb.getBase() == TipoEnum.NIL) {
                return (a instanceof TipoStruct)
                        || (a instanceof TipoArreglo);
            }
        }

        if (a instanceof TipoPrimitivo ta) {
            if (ta.getBase() == TipoEnum.NIL) {
                return (b instanceof TipoStruct)
                        || (b instanceof TipoArreglo);
            }
        }

        return false;
    }

    @Override
    public Tipo visit(NodoAsignVar nodo) {

        String id = nodo.getIdentificador();
        Simbolo s = entornoActual.buscar(id);

        if (s == null) {
            agregarError("Variable no existe " + id, nodo.getLinea(), nodo.getColumna());
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoActual = s.getTipo();
        String op = nodo.getOperador();

        if (op != null && (op.equals("++") || op.equals("--"))) {

            if (nodo.getExpresion() != null) {
                return error("Operador " + op + " no debe tener expresión", nodo.getLinea(), nodo.getColumna());
            }

            if (!(tipoActual instanceof TipoPrimitivo tp
                    && (tp.getBase() == TipoEnum.INT || tp.getBase() == TipoEnum.FLOAT64))) {
                return error("Operador " + op + " solo aplica a números", nodo.getLinea(), nodo.getColumna());
            }

            return tipoActual;
        }

        if (nodo.getExpresion() == null) {
            return error("Expresión requerida en asignación", nodo.getLinea(), nodo.getColumna());
        }

        Tipo tipoExpr = nodo.getExpresion().accept(this);

        if (!sonCompatibles(tipoActual, tipoExpr)) {
            agregarError("Tipos incompatibles en asignación " + id, nodo.getLinea(), nodo.getColumna());
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return tipoActual;
    }

    @Override
    public Tipo visit(NodoAsignCampo nodo) {
        Simbolo s = entornoActual.buscar(nodo.getObjeto());

        if (s == null) {
            agregarError(
                    "Variable no existe: " + nodo.getObjeto(),
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoObjeto = s.getTipo();

        if (!(tipoObjeto instanceof TipoStruct ts)) {
            agregarError(
                    "La variable no es un struct",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        DefStruct def = structs.get(ts.getNombre());

        if (def == null) {
            agregarError(
                    "Struct no definido: " + ts.getNombre(),
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoCampo = def.buscarCampo(nodo.getCampo());

        if (tipoCampo == null) {
            agregarError(
                    "Campo no existe: " + nodo.getCampo(),
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoExpr = nodo.getExpresion().accept(this);

        if (!sonCompatibles(tipoCampo, tipoExpr)) {
            agregarError(
                    "Tipo incompatible en asignación de campo " + nodo.getCampo(),
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return tipoCampo;
    }

    private Tipo retornoEsperado;

    @Override
    public Tipo visit(NodoDefFuncion nodo) {

        String nombre = nodo.getNombre();

        if (funciones.containsKey(nombre)) {
            agregarError(
                    "La función '" + nombre + "' ya existe",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoRetorno = null;

        if (nodo.getTipoRetorno() != null) {
            tipoRetorno = nodo.getTipoRetorno().accept(this);
        } else if (nodo.getSliceRetorno() != null) {
            tipoRetorno = nodo.getSliceRetorno().accept(this);
        }

        if (tipoRetorno == null) {
            tipoRetorno = new TipoPrimitivo(TipoEnum.VOID);
        }

        DefFuncion def = new DefFuncion();
        def.setNombre(nombre);
        def.setRetorno(tipoRetorno);

        List<ParametroFuncion> params = new ArrayList<>();

        for (NodoParametro p : nodo.getParametros()) {

            Tipo tipoParam = (p.getTipo() != null)
                    ? p.getTipo().accept(this)
                    : p.getTipoSlice().accept(this);

            ParametroFuncion pf = new ParametroFuncion();
            pf.setNombre(p.getIdentificador());
            pf.setTipo(tipoParam);

            params.add(pf);
        }

        def.setParametros(params);

        funciones.put(nombre, def);

        Entorno anterior = entornoActual;
        entornoActual = new Entorno(anterior);

        for (ParametroFuncion p : params) {

            Simbolo s = new Simbolo();
            s.setId(p.getNombre());
            s.setTipo(p.getTipo());
            s.setValor(null);
            s.setLinea(nodo.getLinea());
            s.setColumna(nodo.getColumna());

            boolean ok = entornoActual.declarar(s.getId(), s);

            if (!ok) {
                agregarError(
                        "Parámetro duplicado: " + p.getNombre(),
                        nodo.getLinea(),
                        nodo.getColumna()
                );
            }
        }

        Tipo retornoAnterior = retornoEsperado;
        retornoEsperado = tipoRetorno;

        nodo.getBloque().accept(this);

        retornoEsperado = retornoAnterior;
        entornoActual = anterior;

        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoDefFuncionStruct nodo) {

        String structName = nodo.getStructReceptor();
        String varReceiver = nodo.getVariableReceptor();
        String funcName = nodo.getNombre();

        DefStruct defStruct = structs.get(structName);

        if (defStruct == null) {
            agregarError(
                    "El struct '" + structName + "' no existe",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        String fullName = structName + "." + funcName;

        if (funciones.containsKey(fullName)) {
            agregarError(
                    "El método '" + funcName + "' ya existe en struct " + structName,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoRetorno;

        if (nodo.getT() != null) {
            tipoRetorno = nodo.getT().accept(this);
        } else if (nodo.getTs() != null) {
            tipoRetorno = nodo.getTs().accept(this);
        } else {
            tipoRetorno = new TipoPrimitivo(TipoEnum.VOID);
        }

        DefFuncion def = new DefFuncion();
        def.setNombre(fullName);
        def.setRetorno(tipoRetorno);

        List<ParametroFuncion> params = new ArrayList<>();

        ParametroFuncion receptor = new ParametroFuncion();
        receptor.setNombre(varReceiver);
        receptor.setTipo(new TipoStruct(structName));
        params.add(receptor);

        for (NodoParametro p : nodo.getPs()) {

            Tipo t = (p.getTipo() != null)
                    ? p.getTipo().accept(this)
                    : p.getTipoSlice().accept(this);

            ParametroFuncion pf = new ParametroFuncion();
            pf.setNombre(p.getIdentificador());
            pf.setTipo(t);

            params.add(pf);
        }

        def.setParametros(params);

        funciones.put(fullName, def);

        Entorno anterior = entornoActual;
        entornoActual = new Entorno(anterior);

        Simbolo s = new Simbolo();
        s.setId(varReceiver);
        s.setTipo(new TipoStruct(structName));
        s.setValor(null);
        entornoActual.insertar(s.getId(), s);

        for (ParametroFuncion p : params) {
            if (p.getNombre().equals(varReceiver)) {
                continue;
            }

            Simbolo sp = new Simbolo();
            sp.setId(p.getNombre());
            sp.setTipo(p.getTipo());
            sp.setValor(null);

            entornoActual.insertar(sp.getId(), sp);
        }

        Tipo retornoAnterior = retornoEsperado;
        retornoEsperado = tipoRetorno;

        nodo.getB().accept(this);

        retornoEsperado = retornoAnterior;
        entornoActual = anterior;

        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoDefStruct nodo) {
        String nombre = nodo.getNombreStruct();

        if (structs.containsKey(nombre)) {
            agregarError(
                    "El struct '" + nombre + "' ya existe",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        DefStruct def = new DefStruct(nombre);
        structs.put(nombre, def);

        for (NodoAtributoStruct attr : nodo.getAtributos()) {

            String idCampo = attr.getIdentificador();

            if (def.existeCampo(idCampo)) {
                agregarError(
                        "Campo duplicado: " + idCampo,
                        attr.getLinea(),
                        attr.getColumna()
                );
                continue;
            }

            Tipo tipoCampo;

            if (attr.getTipo() != null) {
                tipoCampo = attr.getTipo().accept(this);
            } else {
                tipoCampo = attr.getTipoSlice().accept(this);
            }

            if (tipoCampo == null) {
                agregarError(
                        "Tipo inválido en campo '" + idCampo + "'",
                        attr.getLinea(),
                        attr.getColumna()
                );
                continue;
            }

            def.agregarCampo(idCampo, tipoCampo);
        }

        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoBloque nodo) {

        Entorno anterior = entornoActual;
        entornoActual = new Entorno(anterior);

        for (NodoInstruccion inst : nodo.getInstrucciones()) {
            try {
                inst.accept(this);
            } catch (Exception e) {
                e.printStackTrace();
                agregarError("Error en instrucción", inst.getLinea(), inst.getColumna());
            }
        }

        entornoActual = anterior;

        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoIf nodo) {

        Tipo tipoCond = nodo.getCondicion().accept(this);

        if (!(tipoCond instanceof TipoPrimitivo tp
                && tp.getBase() == TipoEnum.BOOL)) {

            agregarError(
                    "La condición del if debe ser tipo bool",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
        }

        nodo.getBloqueThen().accept(this);

        if (nodo.getBloqueElse() != null) {
            nodo.getBloqueElse().accept(this);
        }

        if (nodo.getElseIfSiguiente() != null) {
            nodo.getElseIfSiguiente().accept(this);
        }

        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoFor nodo) {

        Entorno anterior = entornoActual;
        entornoActual = new Entorno(anterior);

        if (!nodo.isEsRange()) {

            if (nodo.getInit() != null) {
                nodo.getInit().accept(this);
            }

            if (nodo.getCondicion() != null) {

                Tipo tCond = nodo.getCondicion().accept(this);

                if (!(tCond instanceof TipoPrimitivo tp
                        && tp.getBase() == TipoEnum.BOOL)) {

                    agregarError(
                            "La condición del for debe ser bool",
                            nodo.getLinea(),
                            nodo.getColumna()
                    );
                }
            }

            if (nodo.getPost() != null) {
                nodo.getPost().accept(this);
            }
        } else {

            Tipo tipoIterable = nodo.getIterable().accept(this);

            if (!(tipoIterable instanceof TipoArreglo
                    || (tipoIterable instanceof TipoPrimitivo tp2
                    && tp2.getBase() == TipoEnum.STRING))) {

                agregarError(
                        "El valor en range debe ser slice o string",
                        nodo.getLinea(),
                        nodo.getColumna()
                );
            }

            if (nodo.getRangeIdx() != null) {

                Simbolo idx = new Simbolo();
                idx.setId(nodo.getRangeIdx());
                idx.setTipo(new TipoPrimitivo(TipoEnum.INT));
                idx.setValor(null);
                idx.setLinea(nodo.getLinea());
                idx.setColumna(nodo.getColumna());

                entornoActual.insertar(idx.getId(), idx);
            }

            if (nodo.getRangeVal() != null) {

                Tipo tipoVal;

                if (tipoIterable instanceof TipoArreglo arr) {
                    tipoVal = new TipoPrimitivo(arr.getBase());
                } else {
                    tipoVal = new TipoPrimitivo(TipoEnum.RUNE);
                }

                Simbolo val = new Simbolo();
                val.setId(nodo.getRangeVal());
                val.setTipo(tipoVal);
                val.setValor(null);
                val.setLinea(nodo.getLinea());
                val.setColumna(nodo.getColumna());

                entornoActual.insertar(val.getId(), val);
            }
        }

        nodo.getBloque().accept(this);

        entornoActual = anterior;

        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoSwitch nodo) {

        Tipo tipoSwitch = nodo.getExpresion().accept(this);

        for (NodoCaso caso : nodo.getCasos()) {

            Tipo tipoCaso = caso.getExpresion().accept(this);

            if (!sonCompatibles(tipoSwitch, tipoCaso)) {
                agregarError(
                        "Tipo incompatible en switch case",
                        caso.getLinea(),
                        caso.getColumna()
                );
            }

            for (NodoInstruccion i : caso.getInstrucciones()) {
                i.accept(this);
            }
        }

        if (nodo.getCasoDefault() != null) {
            for (NodoInstruccion i : nodo.getCasoDefault().getInstrucciones()) {
                i.accept(this);
            }
        }

        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoReturn nodo) {

        if (retornoEsperado == null) {
            agregarError(
                    "return fuera de función",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoEsperado = retornoEsperado;

        if (nodo.getExpresion() == null) {

            if (!(tipoEsperado instanceof TipoPrimitivo tp
                    && tp.getBase() == TipoEnum.VOID)) {

                agregarError(
                        "Se esperaba un valor de retorno",
                        nodo.getLinea(),
                        nodo.getColumna()
                );
                return new TipoPrimitivo(TipoEnum.ERROR);
            }

            return tipoEsperado;
        }

        Tipo tipoExpr = nodo.getExpresion().accept(this);

        if (!sonCompatibles(tipoEsperado, tipoExpr)) {

            agregarError(
                    "Tipo de retorno incompatible.",
                    nodo.getLinea(),
                    nodo.getColumna()
            );

            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return tipoEsperado;
    }

    @Override
    public Tipo visit(NodoBreak nodo) {
        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoContinue nodo) {
        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoBinaria nodo) {

        Tipo izq = nodo.getIzquierdo().accept(this);
        Tipo der = nodo.getDerecho().accept(this);
        String op = nodo.getOperador();

        if (izq instanceof TipoPrimitivo ti && der instanceof TipoPrimitivo td) {

            TipoEnum a = ti.getBase();
            TipoEnum b = td.getBase();

            if (op.equals("&&") || op.equals("||")) {

                if (a == TipoEnum.BOOL && b == TipoEnum.BOOL) {
                    return new TipoPrimitivo(TipoEnum.BOOL);
                }

                return error("Operadores lógicos requieren bool", nodo.getLinea(), nodo.getColumna());
            }

            if (op.equals("==") || op.equals("!=")) {

                if (sonCompatibles(izq, der)) {
                    return new TipoPrimitivo(TipoEnum.BOOL);
                }

                return error("Comparación inválida", nodo.getLinea(), nodo.getColumna());
            }

            if (op.equals("<") || op.equals(">")
                    || op.equals("<=") || op.equals(">=")) {

                if (esNumerico(a) && esNumerico(b)) {
                    return new TipoPrimitivo(TipoEnum.BOOL);
                }

                if (a == TipoEnum.RUNE && b == TipoEnum.RUNE) {
                    return new TipoPrimitivo(TipoEnum.BOOL);
                }

                return error("Operadores relacionales inválidos", nodo.getLinea(), nodo.getColumna());
            }

            if (op.equals("+") || op.equals("-")
                    || op.equals("*") || op.equals("/") || op.equals("%")) {

                return resolverAritmetica(a, b, op, nodo);
            }
        }

        return error("Operación no soportada", nodo.getLinea(), nodo.getColumna());
    }

    private Tipo resolverAritmetica(TipoEnum a, TipoEnum b, String op, NodoBinaria nodo) {

        if (op.equals("+")) {

            if (a == TipoEnum.STRING && b == TipoEnum.STRING) {
                return new TipoPrimitivo(TipoEnum.STRING);
            }

            if (esNumerico(a) && esNumerico(b)) {
                return promover(a, b);
            }

            return error("Suma inválida", nodo.getLinea(), nodo.getColumna());
        }

        if (op.equals("-") || op.equals("*") || op.equals("/")) {

            if (esNumerico(a) && esNumerico(b)) {
                return promover(a, b);
            }

            return error("Operación aritmética inválida", nodo.getLinea(), nodo.getColumna());
        }

        if (op.equals("%")) {

            if (a == TipoEnum.INT && b == TipoEnum.INT) {
                return new TipoPrimitivo(TipoEnum.INT);
            }

            return error("Módulo solo permite int", nodo.getLinea(), nodo.getColumna());
        }

        return error("Operador inválido", nodo.getLinea(), nodo.getColumna());
    }

    private Tipo promover(TipoEnum a, TipoEnum b) {

        if (a == TipoEnum.FLOAT64 || b == TipoEnum.FLOAT64) {
            return new TipoPrimitivo(TipoEnum.FLOAT64);
        }

        return new TipoPrimitivo(TipoEnum.INT);
    }

    private boolean esNumerico(TipoEnum t) {
        return t == TipoEnum.INT || t == TipoEnum.FLOAT64;
    }

    private Tipo error(String msg, int linea, int columna) {
        agregarError(msg, linea, columna);
        return new TipoPrimitivo(TipoEnum.ERROR);
    }

    @Override
    public Tipo visit(NodoUnaria nodo) {

        Tipo tipo = nodo.getExpresion().accept(this);
        String op = nodo.getOperador();

        if (!(tipo instanceof TipoPrimitivo t)) {
            return error("Operación unaria inválida", nodo.getLinea(), nodo.getColumna());
        }

        TipoEnum base = t.getBase();

        if (op.equals("!")) {

            if (base == TipoEnum.BOOL) {
                return new TipoPrimitivo(TipoEnum.BOOL);
            }

            return error("El operador ! solo aplica a bool", nodo.getLinea(), nodo.getColumna());
        }

        if (op.equals("-")) {

            if (base == TipoEnum.INT || base == TipoEnum.FLOAT64) {
                return new TipoPrimitivo(base);
            }

            return error("El operador - unario solo aplica a números", nodo.getLinea(), nodo.getColumna());
        }

        return error("Operador unario no soportado: " + op, nodo.getLinea(), nodo.getColumna());
    }

    @Override
    public Tipo visit(NodoAgrupacion nodo) {
        return nodo.getExpresion().accept(this);
    }

    @Override
    public Tipo visit(NodoLiteral nodo) {

        return switch (nodo.getTipoLiteral()) {
            case "int" ->
                new TipoPrimitivo(TipoEnum.INT);
            case "float64" ->
                new TipoPrimitivo(TipoEnum.FLOAT64);
            case "string" ->
                new TipoPrimitivo(TipoEnum.STRING);
            case "bool" ->
                new TipoPrimitivo(TipoEnum.BOOL);
            case "rune" ->
                new TipoPrimitivo(TipoEnum.RUNE);
            case "nil" ->
                new TipoPrimitivo(TipoEnum.NIL);
            default ->
                new TipoPrimitivo(TipoEnum.ERROR);
        };
    }

    @Override
    public Tipo visit(NodoIdentificador nodo) {

        Simbolo s = entornoActual.buscar(nodo.getNombre());

        if (s == null) {
            agregarError(
                    "Variable no existe: " + nodo.getNombre(),
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return s.getTipo();
    }

    @Override
    public Tipo visit(NodoLlamadaFuncion nodo) {

        String id = nodo.getIdentificador();

        if (id.contains(".")) {
            int punto = id.indexOf('.');
            String nombreObj = id.substring(0, punto);
            String nombreMetodo = id.substring(punto + 1);

            Simbolo s = entornoActual.buscar(nombreObj);

            if (s == null) {
                agregarError(
                        "La variable no existe: " + nombreObj,
                        nodo.getLinea(),
                        nodo.getColumna()
                );
                return new TipoPrimitivo(TipoEnum.ERROR);
            }

            if (!(s.getTipo() instanceof TipoStruct ts)) {
                agregarError(
                        "La variable no es un struct: " + nombreObj,
                        nodo.getLinea(),
                        nodo.getColumna()
                );
                return new TipoPrimitivo(TipoEnum.ERROR);
            }

            String key = ts.getNombre() + "." + nombreMetodo;
            DefFuncion def = funciones.get(key);

            if (def == null) {
                agregarError(
                        "El método no existe: " + nombreMetodo
                        + " en struct " + ts.getNombre(),
                        nodo.getLinea(),
                        nodo.getColumna()
                );
                return new TipoPrimitivo(TipoEnum.ERROR);
            }

            List<ParametroFuncion> params = def.getParametros();
            List<NodoExpresion> args = nodo.getArgumentos();

            if (params.size() - 1 != args.size()) {
                agregarError(
                        "Cantidad de parámetros incorrecta en método " + nombreMetodo,
                        nodo.getLinea(),
                        nodo.getColumna()
                );
                return def.getRetorno();
            }

            for (int i = 0; i < args.size(); i++) {
                Tipo tipoParam = params.get(i + 1).getTipo();
                Tipo tipoArg = args.get(i).accept(this);

                if (!sonCompatibles(tipoParam, tipoArg)) {
                    agregarError(
                            "Parámetro inválido en método " + nombreMetodo,
                            nodo.getLinea(),
                            nodo.getColumna()
                    );
                }
            }

            return def.getRetorno();
        }

        DefFuncion def = funciones.get(id);

        if (def == null) {
            agregarError(
                    "La función no existe: " + id,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        List<ParametroFuncion> params = def.getParametros();
        List<NodoExpresion> args = nodo.getArgumentos();

        if (params.size() != args.size()) {
            agregarError(
                    "Cantidad de parámetros incorrecta en " + id,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return def.getRetorno();
        }

        for (int i = 0; i < params.size(); i++) {

            Tipo tipoParam = params.get(i).getTipo();
            Tipo tipoArg = args.get(i).accept(this);

            if (!sonCompatibles(tipoParam, tipoArg)) {

                agregarError(
                        "Tipo incompatible en parámetro "
                        + params.get(i).getNombre()
                        + " de la función " + id,
                        nodo.getLinea(),
                        nodo.getColumna()
                );
            }
        }

        return def.getRetorno();
    }

    @Override
    public Tipo visit(NodoLlamadaMetodo nodo) {

        String nombreObj = nodo.getObjeto();

        Simbolo s = entornoActual.buscar(nombreObj);

        if (s == null) {
            agregarError(
                    "El objeto no existe: " + nombreObj,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoObj = s.getTipo();

        if (!(tipoObj instanceof TipoStruct ts)) {
            agregarError(
                    "La variable no es un struct: " + nombreObj,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        String key = ts.getNombre() + "." + nodo.getMetodo();

        DefFuncion def = funciones.get(key);

        if (def == null) {
            agregarError(
                    "El método no existe: " + nodo.getMetodo()
                    + " en struct " + ts.getNombre(),
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        List<ParametroFuncion> params = def.getParametros();
        List<NodoExpresion> args = nodo.getArgumentos();

        if (params.size() - 1 != args.size()) {
            agregarError(
                    "Cantidad de parámetros incorrecta en método "
                    + nodo.getMetodo(),
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return def.getRetorno();
        }

        for (int i = 0; i < args.size(); i++) {
            Tipo tipoParam = params.get(i + 1).getTipo();
            Tipo tipoArg = args.get(i).accept(this);

            if (!sonCompatibles(tipoParam, tipoArg)) {
                agregarError(
                        "Parámetro inválido en método "
                        + nodo.getMetodo(),
                        nodo.getLinea(),
                        nodo.getColumna()
                );
            }
        }

        return def.getRetorno();
    }

    @Override
    public Tipo visit(NodoAccesoCampo nodo) {

        String nombreObj = nodo.getObjeto();

        Simbolo s = entornoActual.buscar(nombreObj);

        if (s == null) {
            agregarError(
                    "La variable no existe: " + nombreObj,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoObj = s.getTipo();

        if (!(tipoObj instanceof TipoStruct ts)) {
            agregarError(
                    "La variable no es un struct: " + nombreObj,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        String nombreStruct = ts.getNombre();

        DefStruct def = structs.get(nombreStruct);

        if (def == null) {
            agregarError(
                    "Struct no definido: " + nombreStruct,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoCampo = def.buscarCampo(nodo.getCampo());

        if (tipoCampo == null) {
            agregarError(
                    "Campo no existe: " + nodo.getCampo()
                    + " en struct " + nombreStruct,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return tipoCampo;
    }

    @Override
    public Tipo visit(NodoAccesoSlice nodo) {

        String id = nodo.getIdentificador();

        Simbolo s = entornoActual.buscar(id);

        if (s == null) {
            agregarError(
                    "La variable no existe: " + id,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoVar = s.getTipo();

        if (!(tipoVar instanceof TipoArreglo arr)) {
            agregarError(
                    "La variable no es un arreglo: " + id,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo t1 = nodo.getIndice1().accept(this);

        if (!(t1 instanceof TipoPrimitivo tp1 && tp1.getBase() == TipoEnum.INT)) {
            agregarError(
                    "El índice debe ser int",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        if (nodo.getIndice2() != null) {

            Tipo t2 = nodo.getIndice2().accept(this);

            if (!(t2 instanceof TipoPrimitivo tp2 && tp2.getBase() == TipoEnum.INT)) {
                agregarError(
                        "El segundo índice debe ser int",
                        nodo.getLinea(),
                        nodo.getColumna()
                );
                return new TipoPrimitivo(TipoEnum.ERROR);
            }

            return new TipoPrimitivo(arr.getBase());
        }

        // Acceso con un solo índice
        if (arr.getDimensiones() == 2) {
            // arr[i] en una matriz devuelve una fila
            return new TipoArreglo(arr.getBase(), 1);
        }

        return new TipoPrimitivo(arr.getBase());
    }

    @Override
    public Tipo visit(NodoSlicesIndex nodo) {

        Tipo tipoSlice = nodo.getSlice().accept(this);

        if (!(tipoSlice instanceof TipoArreglo arr)) {
            agregarError(
                    "slices.Index solo se aplica a arreglos/slices",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoValor = nodo.getValor().accept(this);
        Tipo tipoElemento = new TipoPrimitivo(arr.getBase());

        if (!sonCompatibles(tipoElemento, tipoValor)) {
            agregarError(
                    "Tipo incompatible en slices.Index",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return new TipoPrimitivo(TipoEnum.INT);
    }

    @Override
    public Tipo visit(NodoInstanciaStruct nodo) {

        String nombreTipo = nodo.getTipo();
        String nombreVar = nodo.getNombreStruct();

        DefStruct def = structs.get(nombreTipo);

        if (def == null) {
            agregarError(
                    "El struct no existe: " + nombreTipo,
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        TipoStruct tipoStruct = new TipoStruct(nombreTipo);

        for (NodoCampoStruct campoNodo : nodo.getCampos()) {

            String nombreCampo = campoNodo.getCampo();
            Tipo tipoEsperado = def.buscarCampo(nombreCampo);

            if (tipoEsperado == null) {
                agregarError(
                        "Campo no existe en struct " + nombreTipo + ": " + nombreCampo,
                        nodo.getLinea(),
                        nodo.getColumna()
                );
                continue;
            }

            Tipo tipoValor = campoNodo.getExpresion().accept(this);

            if (!sonCompatibles(tipoEsperado, tipoValor)) {
                agregarError(
                        "Tipo incompatible en campo " + nombreCampo,
                        nodo.getLinea(),
                        nodo.getColumna()
                );
            }
        }

        Simbolo s = new Simbolo();
        s.setId(nombreVar);
        s.setTipo(tipoStruct);
        s.setLinea(nodo.getLinea());
        s.setColumna(nodo.getColumna());

        entornoActual.declarar(nombreVar, s);

        return tipoStruct;
    }

    @Override
    public Tipo visit(NodoLiteralSlice nodo) {

        if (nodo.getDimensiones() == 1) {

            if (nodo.getTipoBase() == null) {
                // Inferir tipo desde los elementos
                TipoEnum tipoInferido = TipoEnum.INT; // defecto

                for (NodoExpresion expr : nodo.getExpresiones()) {
                    Tipo t = expr.accept(this);
                    if (t instanceof TipoPrimitivo tp) {
                        tipoInferido = tp.getBase();
                        break;
                    }
                }

                for (NodoExpresion expr : nodo.getExpresiones()) {
                    Tipo t = expr.accept(this);
                    if (!sonCompatibles(new TipoPrimitivo(tipoInferido), t)) {
                        agregarError(
                                "Tipo incompatible en literal de slice",
                                nodo.getLinea(),
                                nodo.getColumna()
                        );
                    }
                }

                return new TipoArreglo(tipoInferido, 1);
            }

            Tipo tipoBase = nodo.getTipoBase().accept(this);

            if (!(tipoBase instanceof TipoPrimitivo tpBase)) {
                agregarError(
                        "Tipo base inválido en slice literal",
                        nodo.getLinea(),
                        nodo.getColumna()
                );
                return new TipoPrimitivo(TipoEnum.ERROR);
            }

            TipoEnum base = tpBase.getBase();

            for (NodoExpresion expr : nodo.getExpresiones()) {
                Tipo t = expr.accept(this);
                if (!sonCompatibles(new TipoPrimitivo(base), t)) {
                    agregarError(
                            "Tipo incompatible en literal de slice",
                            nodo.getLinea(),
                            nodo.getColumna()
                    );
                }
            }

            return new TipoArreglo(base, 1);
        }

        TipoEnum base = TipoEnum.INT; // defecto

        if (nodo.getTipoBase() != null) {
            Tipo tipoBase = nodo.getTipoBase().accept(this);
            if (tipoBase instanceof TipoPrimitivo tp) {
                base = tp.getBase();
            }
        }

        for (NodoLiteralSlice fila : nodo.getFilas()) {
            Tipo t = fila.accept(this);

            if (!(t instanceof TipoArreglo arr)) {
                agregarError(
                        "Fila inválida en slice 2D",
                        nodo.getLinea(),
                        nodo.getColumna()
                );
                return new TipoPrimitivo(TipoEnum.ERROR);
            }

            if (arr.getBase() != base) {
                agregarError(
                        "Tipos incompatibles en matriz",
                        nodo.getLinea(),
                        nodo.getColumna()
                );
            }
        }

        return new TipoArreglo(base, 2);
    }

    @Override
    public Tipo visit(NodoLen nodo) {

        Tipo tipoExpr = nodo.getExpresion().accept(this);

        if (tipoExpr instanceof TipoArreglo) {
            return new TipoPrimitivo(TipoEnum.INT);
        }

        if (tipoExpr instanceof TipoPrimitivo tp
                && tp.getBase() == TipoEnum.STRING) {
            return new TipoPrimitivo(TipoEnum.INT);
        }

        agregarError(
                "len solo puede aplicarse a slices o strings",
                nodo.getLinea(),
                nodo.getColumna()
        );

        return new TipoPrimitivo(TipoEnum.ERROR);
    }

    @Override
    public Tipo visit(NodoAppend nodo) {

        Tipo tipoSlice = nodo.getSlice().accept(this);

        if (!(tipoSlice instanceof TipoArreglo arr)) {
            agregarError(
                    "append solo puede aplicarse a slices",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoValor = nodo.getValor().accept(this);

        if (arr.getDimensiones() == 2) {
            if (!(tipoValor instanceof TipoArreglo arrVal
                    && arrVal.getBase() == arr.getBase()
                    && arrVal.getDimensiones() == 1)) {
                agregarError(
                        "El valor agregado a la matriz debe ser un slice 1D del mismo tipo",
                        nodo.getLinea(),
                        nodo.getColumna()
                );
            }
            return tipoSlice;
        }

        // Append en slice 1D normal
        Tipo tipoElemento = new TipoPrimitivo(arr.getBase());

        if (!sonCompatibles(tipoElemento, tipoValor)) {
            agregarError(
                    "El valor agregado no coincide con el tipo del slice",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return tipoSlice;
    }

    @Override
    public Tipo visit(NodoAtoi nodo) {

        Tipo tipoExpr = nodo.getExpresion().accept(this);

        if (!(tipoExpr instanceof TipoPrimitivo tp
                && tp.getBase() == TipoEnum.STRING)) {

            agregarError(
                    "atoi requiere un argumento de tipo string",
                    nodo.getLinea(),
                    nodo.getColumna()
            );

            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return new TipoPrimitivo(TipoEnum.INT);
    }

    @Override
    public Tipo visit(NodoParsefloat nodo) {

        Tipo tipoExpr = nodo.getExpresion().accept(this);

        if (!(tipoExpr instanceof TipoPrimitivo tp
                && tp.getBase() == TipoEnum.STRING)) {

            agregarError(
                    "parseFloat requiere un argumento de tipo string",
                    nodo.getLinea(),
                    nodo.getColumna()
            );

            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return new TipoPrimitivo(TipoEnum.FLOAT64);
    }

    @Override
    public Tipo visit(NodoTypeof nodo) {

        Tipo t = nodo.getExpresion().accept(this);

        if (t instanceof TipoPrimitivo tp
                && tp.getBase() == TipoEnum.ERROR) {
            return t;
        }

        return new TipoPrimitivo(TipoEnum.STRING);
    }

    @Override
    public Tipo visit(NodoStringsJoin nodo) {

        Tipo tipoSlice = nodo.getSlice().accept(this);

        if (!(tipoSlice instanceof TipoArreglo arr
                && arr.getDimensiones() == 1
                && arr.getBase() == TipoEnum.STRING)) {

            agregarError(
                    "strings.Join requiere un slice de string",
                    nodo.getLinea(),
                    nodo.getColumna()
            );

            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        Tipo tipoSep = nodo.getSeparador().accept(this);

        if (!(tipoSep instanceof TipoPrimitivo tp
                && tp.getBase() == TipoEnum.STRING)) {

            agregarError(
                    "El separador de strings.Join debe ser string",
                    nodo.getLinea(),
                    nodo.getColumna()
            );

            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return new TipoPrimitivo(TipoEnum.STRING);
    }

    @Override
    public Tipo visit(NodoPrintln nodo) {
        for (NodoExpresion arg : nodo.getArgumentos()) {
            arg.accept(this);
        }
        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoParametro nodo) {

        if (nodo.getTipo() != null) {
            return nodo.getTipo().accept(this);
        }

        if (nodo.getTipoSlice() != null) {
            return nodo.getTipoSlice().accept(this);
        }

        agregarError(
                "Tipo inválido en parámetro: " + nodo.getIdentificador(),
                nodo.getLinea(),
                nodo.getColumna()
        );

        return new TipoPrimitivo(TipoEnum.ERROR);
    }

    @Override
    public Tipo visit(NodoCampoStruct nodo) {

        if (nodo.getExpresion() == null) {
            agregarError(
                    "Campo de struct sin expresión",
                    nodo.getLinea(),
                    nodo.getColumna()
            );
            return new TipoPrimitivo(TipoEnum.ERROR);
        }

        return nodo.getExpresion().accept(this);
    }

    @Override
    public Tipo visit(NodoAtributoStruct nodo) {

        if (nodo.getTipo() != null) {
            return nodo.getTipo().accept(this);
        }

        if (nodo.getTipoSlice() != null) {
            return nodo.getTipoSlice().accept(this);
        }

        agregarError(
                "Tipo inválido en atributo de struct: " + nodo.getIdentificador(),
                nodo.getLinea(),
                nodo.getColumna()
        );

        return new TipoPrimitivo(TipoEnum.ERROR);
    }

    @Override
    public Tipo visit(NodoCaso nodo) {

        Tipo tipoExp = nodo.getExpresion().accept(this);

        if (tipoExp instanceof TipoPrimitivo tp
                && tp.getBase() == TipoEnum.ERROR) {
            return tp;
        }

        for (NodoInstruccion ins : nodo.getInstrucciones()) {
            ins.accept(this);
        }

        return tipoExp;
    }

    @Override
    public Tipo visit(NodoCasoDefault nodo) {

        for (NodoInstruccion ins : nodo.getInstrucciones()) {
            ins.accept(this);
        }

        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Tipo visit(NodoTipo nodo) {

        String nombre = nodo.getNombreTipo();

        return switch (nombre) {

            case "int" ->
                new TipoPrimitivo(TipoEnum.INT);

            case "string" ->
                new TipoPrimitivo(TipoEnum.STRING);

            case "bool" ->
                new TipoPrimitivo(TipoEnum.BOOL);

            case "float64" ->
                new TipoPrimitivo(TipoEnum.FLOAT64);

            case "rune" ->
                new TipoPrimitivo(TipoEnum.RUNE);

            default -> {

                if (structs.containsKey(nombre)) {
                    yield new TipoStruct(nombre);
                }

                agregarError(
                        "Tipo no definido: " + nombre,
                        nodo.getLinea(),
                        nodo.getColumna()
                );

                yield new TipoPrimitivo(TipoEnum.ERROR);
            }
        };
    }

    @Override
    public Tipo visit(NodoTipoSlice nodo) {

        Tipo base = nodo.getTipoBase().accept(this);

        if (base instanceof TipoPrimitivo tp
                && tp.getBase() == TipoEnum.ERROR) {
            return tp;
        }

        if (!(base instanceof TipoPrimitivo tpBase)) {
            return new TipoArreglo(TipoEnum.ERROR, nodo.getDimensiones());
        }

        return new TipoArreglo(tpBase.getBase(), nodo.getDimensiones());
    }

    @Override
    public Tipo visit(NodoError nodo) {
        return new TipoPrimitivo(TipoEnum.ERROR);
    }

    public List<String[]> getErrores() {
        return errores;
    }

    private void agregarError(String msg, int ln, int col) {
        errores.add(new String[]{msg, String.valueOf(ln), String.valueOf(col)});
    }

    private String structsToString() {
        StringBuilder sb = new StringBuilder();

        sb.append(" STRUCTS \n");

        if (structs.isEmpty()) {
            sb.append("(vacío)\n");
            return sb.toString();
        }

        for (Map.Entry<String, DefStruct> entry : structs.entrySet()) {
            sb.append(entry.getKey())
                    .append(" -> ")
                    .append(entry.getValue())
                    .append("\n");
        }

        return sb.toString();
    }

}