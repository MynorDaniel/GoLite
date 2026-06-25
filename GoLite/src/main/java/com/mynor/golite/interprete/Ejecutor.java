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

    @Override
    public Object visit(NodoPrograma nodo) {

        Entorno global = new Entorno(null);
        Entorno anterior = entornoActual;
        entornoActual = global;

        for (NodoDeclaracionGlobal decl : nodo.getGlobales()) {
            decl.accept(this);
        }

        DefFuncion main = funciones.get("main");

        if (main == null) {
            throw new RuntimeException("No se encontró la función main");
        }

        ejecutarFuncion(main, new ArrayList<>());

        entornoActual = anterior;
        return null;
    }

    private Object ejecutarFuncion(DefFuncion f, List<Object> args) {

        callStack.push(entornoActual);
        entornoActual = new Entorno(entornoActual);

        try {

            for (int i = 0; i < f.getParametros().size(); i++) {

                ParametroFuncion p = f.getParametros().get(i);

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

    @Override
    public Object visit(NodoDeclVar nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoAsignVar nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoAsignCampo nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoDefFuncion nodo) {

        DefFuncion def = new DefFuncion();

        def.setNombre(nodo.getNombre());

        List<ParametroFuncion> params = new ArrayList<>();

        for (NodoParametro p : nodo.getParametros()) {

            ParametroFuncion pf = new ParametroFuncion();

            pf.setNombre(p.getIdentificador());

            Tipo t;
            if (p.getTipo() != null) {
                t = (Tipo) p.getTipo().accept(this);
            } else {
                t = (Tipo) p.getTipoSlice().accept(this);
            }

            pf.setTipo(t);

            params.add(pf);
        }

        def.setParametros(params);

        if (nodo.getTipoRetorno() != null) {
            def.setRetorno((Tipo) nodo.getTipoRetorno().accept(this));
        } else {
            def.setRetorno(new TipoPrimitivo(TipoEnum.VOID));
        }

        def.setCuerpo(nodo.getBloque());

        funciones.put(def.getNombre(), def);

        return null;
    }

    @Override
    public Object visit(NodoDefFuncionStruct nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoDefStruct nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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

                    if (!((Boolean) cond)) {
                        break;
                    }
                }

                Object r = nodo.getBloque().accept(this);

                if (r instanceof BreakControl) {
                    break;
                }

                if (r instanceof ContinueControl) {
                    if (nodo.getPost() != null) {
                        nodo.getPost().accept(this);
                    }
                    continue;
                }

                if (r instanceof ReturnControl) {
                    entornoActual = anterior;
                    return r;
                }

                if (nodo.getPost() != null) {
                    nodo.getPost().accept(this);
                }
            }

        } else {

            Object iterable = nodo.getIterable().accept(this);

            if (iterable instanceof List<?> list) {

                int idx = 0;

                for (Object val : list) {

                    if (nodo.getRangeIdx() != null) {
                        Simbolo s1 = new Simbolo();
                        s1.setId(nodo.getRangeIdx());
                        s1.setTipo(new TipoPrimitivo(TipoEnum.INT));
                        s1.setValor(idx);
                        entornoActual.insertar(s1.getId(), s1);
                    }

                    if (nodo.getRangeVal() != null) {
                        Simbolo s2 = new Simbolo();
                        s2.setId(nodo.getRangeVal());
                        s2.setTipo(inferirTipo(val));
                        s2.setValor(val);
                        entornoActual.insertar(s2.getId(), s2);
                    }

                    Object r = nodo.getBloque().accept(this);

                    if (r instanceof BreakControl) {
                        break;
                    }

                    if (r instanceof ContinueControl) {
                        idx++;
                        continue;
                    }

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

    private Tipo inferirTipo(Object val) {
        if (val instanceof Integer) {
            return new TipoPrimitivo(TipoEnum.INT);
        }
        if (val instanceof Double) {
            return new TipoPrimitivo(TipoEnum.FLOAT64);
        }
        if (val instanceof Boolean) {
            return new TipoPrimitivo(TipoEnum.BOOL);
        }
        if (val instanceof String) {
            return new TipoPrimitivo(TipoEnum.STRING);
        }
        return new TipoPrimitivo(TipoEnum.VOID);
    }

    @Override
    public Object visit(NodoSwitch nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoUnaria nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoAgrupacion nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoLiteral nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoIdentificador nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoLlamadaFuncion nodo) {
        System.out.println("Llamada a funcion " + nodo.getIdentificador());
        return null;
    }

    @Override
    public Object visit(NodoLlamadaMetodo nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoAccesoCampo nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoAccesoSlice nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoSlicesIndex nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoInstanciaStruct nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoLiteralSlice nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoLen nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoAppend nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoAtoi nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoParsefloat nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoTypeof nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoStringsJoin nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoPrintln nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoParametro nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoCampoStruct nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoAtributoStruct nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoCaso nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoCasoDefault nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoTipo nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoTipoSlice nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visit(NodoError nodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
