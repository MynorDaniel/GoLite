/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mynor.golite.graphviz;

import com.mynor.golite.ast.*;
import com.mynor.golite.interprete.Visitor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author mynordma
 */
public class ManejadorGraphviz implements Visitor<String> {

    private final StringBuilder dot = new StringBuilder();
    private int contador = 0;

    public String generarImagenAST(NodoPrograma nodo, String rutaAbsoluta) {

        String msg = "";

        dot.setLength(0);
        contador = 0;

        dot.append("digraph AST {\n");
        dot.append("    graph [rankdir=TB, fontname=\"Helvetica\"];\n");
        dot.append("    node  [shape=box, style=\"filled,rounded\", ")
                .append("fillcolor=\"#dbe9f4\", fontname=\"Helvetica\", fontsize=11];\n");
        dot.append("    edge  [fontname=\"Helvetica\", fontsize=9];\n\n");

        nodo.accept(this);

        dot.append("}\n");

        String archivoDot = rutaAbsoluta + ".dot";
        String archivoPng = rutaAbsoluta + ".png";

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoDot))) {
            pw.print(dot.toString());
        } catch (IOException e) {
            System.err.println("Error al escribir .dot: " + e.getMessage());
            msg = "Error al escribir .dot: " + e.getMessage();
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", archivoDot, "-o", archivoPng);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            int exit = p.waitFor();
            if (exit == 0) {
                msg = "AST generado: " + archivoPng;
                System.out.println("AST generado: " + archivoPng);
            } else {
                msg = "dot terminó con código " + exit;
                System.err.println("dot terminó con código " + exit);
            }
        } catch (IOException | InterruptedException e) {
            msg = "Error al ejecutar dot: " + e.getMessage();
            System.err.println("Error al ejecutar dot: " + e.getMessage());
        }

        return msg;
    }

    private String nodo(String etiqueta) {
        String id = "n" + (contador++);
        String label = etiqueta
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
        dot.append("    ").append(id)
                .append(" [label=\"").append(label).append("\"];\n");
        return id;
    }

    private String nodo(String etiqueta, String color) {
        String id = "n" + (contador++);
        String label = etiqueta
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
        dot.append("    ").append(id)
                .append(" [label=\"").append(label)
                .append("\", fillcolor=\"").append(color).append("\"];\n");
        return id;
    }

    private void arista(String padre, String hijo) {
        dot.append("    ").append(padre).append(" -> ").append(hijo).append(";\n");
    }

    private void arista(String padre, String hijo, String label) {
        dot.append("    ").append(padre).append(" -> ").append(hijo)
                .append(" [label=\"").append(label).append("\"];\n");
    }

    private String hoja(String etiqueta) {
        return nodo(etiqueta, "#d4edda");
    }

    @Override
    public String visit(NodoPrograma nodo) {
        String id = nodo("NodoPrograma", "#b8d4e8");
        for (NodoDeclaracionGlobal g : nodo.getGlobales()) {
            if (g != null) {
                arista(id, g.accept(this));
            }
        }
        return id;
    }

    @Override
    public String visit(NodoDefStruct nodo) {
        String id = nodo("NodoDefStruct\nnombre: " + nodo.getNombreStruct(), "#ffe0b2");
        for (NodoAtributoStruct a : nodo.getAtributos()) {
            arista(id, a.accept(this));
        }
        return id;
    }

    @Override
    public String visit(NodoAtributoStruct nodo) {
        String id = nodo("NodoAtributoStruct\nid: " + nodo.getIdentificador());
        if (nodo.getTipo() != null) {
            arista(id, nodo.getTipo().accept(this), "tipo");
        }
        if (nodo.getTipoSlice() != null) {
            arista(id, nodo.getTipoSlice().accept(this), "tipo");
        }
        return id;
    }

    @Override
    public String visit(NodoDefFuncion nodo) {
        String id = nodo("NodoDefFuncion\nnombre: " + nodo.getNombre()
                + (nodo.isEsMain() ? " [main]" : ""), "#b39ddb");
        for (NodoParametro p : nodo.getParametros()) {
            arista(id, p.accept(this), "param");
        }
        if (nodo.getTipoRetorno() != null) {
            arista(id, nodo.getTipoRetorno().accept(this), "retorno");
        }
        if (nodo.getSliceRetorno() != null) {
            arista(id, nodo.getSliceRetorno().accept(this), "retorno");
        }
        arista(id, nodo.getBloque().accept(this), "bloque");
        return id;
    }

    @Override
    public String visit(NodoDefFuncionStruct nodo) {
        String id = nodo("NodoDefFuncionStruct\nreceptor: "
                + nodo.getStructReceptor() + " " + nodo.getVariableReceptor()
                + "\nnombre: " + nodo.getNombre(), "#b39ddb");
        for (NodoParametro p : nodo.getPs()) {
            arista(id, p.accept(this), "param");
        }
        if (nodo.getT() != null) {
            arista(id, nodo.getT().accept(this), "retorno");
        }
        if (nodo.getTs() != null) {
            arista(id, nodo.getTs().accept(this), "retorno");
        }
        arista(id, nodo.getB().accept(this), "bloque");
        return id;
    }

    @Override
    public String visit(NodoParametro nodo) {
        String id = nodo("NodoParametro\nid: " + nodo.getIdentificador());
        if (nodo.getTipo() != null) {
            arista(id, nodo.getTipo().accept(this), "tipo");
        }
        if (nodo.getTipoSlice() != null) {
            arista(id, nodo.getTipoSlice().accept(this), "tipo");
        }
        return id;
    }

    @Override
    public String visit(NodoBloque nodo) {
        String id = nodo("NodoBloque", "#e8f5e9");
        for (NodoInstruccion ins : nodo.getInstrucciones()) {
            if (ins != null) {
                arista(id, ins.accept(this));
            }
        }
        return id;
    }

    @Override
    public String visit(NodoIf nodo) {
        String id = nodo("NodoIf", "#fff9c4");
        arista(id, nodo.getCondicion().accept(this), "condicion");
        arista(id, nodo.getBloqueThen().accept(this), "then");
        if (nodo.getBloqueElse() != null) {
            arista(id, nodo.getBloqueElse().accept(this), "else");
        }
        if (nodo.getElseIfSiguiente() != null) {
            arista(id, nodo.getElseIfSiguiente().accept(this), "else if");
        }
        return id;
    }

    @Override
    public String visit(NodoFor nodo) {
        String id = nodo("NodoFor\nesRange: " + nodo.isEsRange(), "#fff9c4");
        if (!nodo.isEsRange()) {
            if (nodo.getInit() != null) {
                arista(id, nodo.getInit().accept(this), "init");
            }
            if (nodo.getCondicion() != null) {
                arista(id, nodo.getCondicion().accept(this), "condicion");
            }
            if (nodo.getPost() != null) {
                arista(id, nodo.getPost().accept(this), "post");
            }
        } else {
            arista(id, nodo.getIterable().accept(this), "iterable");
            if (nodo.getRangeIdx() != null) {
                String nIdx = hoja("idx: " + nodo.getRangeIdx());
                arista(id, nIdx, "rangeIdx");
            }
            if (nodo.getRangeVal() != null) {
                String nVal = hoja("val: " + nodo.getRangeVal());
                arista(id, nVal, "rangeVal");
            }
        }
        arista(id, nodo.getBloque().accept(this), "bloque");
        return id;
    }

    @Override
    public String visit(NodoSwitch nodo) {
        String id = nodo("NodoSwitch", "#fff9c4");
        arista(id, nodo.getExpresion().accept(this), "expr");
        for (NodoCaso c : nodo.getCasos()) {
            arista(id, c.accept(this));
        }
        if (nodo.getCasoDefault() != null) {
            arista(id, nodo.getCasoDefault().accept(this));
        }
        return id;
    }

    @Override
    public String visit(NodoCaso nodo) {
        String id = nodo("NodoCaso");
        arista(id, nodo.getExpresion().accept(this), "valor");
        for (NodoInstruccion ins : nodo.getInstrucciones()) {
            arista(id, ins.accept(this));
        }
        return id;
    }

    @Override
    public String visit(NodoCasoDefault nodo) {
        String id = nodo("NodoCasoDefault");
        for (NodoInstruccion ins : nodo.getInstrucciones()) {
            arista(id, ins.accept(this));
        }
        return id;
    }

    @Override
    public String visit(NodoReturn nodo) {
        String id = nodo("NodoReturn", "#ffcdd2");
        if (nodo.getExpresion() != null) {
            arista(id, nodo.getExpresion().accept(this), "valor");
        }
        return id;
    }

    @Override
    public String visit(NodoBreak nodo) {
        return nodo("NodoBreak", "#ffcdd2");
    }

    @Override
    public String visit(NodoContinue nodo) {
        return nodo("NodoContinue", "#ffcdd2");
    }

    @Override
    public String visit(NodoDeclVar nodo) {
        String id = nodo("NodoDeclVar\nid: " + nodo.getIdentificador());
        if (nodo.getTipo() != null) {
            arista(id, nodo.getTipo().accept(this), "tipo");
        }
        if (nodo.getTipoSlice() != null) {
            arista(id, nodo.getTipoSlice().accept(this), "tipo");
        }
        if (nodo.getExpresion() != null) {
            arista(id, nodo.getExpresion().accept(this), "expr");
        }
        return id;
    }

    @Override
    public String visit(NodoAsignVar nodo) {
        String id = nodo("NodoAsignVar\nid: " + nodo.getIdentificador()
                + "\nop: " + nodo.getOperador());
        if (nodo.getIndice1() != null) {
            arista(id, nodo.getIndice1().accept(this), "idx1");
        }
        if (nodo.getIndice2() != null) {
            arista(id, nodo.getIndice2().accept(this), "idx2");
        }
        if (nodo.getExpresion() != null) {
            arista(id, nodo.getExpresion().accept(this), "expr");
        }
        return id;
    }

    @Override
    public String visit(NodoAsignCampo nodo) {
        String id = nodo("NodoAsignCampo\nobj: " + nodo.getObjeto()
                + "\ncampo: " + nodo.getCampo());
        arista(id, nodo.getExpresion().accept(this), "expr");
        return id;
    }

    @Override
    public String visit(NodoInstanciaStruct nodo) {
        String id = nodo("NodoInstanciaStruct\ntipo: " + nodo.getTipo()
                + "\nvar: " + nodo.getNombreStruct(), "#ffe0b2");
        for (NodoCampoStruct c : nodo.getCampos()) {
            arista(id, c.accept(this));
        }
        return id;
    }

    @Override
    public String visit(NodoCampoStruct nodo) {
        String id = nodo("NodoCampoStruct\ncampo: " + nodo.getCampo());
        if (nodo.getExpresion() != null) {
            arista(id, nodo.getExpresion().accept(this), "valor");
        }
        return id;
    }

    @Override
    public String visit(NodoAccesoCampo nodo) {
        return nodo("NodoAccesoCampo\nobj: " + nodo.getObjeto()
                + "\ncampo: " + nodo.getCampo());
    }

    @Override
    public String visit(NodoBinaria nodo) {
        String id = nodo("NodoBinaria\nop: " + nodo.getOperador());
        arista(id, nodo.getIzquierdo().accept(this), "izq");
        arista(id, nodo.getDerecho().accept(this), "der");
        return id;
    }

    @Override
    public String visit(NodoUnaria nodo) {
        String id = nodo("NodoUnaria\nop: " + nodo.getOperador());
        arista(id, nodo.getExpresion().accept(this), "expr");
        return id;
    }

    @Override
    public String visit(NodoAgrupacion nodo) {
        String id = nodo("NodoAgrupacion");
        arista(id, nodo.getExpresion().accept(this), "expr");
        return id;
    }

    @Override
    public String visit(NodoLiteral nodo) {
        return hoja("NodoLiteral\ntipo: " + nodo.getTipoLiteral()
                + "\nval: " + nodo.getValor());
    }

    @Override
    public String visit(NodoIdentificador nodo) {
        return hoja("NodoIdentificador\n" + nodo.getNombre());
    }

    @Override
    public String visit(NodoLlamadaFuncion nodo) {
        String id = nodo("NodoLlamadaFuncion\nid: " + nodo.getIdentificador(), "#e1bee7");
        for (NodoExpresion arg : nodo.getArgumentos()) {
            arista(id, arg.accept(this), "arg");
        }
        return id;
    }

    @Override
    public String visit(NodoLlamadaMetodo nodo) {
        String id = nodo("NodoLlamadaMetodo\nobj: " + nodo.getObjeto()
                + "\nmetodo: " + nodo.getMetodo(), "#e1bee7");
        for (NodoExpresion arg : nodo.getArgumentos()) {
            arista(id, arg.accept(this), "arg");
        }
        return id;
    }

    @Override
    public String visit(NodoAccesoSlice nodo) {
        String id = nodo("NodoAccesoSlice\nid: " + nodo.getIdentificador());
        arista(id, nodo.getIndice1().accept(this), "idx1");
        if (nodo.getIndice2() != null) {
            arista(id, nodo.getIndice2().accept(this), "idx2");
        }
        return id;
    }

    @Override
    public String visit(NodoLiteralSlice nodo) {
        String label = "NodoLiteralSlice\ndim: " + nodo.getDimensiones();
        String id = nodo(label, "#c8e6c9");
        if (nodo.getTipoBase() != null) {
            arista(id, nodo.getTipoBase().accept(this), "tipoBase");
        }
        if (nodo.getDimensiones() == 1) {
            for (NodoExpresion e : nodo.getExpresiones()) {
                arista(id, e.accept(this), "elem");
            }
        } else {
            for (NodoLiteralSlice fila : nodo.getFilas()) {
                arista(id, fila.accept(this), "fila");
            }
        }
        return id;
    }

    @Override
    public String visit(NodoPrintln nodo) {
        String id = nodo("NodoPrintln", "#e1bee7");
        for (NodoExpresion arg : nodo.getArgumentos()) {
            arista(id, arg.accept(this), "arg");
        }
        return id;
    }

    @Override
    public String visit(NodoLen nodo) {
        String id = nodo("NodoLen", "#e1bee7");
        arista(id, nodo.getExpresion().accept(this), "expr");
        return id;
    }

    @Override
    public String visit(NodoAppend nodo) {
        String id = nodo("NodoAppend", "#e1bee7");
        arista(id, nodo.getSlice().accept(this), "slice");
        arista(id, nodo.getValor().accept(this), "valor");
        return id;
    }

    @Override
    public String visit(NodoAtoi nodo) {
        String id = nodo("NodoAtoi", "#e1bee7");
        arista(id, nodo.getExpresion().accept(this), "expr");
        return id;
    }

    @Override
    public String visit(NodoParsefloat nodo) {
        String id = nodo("NodoParsefloat", "#e1bee7");
        arista(id, nodo.getExpresion().accept(this), "expr");
        return id;
    }

    @Override
    public String visit(NodoTypeof nodo) {
        String id = nodo("NodoTypeof", "#e1bee7");
        arista(id, nodo.getExpresion().accept(this), "expr");
        return id;
    }

    @Override
    public String visit(NodoSlicesIndex nodo) {
        String id = nodo("NodoSlicesIndex", "#e1bee7");
        arista(id, nodo.getSlice().accept(this), "slice");
        arista(id, nodo.getValor().accept(this), "valor");
        return id;
    }

    @Override
    public String visit(NodoStringsJoin nodo) {
        String id = nodo("NodoStringsJoin", "#e1bee7");
        arista(id, nodo.getSlice().accept(this), "slice");
        arista(id, nodo.getSeparador().accept(this), "sep");
        return id;
    }

    @Override
    public String visit(NodoTipo nodo) {
        return hoja("NodoTipo\n" + nodo.getNombreTipo());
    }

    @Override
    public String visit(NodoTipoSlice nodo) {
        String id = nodo("NodoTipoSlice\ndim: " + nodo.getDimensiones());
        arista(id, nodo.getTipoBase().accept(this), "base");
        return id;
    }

    @Override
    public String visit(NodoError nodo) {
        return nodo("NodoError", "#ffcdd2");
    }
}
