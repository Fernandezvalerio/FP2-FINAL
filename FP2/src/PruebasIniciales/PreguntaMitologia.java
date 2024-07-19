package PruebasIniciales;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreguntaMitologia extends JPanel {
    private List<Pregunta> preguntas;
    private int indicePreguntaActual = 0;
    private int puntaje = 0;
    private String nombreUsuario;
    private static final String CORRECT_SOUND_PATH = "../sonidos/correct.wav"; // Ruta al archivo de sonido de respuesta correcta
    private static final String ERROR_SOUND_PATH = "../sonidos/error.wav"; // Ruta al archivo de sonido de respuesta incorrecta

    public PreguntaMitologia(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        cargarPreguntasDesdeBD();
        setLayout(new BorderLayout());
        mostrarSiguientePregunta();
    }

    private void cargarPreguntasDesdeBD() {
        preguntas = new ArrayList<>();
        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT * FROM preguntas_mitologia";
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

            JPanel panelPregunta = new JPanel(new BorderLayout(10, 10)); // Añadir espacio entre los componentes
            panelPregunta.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Añadir márgenes

            JLabel labelPregunta = new JLabel("<html> <div style='text-align: center;'>" + preguntaActual.getPregunta() + "</div></html>", SwingConstants.CENTER);
            labelPregunta.setOpaque(true);
            labelPregunta.setBackground(Color.WHITE);
            labelPregunta.setSize(4000, 800);
            panelPregunta.add(labelPregunta, BorderLayout.NORTH);

            JPanel opcionesPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // Añadir espacio entre las opciones
            opcionesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10)); // Añadir márgenes

            JButton opcion1 = crearBotonOpcion(preguntaActual.getOpcion1(), 1, preguntaActual.getCorrecta());
            JButton opcion2 = crearBotonOpcion(preguntaActual.getOpcion2(), 2, preguntaActual.getCorrecta());
            JButton opcion3 = crearBotonOpcion(preguntaActual.getOpcion3(), 3, preguntaActual.getCorrecta());
            JButton opcion4 = crearBotonOpcion(preguntaActual.getOpcion4(), 4, preguntaActual.getCorrecta());

            opcionesPanel.add(opcion1);
            opcionesPanel.add(opcion2);
            opcionesPanel.add(opcion3);
            opcionesPanel.add(opcion4);

            panelPregunta.add(opcionesPanel, BorderLayout.CENTER);
            add(panelPregunta, BorderLayout.CENTER);
            revalidate();
            repaint();
            ajustarTamañoFuente(labelPregunta, preguntaActual.getPregunta());
        } else {
            mostrarResultadoFinal();
        }
    }

    private JButton crearBotonOpcion(String texto, int opcion, int correcta) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 40));
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boton.getBackground().equals(UIManager.getColor("Button.background"))) {
                    boton.setBackground(new Color(255, 255, 0, 128)); // Amarillo semitransparente
                }
                boton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (boton.getBackground().equals(new Color(255, 255, 0, 128))) {
                    boton.setBackground(UIManager.getColor("Button.background"));
                }
                boton.repaint();
            }
        });

        boton.addActionListener(e -> {
            Color originalColor = boton.getBackground();
            if (opcion == correcta) {
                boton.setBackground(new Color(140, 255, 140, 128)); // Verde semitransparente
                playSound(CORRECT_SOUND_PATH); // Reproducir sonido de respuesta correcta
            } else {
                boton.setBackground(new Color(255, 13, 17, 128)); // Rojo semitransparente
                playSound(ERROR_SOUND_PATH); // Reproducir sonido de respuesta incorrecta
            }
            boton.repaint();

            Timer timer = new Timer(1000, event -> {
                if (opcion == correcta) {
                    puntaje += 10;
                    indicePreguntaActual++;
                }
                boton.setBackground(originalColor);
                mostrarSiguientePregunta();
            });
            timer.setRepeats(false);
            timer.start();
        });

        return boton;
    }

    private void ajustarTamañoFuente(JLabel label, String text) {
        Font font = label.getFont();
        int componentWidth = label.getWidth();
        int componentHeight = label.getHeight();

        // Initial size of the font
        int fontSize = 70;

        // Create a new font with the initial size
        Font newFont = font.deriveFont(Font.BOLD, fontSize);

        // Calculate the width and height of the text with the new font
        FontMetrics metrics = label.getFontMetrics(newFont);
        int stringWidth = metrics.stringWidth(text);
        int stringHeight = metrics.getHeight();

        // Adjust the font size until the text fits within the component
        while ((stringWidth > componentWidth || stringHeight > componentHeight) && fontSize > 1) {
            fontSize--;
            newFont = font.deriveFont(Font.BOLD, fontSize);
            metrics = label.getFontMetrics(newFont);
            stringWidth = metrics.stringWidth(text);
            stringHeight = metrics.getHeight();
        }

        // Set the label's font to the newly determined size
        label.setFont(newFont);
        label.repaint(); // Asegurarse de que el cambio de fuente se refleje en el componente
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

    private void playSound(String soundFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(soundFile));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
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
