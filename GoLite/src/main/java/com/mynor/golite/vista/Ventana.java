/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mynor.golite.vista;

import java.awt.Color;
import java.io.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

/**
 *
 * @author mynordma
 */
public class Ventana extends javax.swing.JFrame {

    private String rutaActual = "";
    private String rutaAbsolutaActual = "";
    private boolean cambiosSinGuardar = false;
    private boolean hayErrores = false;

    public Ventana() {
        initComponents();
        setTitle("Editor - GoLite");
        setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(30, 30, 30));
        outputTextArea.setEditable(false);

        inputTextArea.getDocument().addDocumentListener(new DocumentListener() {
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

        inputTextArea.setBackground(new Color(37, 37, 38));
        inputTextArea.setForeground(new Color(212, 212, 212));
        inputTextArea.setCaretColor(Color.WHITE);
        inputTextArea.setSelectionColor(new Color(38, 79, 120));

        outputTextArea.setBackground(new Color(37, 37, 38));
        outputTextArea.setForeground(new Color(212, 212, 212));
        outputTextArea.setCaretColor(Color.WHITE);
        outputTextArea.setSelectionColor(new Color(38, 79, 120));

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        inputTextArea = new javax.swing.JTextArea();
        infoLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        outputTextArea = new javax.swing.JTextArea();
        outputLabel = new javax.swing.JLabel();
        tokensBtn = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        abrir = new javax.swing.JMenu();
        guardar = new javax.swing.JMenu();
        guardarComo = new javax.swing.JMenu();
        nuevo = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(java.awt.Color.black);

        inputTextArea.setColumns(20);
        inputTextArea.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        inputTextArea.setRows(5);
        inputTextArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inputTextAreaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(inputTextArea);

        infoLabel.setBackground(new java.awt.Color(255, 255, 255));
        infoLabel.setForeground(new java.awt.Color(255, 255, 255));
        infoLabel.setText("Ln 1 Col 1");

        outputTextArea.setColumns(20);
        outputTextArea.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        outputTextArea.setRows(5);
        jScrollPane2.setViewportView(outputTextArea);

        outputLabel.setBackground(new java.awt.Color(255, 255, 255));
        outputLabel.setForeground(new java.awt.Color(255, 255, 255));
        outputLabel.setText("Output");

        tokensBtn.setBackground(new java.awt.Color(51, 51, 51));
        tokensBtn.setForeground(new java.awt.Color(255, 255, 255));
        tokensBtn.setText("Tokens");
        tokensBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tokensBtnMouseClicked(evt);
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

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(outputLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(infoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 331, Short.MAX_VALUE)
                        .addComponent(tokensBtn)
                        .addGap(211, 211, 211)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(infoLabel)
                    .addComponent(tokensBtn))
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
                inputTextArea.setText(contenido.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_abrirMouseClicked

    private void guardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guardarMouseClicked
        if (!rutaAbsolutaActual.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaAbsolutaActual))) {
                writer.write(inputTextArea.getText());
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
                writer.write(inputTextArea.getText());
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

        inputTextArea.setText("");
        rutaAbsolutaActual = "";
        rutaActual = "";
        setTitle("Editor - GoLite - Nuevo Documento");
        cambiosSinGuardar = false;
    }//GEN-LAST:event_nuevoMouseClicked

    private void tokensBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tokensBtnMouseClicked
        if (!hayErrores) {
            JDialog dialogo = new JDialog(this, "Tokens", true);
            dialogo.setSize(1000, 800);
            dialogo.setLocationRelativeTo(this);

            String[] columnas = {"Lexema", "Línea", "Columna", "Tipo"};
            JTable tablaTokens = new JTable(new String[][]{{"a", "b", "c", "d"}}, columnas);
            JScrollPane scrollPane = new JScrollPane(tablaTokens);

            dialogo.add(scrollPane);
            dialogo.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Hay errores en el texto.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_tokensBtnMouseClicked

    private void inputTextAreaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputTextAreaMouseClicked
        actualizarLabelInfo();
    }//GEN-LAST:event_inputTextAreaMouseClicked

    private void textoModificado() {
        setTitle("Editor - GoLite - " + rutaActual + " (Sin guardar)");
        cambiosSinGuardar = true;
        actualizarLabelInfo();
    }

    private void actualizarLabelInfo() {
        int caretPosition = inputTextArea.getCaretPosition();
        try {
            int linea = inputTextArea.getLineOfOffset(caretPosition);
            int columna = caretPosition - inputTextArea.getLineStartOffset(linea);

            infoLabel.setText("Ln " + (linea + 1) + " Col: " + (columna + 1));
        } catch (BadLocationException e) {
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu abrir;
    private javax.swing.JMenu guardar;
    private javax.swing.JMenu guardarComo;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JTextArea inputTextArea;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenu nuevo;
    private javax.swing.JLabel outputLabel;
    private javax.swing.JTextArea outputTextArea;
    private javax.swing.JButton tokensBtn;
    // End of variables declaration//GEN-END:variables

}
