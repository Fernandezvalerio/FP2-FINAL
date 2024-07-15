package PruebasIniciales;

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
            imagenFondo = ImageIO.read(getClass().getResource("/imagenes/img1_fondo.jpg"));
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
        iniciarJuegoButton.setBounds(610, 400, 300, 70);
        iniciarJuegoButton.setFont(new Font("Arial", Font.BOLD, 40));
        iniciarJuegoButton.setBackground(Color.RED);
        iniciarJuegoButton.setForeground(Color.WHITE);

        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(610, 550, 300, 70);
        salirButton.setFont(new Font("Arial", Font.BOLD, 40));
        salirButton.setBackground(Color.RED);
        salirButton.setForeground(Color.WHITE);

        JButton logrosButton = new JButton("Logros");
        logrosButton.setBounds(80, 690, 200, 50);
        logrosButton.setFont(new Font("Arial", Font.BOLD, 20));
        logrosButton.setBackground(Color.RED);
        logrosButton.setForeground(Color.WHITE);

        JButton CreditosButton = new JButton("Creditos");
        CreditosButton.setBounds(1250, 690, 200, 50);
        CreditosButton.setFont(new Font("Arial", Font.BOLD, 20));
        CreditosButton.setBackground(Color.RED);
        CreditosButton.setForeground(Color.WHITE);

        // Agregar los botones al panel
        add(CreditosButton);
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
        g.setFont(new Font("Arial", Font.BOLD, 100));
        g.drawString("PluzTriviaPeru", 425, 200);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Aprende el que pregunta Edición Perú", 500, 300);
    }

    // Método para crear y mostrar la interfaz gráfica de usuario (GUI)
    // Panel base para crear todo
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("PluzTriviaPeru");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1550, 820);
        frame.add(new Inicio());
        frame.setVisible(true);
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Inicio::createAndShowGUI);
    }
}

