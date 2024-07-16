package PruebasIniciales;
import javax.swing.*;
import java.awt.*;

public class ImageButton extends JButton {
    private Image backgroundImage;

    public ImageButton(String text, ImageIcon icon) {
        super(text);
        this.backgroundImage = icon.getImage();
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);
        setForeground(Color.YELLOW); // Para que el texto sea visible sobre la imagen
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar el texto con contorno
        String text = getText();
        Font font = getFont().deriveFont(Font.BOLD, 30f); // Aumentar el tamaño de la fuente
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() + fm.getAscent()) / 2 - fm.getDescent();

        // Dibujar el contorno del texto
        g2.setColor(Color.BLACK);
        g2.drawString(text, x - 1, y - 1);
        g2.drawString(text, x - 1, y + 1);
        g2.drawString(text, x + 1, y - 1);
        g2.drawString(text, x + 1, y + 1);
        g2.drawString(text, x - 2, y);
        g2.drawString(text, x + 2, y);
        g2.drawString(text, x, y - 2);
        g2.drawString(text, x, y + 2);

        // Dibujar el texto principal
        g2.setColor(Color.YELLOW);
        g2.drawString(text, x, y);

        g2.dispose();
        super.paintComponent(g);
    }
}

