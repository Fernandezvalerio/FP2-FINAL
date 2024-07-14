package MIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Prueba extends JPanel {

    private BufferedImage imagenFondo;

    public Prueba() {
        try {
            // Cargar la imagen 
            imagenFondo = ImageIO.read(getClass().getResource("/imagenes/img1.jpg"));
            if (imagenFondo == null) {
                System.out.println("No se pudo cargar la imagen.");
            }
        } catch (IOException | IllegalArgumentException ex) {
            ex.printStackTrace();
            System.out.println("Excepción al intentar cargar la imagen: " + ex.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.RED);
            g.drawString("No se pudo cargar la imagen.", 20, 30);
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Texto sobre la imagen", 20, 30);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Panel con Imagen de Fondo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.add(new Prueba());
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Prueba::createAndShowGUI);
    }
}




