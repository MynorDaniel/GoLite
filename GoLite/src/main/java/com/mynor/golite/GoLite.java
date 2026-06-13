/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mynor.golite;

import com.mynor.golite.ast.*;
import com.mynor.golite.lexer.LexerGLT;
import com.mynor.golite.lexer.TipoToken;
import com.mynor.golite.lexer.Token;
import com.mynor.golite.parser.ParserGLT;
import com.mynor.golite.parser.Terminal;
import com.mynor.golite.vista.Ventana;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.Symbol;

/**
 *
 * @author mynordma
 */
public class GoLite {

    public static void main(String[] args) throws FileNotFoundException {
        Ventana ventana = new Ventana();
        ventana.setVisible(true);
        
        

    }

    static void imprimirAST(Object nodo, int nivel) {
        if (nodo == null) {
            return;
        }

        String indent = "  ".repeat(nivel);

        System.out.println(indent + nodo.getClass().getSimpleName());

        try {
            var fields = nodo.getClass().getDeclaredFields();
            for (var f : fields) {
                f.setAccessible(true);
                Object value = f.get(nodo);

                if (value instanceof java.util.List<?> list) {
                    for (Object child : list) {
                        imprimirAST(child, nivel + 1);
                    }
                } else if (value != null
                        && !f.getType().getName().startsWith("java.lang")) {
                    imprimirAST(value, nivel + 1);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static void imprimirErrores(ArrayList<Token> erroresLexicos, List<String> erroresSintacticos) {
        erroresLexicos.forEach(e -> {
            System.out.println("Error léxico: token inesperado " + e.getLexema() + " en ln " + e.getLinea() + " col " + e.getColumna());
        });

        erroresSintacticos.forEach(e -> {
            System.out.println("Error sintáctico: " + e);
        });
    }

    private static void imprimirTokens(ArrayList<Token> tokens) {
        tokens.forEach(e -> {
            System.out.println("Token: " + e.getLexema() + " | " + e.getTipo().name());
        });
    }
}
