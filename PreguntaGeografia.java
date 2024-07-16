package PruebasIniciales;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreguntaGeografia extends JPanel {
    private List<Pregunta> preguntas;
    private int indicePreguntaActual = 0;
    private int puntaje = 0;
    private String nombreUsuario;

    public PreguntaGeografia(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        cargarPreguntasDesdeBD();
        setLayout(new BorderLayout());
        mostrarSiguientePregunta();
    }

    private void cargarPreguntasDesdeBD() {
        preguntas = new ArrayList<>();
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT * FROM preguntas_geografia";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String pregunta = rs.getString("pregunta");
                String opcion1 = rs.getString("opcion1");
                String opcion2 = rs.getString("opcion2");
                String opcion3 = rs.getString("opcion3");
                String opcion4 = rs.getString("opcion4");
                int correcta = rs.getInt("respuesta_correcta");
                preguntas.add(new Pregunta(pregunta, opcion1, opcion2, opcion3, opcion4, correcta));
            }

            Collections.shuffle(preguntas); // Mezclar preguntas para aleatoriedad
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void mostrarSiguientePregunta() {
        if (indicePreguntaActual < 5 && indicePreguntaActual < preguntas.size()) {
            Pregunta preguntaActual = preguntas.get(indicePreguntaActual);
            removeAll();
            JPanel panelPregunta = new JPanel(new GridLayout(6, 1));
            JLabel labelPregunta = new JLabel(preguntaActual.getPregunta(), SwingConstants.CENTER);
            JButton opcion1 = new JButton(preguntaActual.getOpcion1());
            JButton opcion2 = new JButton(preguntaActual.getOpcion2());
            JButton opcion3 = new JButton(preguntaActual.getOpcion3());
            JButton opcion4 = new JButton(preguntaActual.getOpcion4());

            opcion1.addActionListener(e -> verificarRespuesta(1, preguntaActual.getCorrecta()));
            opcion2.addActionListener(e -> verificarRespuesta(2, preguntaActual.getCorrecta()));
            opcion3.addActionListener(e -> verificarRespuesta(3, preguntaActual.getCorrecta()));
            opcion4.addActionListener(e -> verificarRespuesta(4, preguntaActual.getCorrecta()));

            panelPregunta.add(labelPregunta);
            panelPregunta.add(opcion1);
            panelPregunta.add(opcion2);
            panelPregunta.add(opcion3);
            panelPregunta.add(opcion4);
            add(panelPregunta, BorderLayout.CENTER);
            revalidate();
            repaint();
        } else {
            mostrarResultadoFinal();
        }
    }

    private void verificarRespuesta(int respuestaSeleccionada, int respuestaCorrecta) {
        if (respuestaSeleccionada == respuestaCorrecta) {
            JOptionPane.showMessageDialog(this, "¡Muy bien!");
            puntaje += 10;
            indicePreguntaActual++;
            mostrarSiguientePregunta();
        } else {
            JOptionPane.showMessageDialog(this, "Más suerte para la próxima.");
        }
    }

    private void mostrarResultadoFinal() {
        JOptionPane.showMessageDialog(this, nombreUsuario + " tiene un puntaje de " + puntaje + " puntos.");
        guardarPuntajeEnBD();
        System.exit(0); // Salir del juego
    }

    private void guardarPuntajeEnBD() {
        try (Connection conn = Conexion.getConnection()) {
            String query = "UPDATE usuarios SET puntaje = ? WHERE nombre = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, puntaje);
            stmt.setString(2, nombreUsuario);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Clase para manejar las preguntas
    private static class Pregunta {
        private String pregunta, opcion1, opcion2, opcion3, opcion4;
        private int correcta;

        public Pregunta(String pregunta, String opcion1, String opcion2, String opcion3, String opcion4, int correcta) {
            this.pregunta = pregunta;
            this.opcion1 = opcion1;
            this.opcion2 = opcion2;
            this.opcion3 = opcion3;
            this.opcion4 = opcion4;
            this.correcta = correcta;
        }

        public String getPregunta() {
            return pregunta;
        }

        public String getOpcion1() {
            return opcion1;
        }

        public String getOpcion2() {
            return opcion2;
        }

        public String getOpcion3() {
            return opcion3;
        }

        public String getOpcion4() {
            return opcion4;
        }

        public int getCorrecta() {
            return correcta;
        }
    }
}

