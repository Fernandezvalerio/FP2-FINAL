package PruebasIniciales;

import javax.swing.*;
import javafx.embed.swing.JFXPanel;
import java.awt.*;

public class Creditos {

    private static final String CREDITOS_IMAGE_PATH = "../imagenes/img_creditos1.png"; // Ruta a la imagen de fondo de Créditos

    private Inicio inicio;

    public Creditos(Inicio inicio) {
        this.inicio = inicio;
    }

    public void mostrarPanelCreditos(JLayeredPane layeredPane, JFXPanel fxPanel) {
        JPanel overlayPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon(getClass().getResource(CREDITOS_IMAGE_PATH));
                Image background = backgroundIcon.getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());

        // Botones de crédito
        int buttonWidth = 300;
        int buttonHeight = 150;
        int buttonSpacing = 30;
        int centerX = (overlayPanel.getWidth() - buttonWidth) / 2 + 150;
        int centerY = (overlayPanel.getHeight() - (buttonHeight * 2 + buttonSpacing)) / 2 + 200;

        JButton andreButton = crearBoton("Andre", "mensaje 1");
        andreButton.setBounds(centerX - buttonWidth - buttonSpacing / 2, centerY - buttonHeight - buttonSpacing, buttonWidth, buttonHeight);

        JButton valerioButton = crearBoton("Valerio", "mensaje 2");
        valerioButton.setBounds(centerX + buttonSpacing / 2, centerY - buttonHeight - buttonSpacing, buttonWidth, buttonHeight);

        JButton sadithButton = crearBoton("Sadith", "mensaje 3");
        sadithButton.setBounds(centerX - buttonWidth - buttonSpacing / 2, centerY + buttonSpacing / 2, buttonWidth, buttonHeight);

        JButton anthonyButton = crearBoton("Anthony", "mensaje 4");
        anthonyButton.setBounds(centerX + buttonSpacing / 2, centerY + buttonSpacing / 2, buttonWidth, buttonHeight);

        JButton salirButton = new JButton("SALIR");
        salirButton.setBounds(centerX - buttonWidth / 2, overlayPanel.getHeight()+100 - buttonHeight - 100, buttonWidth, 50);
        salirButton.setBackground(Color.RED);
        salirButton.setForeground(Color.WHITE);
        salirButton.setFont(new Font("Arial", Font.BOLD, 24));
        salirButton.addActionListener(e -> {
            layeredPane.remove(overlayPanel);
            inicio.mostrarPanelInicio();
        });

        overlayPanel.add(andreButton);
        overlayPanel.add(valerioButton);
        overlayPanel.add(sadithButton);
        overlayPanel.add(anthonyButton);
        overlayPanel.add(salirButton);

        layeredPane.removeAll();
        layeredPane.add(fxPanel, Integer.valueOf(1)); // Asegurarse de que el video esté detrás
        layeredPane.add(overlayPanel, Integer.valueOf(2)); // Añadir el panel de botones en la capa superior
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    private JButton crearBoton(String nombre, String mensaje) {
        JButton button = new JButton(nombre);
        button.setFont(new Font("Arial", Font.BOLD, 30));
        button.setBackground(Color.CYAN);
        button.setForeground(Color.BLACK);
        button.setOpaque(true);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        return button;
    }
}








