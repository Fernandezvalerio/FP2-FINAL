package MIO;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// Clase que representa el panel de inicio del juego
public class Inicio extends JPanel {

    // Variable para almacenar la imagen de fondo
    private BufferedImage imagenFondo;

    // Constructor de la clase Inicio
    public Inicio() {
        try {
            // Cargar la imagen desde los recursos
            imagenFondo = ImageIO.read(getClass().getResource("/imagenes/image.jpg"));
            if (imagenFondo == null) {
                System.out.println("No se pudo cargar la imagen.");
            }
        } catch (IOException | IllegalArgumentException ex) {
            ex.printStackTrace();
            System.out.println("Excepción al intentar cargar la imagen: " + ex.getMessage());
        }
        // Establecer el diseño del panel como null para posicionar los componentes manualmente
        setLayout(null);
        
        // Crear y configurar los botones
        JButton iniciarJuegoButton = new JButton("Iniciar Juego");
        iniciarJuegoButton.setBounds(450, 250, 200, 50);
        iniciarJuegoButton.setFont(new Font("Arial", Font.BOLD, 20));
        iniciarJuegoButton.setBackground(Color.RED);
        iniciarJuegoButton.setForeground(Color.WHITE);

        JButton logrosButton = new JButton("Logros");
        logrosButton.setBounds(450, 320, 200, 50);
        logrosButton.setFont(new Font("Arial", Font.BOLD, 20));
        logrosButton.setBackground(Color.RED);
        logrosButton.setForeground(Color.WHITE);

        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(450, 390, 200, 50);
        salirButton.setFont(new Font("Arial", Font.BOLD, 20));
        salirButton.setBackground(Color.RED);
        salirButton.setForeground(Color.WHITE);

        // Agregar los botones al panel
        add(iniciarJuegoButton);
        add(logrosButton);
        add(salirButton);
    }

    // Método para pintar el componente
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            // Dibujar la imagen de fondo ajustada al tamaño del panel
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Si no se pudo cargar la imagen, mostrar un mensaje de error
            g.setColor(Color.RED);
            g.drawString("No se pudo cargar la imagen.", 20, 30);
        }
        // Dibujar el título y subtítulo
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("PluzTriviaPeru", 350, 100);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Aprende el que pregunta Edición Perú", 330, 140);
    }

    // Método para crear y mostrar la interfaz gráfica de usuario (GUI)
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("PluzTriviaPeru");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new Inicio());
        frame.setVisible(true);
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Inicio::createAndShowGUI);
    }
}

