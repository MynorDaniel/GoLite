/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mynor.golite.vista;

import com.mynor.golite.analizadorsemantico.AnalizadorSemantico;
import com.mynor.golite.ast.NodoPrograma;
import com.mynor.golite.graphviz.ManejadorGraphviz;
import com.mynor.golite.interprete.Ejecutor;
import com.mynor.golite.lexer.LexerGLT;
import com.mynor.golite.lexer.TipoToken;
import com.mynor.golite.lexer.Token;
import com.mynor.golite.parser.ParserGLT;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.Symbol;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

/**
 *
 * @author mynordma
 */
public class Ventana extends javax.swing.JFrame {

    private String rutaActual = "";
    private String rutaAbsolutaActual = "";
    private boolean cambiosSinGuardar = false;

    RSyntaxTextArea editor = new RSyntaxTextArea(800, 600);

    private ArrayList<Token> tokens = new ArrayList<>();
    private ArrayList<String[]> errores = new ArrayList<>(); // tipo, error, ln, col
    private String[][] tablaSimbolos;
    private NodoPrograma programa;

    public Ventana() {
        initComponents();

        editorScrollPane.setViewportView(editor);

        editor.setCodeFoldingEnabled(true);
        editor.setAntiAliasingEnabled(true);
        editor.setTabSize(4);
        editor.setSyntaxEditingStyle(
                SyntaxConstants.SYNTAX_STYLE_GO
        );

        editorScrollPane.setRowHeaderView(
                new org.fife.ui.rtextarea.LineNumberList(editor)
        );

        editor.setHighlightCurrentLine(false);

        setTitle("Editor - GoLite");
        setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(30, 30, 30));
        outputTextArea.setEditable(false);

        editor.setBackground(new Color(37, 37, 38));
        editor.setForeground(new Color(212, 212, 212));
        editor.setCaretColor(Color.WHITE);
        editor.setSelectionColor(new Color(38, 79, 120));
        outputTextArea.setBackground(new Color(37, 37, 38));
        outputTextArea.setForeground(new Color(212, 212, 212));
        outputTextArea.setCaretColor(Color.WHITE);
        outputTextArea.setSelectionColor(new Color(38, 79, 120));
        outputTextArea.setDragEnabled(true);

        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textoModificado();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textoModificado();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textoModificado();
            }
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        outputTextArea = new javax.swing.JTextArea();
        outputLabel = new javax.swing.JLabel();
        execBtn = new javax.swing.JButton();
        editorScrollPane = new org.fife.ui.rtextarea.RTextScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        abrir = new javax.swing.JMenu();
        guardar = new javax.swing.JMenu();
        guardarComo = new javax.swing.JMenu();
        nuevo = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        tknsItem = new javax.swing.JMenuItem();
        errItem = new javax.swing.JMenuItem();
        astItem = new javax.swing.JMenuItem();
        symItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(java.awt.Color.black);

        infoLabel.setBackground(new java.awt.Color(255, 255, 255));
        infoLabel.setForeground(new java.awt.Color(255, 255, 255));
        infoLabel.setText("Ln 1 Col 1");

        outputTextArea.setBackground(new java.awt.Color(43, 43, 43));
        outputTextArea.setColumns(20);
        outputTextArea.setFont(new java.awt.Font("Monospaced", 0, 15)); // NOI18N
        outputTextArea.setForeground(new java.awt.Color(169, 183, 198));
        outputTextArea.setRows(5);
        outputTextArea.setDragEnabled(true);
        jScrollPane2.setViewportView(outputTextArea);

        outputLabel.setBackground(new java.awt.Color(255, 255, 255));
        outputLabel.setForeground(new java.awt.Color(255, 255, 255));
        outputLabel.setText("Output");

        execBtn.setBackground(new java.awt.Color(51, 51, 51));
        execBtn.setForeground(new java.awt.Color(255, 255, 255));
        execBtn.setText("Ejecutar");
        execBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                execBtnActionPerformed(evt);
            }
        });

        jMenuBar1.setBackground(new java.awt.Color(51, 51, 51));
        jMenuBar1.setForeground(new java.awt.Color(255, 255, 255));

        abrir.setForeground(new java.awt.Color(255, 255, 255));
        abrir.setText("Abrir");
        abrir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirMouseClicked(evt);
            }
        });
        jMenuBar1.add(abrir);

        guardar.setForeground(new java.awt.Color(255, 255, 255));
        guardar.setText("Guardar");
        guardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                guardarMouseClicked(evt);
            }
        });
        jMenuBar1.add(guardar);

        guardarComo.setForeground(new java.awt.Color(255, 255, 255));
        guardarComo.setText("Guardar como");
        guardarComo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                guardarComoMouseClicked(evt);
            }
        });
        jMenuBar1.add(guardarComo);

        nuevo.setForeground(new java.awt.Color(255, 255, 255));
        nuevo.setText("Nuevo");
        nuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nuevoMouseClicked(evt);
            }
        });
        jMenuBar1.add(nuevo);

        jMenu1.setBackground(new java.awt.Color(51, 51, 51));
        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("Reportes");

        tknsItem.setBackground(new java.awt.Color(51, 51, 51));
        tknsItem.setForeground(new java.awt.Color(255, 255, 255));
        tknsItem.setText("Tokens");
        tknsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tknsItemActionPerformed(evt);
            }
        });
        jMenu1.add(tknsItem);

        errItem.setBackground(new java.awt.Color(51, 51, 51));
        errItem.setForeground(new java.awt.Color(255, 255, 255));
        errItem.setText("Errores");
        errItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errItemActionPerformed(evt);
            }
        });
        jMenu1.add(errItem);

        astItem.setBackground(new java.awt.Color(51, 51, 51));
        astItem.setForeground(new java.awt.Color(255, 255, 255));
        astItem.setText("AST");
        astItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                astItemActionPerformed(evt);
            }
        });
        jMenu1.add(astItem);

        symItem.setBackground(new java.awt.Color(51, 51, 51));
        symItem.setForeground(new java.awt.Color(255, 255, 255));
        symItem.setText("Tabla De Simbolos");
        symItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symItemActionPerformed(evt);
            }
        });
        jMenu1.add(symItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(editorScrollPane)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(infoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 498, Short.MAX_VALUE)
                        .addComponent(execBtn)
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(outputLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editorScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(outputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(infoLabel)
                    .addComponent(execBtn))
                .addGap(9, 9, 9))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void abrirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_abrirMouseClicked
        if (cambiosSinGuardar) {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Guardar cambios?",
                    "Cambios no guardados",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                return;
            }

            if (opcion == JOptionPane.YES_OPTION) {
                guardarMouseClicked(evt);
            }
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona un archivo");

        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
                "Archivos de texto (*.glt)", "glt"
        );
        fileChooser.setFileFilter(filtro);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            rutaAbsolutaActual = archivoSeleccionado.getAbsolutePath();
            rutaActual = archivoSeleccionado.getPath();
            setTitle("Editor - GoLite - " + rutaActual);

            try (BufferedReader br = new BufferedReader(new FileReader(archivoSeleccionado))) {
                StringBuilder contenido = new StringBuilder();
                String linea;
                while ((linea = br.readLine()) != null) {
                    contenido.append(linea).append("\n");
                }
                editor.setText(contenido.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_abrirMouseClicked

    private void guardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarMouseClicked
        if (!rutaAbsolutaActual.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaAbsolutaActual))) {
                writer.write(editor.getText());
                setTitle("Editor - GoLite - " + rutaActual + " (Guardado)");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            cambiosSinGuardar = false;
        } else {
            guardarComoMouseClicked(evt);
        }
    }//GEN-LAST:event_guardarMouseClicked

    private void guardarComoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarComoMouseClicked
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo como...");

        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivo (*.glt)", "glt");
        fileChooser.setFileFilter(filtro);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int resultado = fileChooser.showSaveDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            String rutaNuevoArchivo = archivoSeleccionado.getAbsolutePath();

            if (!rutaNuevoArchivo.toLowerCase().endsWith(".glt")) {
                archivoSeleccionado = new File(rutaNuevoArchivo + ".glt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSeleccionado))) {
                writer.write(editor.getText());
                if (rutaAbsolutaActual.isEmpty()) {
                    rutaAbsolutaActual = archivoSeleccionado.getAbsolutePath();
                    rutaActual = archivoSeleccionado.getPath();
                }
                setTitle("Editor - GoLite - " + rutaActual);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_guardarComoMouseClicked

    private void nuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoMouseClicked
        if (cambiosSinGuardar) {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Guardar cambios?",
                    "Cambios no guardados",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                return;
            }

            if (opcion == JOptionPane.YES_OPTION) {
                guardarMouseClicked(evt);
            }
        }

        editor.setText("");
        rutaAbsolutaActual = "";
        rutaActual = "";
        setTitle("Editor - GoLite - Nuevo Documento");
        cambiosSinGuardar = false;
    }//GEN-LAST:event_nuevoMouseClicked

    private void execBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_execBtnActionPerformed
        // Ejecutar analisis

        tablaSimbolos = null;
        programa = null;

        String entrada = editor.getText();
        StringReader reader = new StringReader(entrada);

        LexerGLT lexer = new LexerGLT(reader);
        ParserGLT parser = new ParserGLT(lexer);

        Symbol result = null;
        try {
            result = parser.parse();

        } catch (Exception ex) {
            log(ex.getMessage());
        }
        NodoPrograma ast = (NodoPrograma) result.value;

        AnalizadorSemantico analizadorSemantico = new AnalizadorSemantico();

        try {
            analizadorSemantico.visit(ast);
        } catch (Exception e) {
            log("Error al analizar la semántica: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        cargarErrores(lexer.getTokens(), parser.getErroresSintacticos(), analizadorSemantico.getErrores());
        tokens = lexer.getTokens();

        Ejecutor ejecutor = new Ejecutor();
        try {
            ejecutor.visit(ast);
        } catch (Exception e) {
            log("Error al ejecutar el ast: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        errores.forEach(e -> {
            log("Error " + e[0] + ": " + e[1] + " --- " + e[2] + "|" + e[3]);
        });

        if (ejecutor.getSalida().isBlank()) {
            log("Salida vacía");
        } else {
            log("------------------- Ejecutando Programa -------------------");
            log(ejecutor.getSalida());
        }

        tablaSimbolos = ejecutor.getTablaSimbolos();
        programa = ast;

    }//GEN-LAST:event_execBtnActionPerformed

    private void log(String s) {
        outputTextArea.append("\n");
        outputTextArea.append(s);
    }

    private void cargarErrores(ArrayList<Token> erroresLexicos, List<String[]> erroresSintacticos, List<String[]> erroresSemanticos) {
        errores.clear();

        for (Token e : erroresLexicos) {
            if (e.getTipo() == TipoToken.ERROR) {
                errores.add(new String[]{"Léxico", "Token " + e.getLexema() + " inesperado", String.valueOf(e.getLinea()), String.valueOf(e.getColumna())});
            }
        }

        for (String[] errorSintactico : erroresSintacticos) {
            errores.add(new String[]{"Sintáctico", errorSintactico[0], errorSintactico[1], errorSintactico[2]});
        }

        for (String[] errorSemantico : erroresSemanticos) {
            errores.add(new String[]{"Semántico", errorSemantico[0], String.valueOf((Integer.parseInt(errorSemantico[1]) + 1)), String.valueOf((Integer.parseInt(errorSemantico[2]) + 1))});
        }
    }

    private void tknsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tknsItemActionPerformed
        String[] columnas = {"Tipo de Token", "Lexema", "Línea", "Columna"};

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Token t : tokens) {
            Object[] fila = {
                t.getTipo(),
                t.getLexema(),
                t.getLinea(),
                t.getColumna()
            };
            modelo.addRow(fila);
        }

        JTable tablaTokens = new JTable(modelo);
        tablaTokens.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tablaTokens);

        javax.swing.JDialog ventanaTokens = new javax.swing.JDialog(this, "Tabla de Tokens", true);
        ventanaTokens.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ventanaTokens.setSize(600, 400);
        ventanaTokens.setLocationRelativeTo(this);

        ventanaTokens.add(scrollPane);
        ventanaTokens.setVisible(true);
    }//GEN-LAST:event_tknsItemActionPerformed

    private void errItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errItemActionPerformed
        String[] columnas = {"Tipo", "Descripción", "Ln", "Col"};

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (String[] err : errores) {
            modelo.addRow(err);
        }

        JTable tablaErrores = new JTable(modelo);
        tablaErrores.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tablaErrores);

        javax.swing.JDialog ventanaErrores = new javax.swing.JDialog(this, "Errores", true);
        ventanaErrores.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ventanaErrores.setSize(600, 300);
        ventanaErrores.setLocationRelativeTo(this);

        ventanaErrores.add(scrollPane);
        ventanaErrores.setVisible(true);
    }//GEN-LAST:event_errItemActionPerformed

    private void astItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_astItemActionPerformed
        if (programa == null) {
            log("AST vacío");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar AST");

        fileChooser.setSelectedFile(new File("ast"));

        int opcion = fileChooser.showSaveDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {

            File archivo = fileChooser.getSelectedFile();

            String ruta = archivo.getAbsolutePath();

            try {
                ManejadorGraphviz m = new ManejadorGraphviz();
                m.generarImagenAST(programa, ruta);

                JOptionPane.showMessageDialog(
                        this,
                        "AST generado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al generar el AST:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_astItemActionPerformed

    private void symItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symItemActionPerformed
        if (tablaSimbolos != null) {
            String[] columnas = {"Categoría", "Nombre", "Tipo", "Valor", "Ámbito"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (String[] fila : tablaSimbolos) {
                modelo.addRow(fila);
            }
            JTable tabla = new JTable(modelo);
            tabla.setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(tabla);
            javax.swing.JDialog ventana = new javax.swing.JDialog(this, "Tabla de Símbolos", true);
            ventana.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            ventana.setSize(700, 400);
            ventana.setLocationRelativeTo(this);
            ventana.add(scrollPane);
            ventana.setVisible(true);
        } else {
            log("Tabla de Símbolos vacía");
        }
        
        tablaSimbolos = null;
    }//GEN-LAST:event_symItemActionPerformed

    private void textoModificado() {
        setTitle("Editor - GoLite - " + rutaActual + " (Sin guardar)");
        cambiosSinGuardar = true;
        actualizarLabelInfo();
    }

    private void actualizarLabelInfo() {
        int caretPosition = editor.getCaretPosition();
        try {
            int linea = editor.getLineOfOffset(caretPosition);
            int columna = caretPosition - editor.getLineStartOffset(linea);

            infoLabel.setText("Ln " + (linea + 1) + " Col " + (columna + 1));
        } catch (BadLocationException e) {
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu abrir;
    private javax.swing.JMenuItem astItem;
    private javax.swing.JScrollPane editorScrollPane;
    private javax.swing.JMenuItem errItem;
    private javax.swing.JButton execBtn;
    private javax.swing.JMenu guardar;
    private javax.swing.JMenu guardarComo;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenu nuevo;
    private javax.swing.JLabel outputLabel;
    private javax.swing.JTextArea outputTextArea;
    private javax.swing.JMenuItem symItem;
    private javax.swing.JMenuItem tknsItem;
    // End of variables declaration//GEN-END:variables

}
