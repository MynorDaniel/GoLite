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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.Symbol;

/**
 *
 * @author mynordma
 */
public class GoLite {

    static PrintWriter out;

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        //Ventana ventana = new Ventana();
        //ventana.setVisible(true);

        test();

    }

    static void test() throws IOException, Exception {
        out = new PrintWriter(new FileWriter("/home/mynordma/testcompi/testx/salida.txt"));

        FileReader reader = new FileReader(new File("/home/mynordma/testcompi/testx/test.glt"));
        LexerGLT lexer = new LexerGLT(reader);

        ParserGLT parser = new ParserGLT(lexer);

        Symbol result = parser.parse();

        NodoPrograma ast = (NodoPrograma) result.value;
        
        imprimirErrores(parser.getErroresSintacticos());

        imprimirAST(ast, 0);
        out.flush();

    }

    static void imprimirAST(Object nodo, int nivel) {
        if (nodo == null) {
            return;
        }

        String indent = "  ".repeat(nivel);

        if (nodo instanceof com.mynor.golite.ast.NodoAST || nodo.getClass().getSimpleName().startsWith("Nodo")) {
            out.println(indent + nodo.getClass().getSimpleName());

            try {
                var fields = nodo.getClass().getDeclaredFields();
                for (var f : fields) {
                    f.setAccessible(true);
                    Object value = f.get(nodo);
                    if (value == null) {
                        continue;
                    }

                    if (value instanceof java.util.List<?> list) {
                        for (Object child : list) {
                            imprimirAST(child, nivel + 1);
                        }
                    } 
                    else if (f.getType().isPrimitive() || value instanceof String || value instanceof Boolean) {
                        out.println(indent + "  [" + f.getName() + ": " + value + "]");
                    } 
                    else {
                        imprimirAST(value, nivel + 1);
                    }
                }
            } catch (Exception ignored) {
            }
        } 
        else {
            out.println(indent + nodo.toString());
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

    private static void imprimirErrores(List<String[]> erroresSintacticos) {
        erroresSintacticos.forEach(e -> {
            System.out.println("Error sintactico: " + e[0] + " | " + e[1] + " |" + e[2]);
        });
    }
}
