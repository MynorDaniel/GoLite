/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mynor.golite;

import com.mynor.golite.lexer.LexerGLT;
import com.mynor.golite.lexer.TipoToken;
import com.mynor.golite.parser.Terminal;
import com.mynor.golite.vista.Ventana;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java_cup.runtime.Symbol;

/**
 *
 * @author mynordma
 */
public class GoLite {

    public static void main(String[] args) throws FileNotFoundException {
        //Ventana ventana = new Ventana();
        //ventana.setVisible(true);
        
        try {

            LexerGLT lexer = new LexerGLT(
                new FileReader("/home/mynordma/EJ26_OLC1_3358109340901-202331039/doc/test.glt")
            );

            Symbol token;

            do {

                token = lexer.next_token();

            } while (token.sym != Terminal.EOF);
            
            int[] errores = new int[]{0};
            
            lexer.getTokens().forEach(t -> {
                if(t.getTipo() == TipoToken.ERROR) errores[0]++;
                System.out.println("Token: " + t.getLexema() + " " + t.getLinea() + "|" + t.getColumna() + " " + t.getTipo().name());
            });
            
            System.out.println("Total errores: " + errores[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
