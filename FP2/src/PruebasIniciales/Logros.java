package PruebasIniciales;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javafx.embed.swing.JFXPanel;
import java.awt.*;
import java.sql.*;

public class Logros {

    private static final String LOGROS_IMAGE_PATH = "../imagenes/img1_fondo.jpg"; // Ruta a la imagen de fondo de Logros

    private Inicio inicio;

    public Logros(Inicio inicio) {
        this.inicio = inicio;
    }

    public void mostrarPanelLogros(JLayeredPane layeredPane, JFXPanel fxPanel) {
        JPanel overlayPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon(getClass().getResource(LOGROS_IMAGE_PATH));
                Image background = backgroundIcon.getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Panel con la tabla centrada
        JPanel logrosPanel = new JPanel(new BorderLayout());
        logrosPanel.setOpaque(false);

        String[] columnNames = {"Nombre", "Puntaje"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT nombre, puntaje FROM usuarios ORDER BY puntaje DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int puntaje = rs.getInt("puntaje");
                model.addRow(new Object[]{nombre, puntaje});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(inicio, "Error al cargar los logros", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 18)); // Fuente más grande para el contenido de la tabla
        table.setRowHeight(30); // Aumenta la altura de las filas
        table.setBackground(Color.WHITE); // Fondo amarillo para la tabla

        // Aumentar el tamaño de la fuente del encabezado de la tabla
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente más grande para "Nombre" y "Puntaje"
        header.setBackground(Color.WHITE); // Fondo amarillo para el encabezado

        // Aumentar el tamaño de la tabla
        table.setPreferredScrollableViewportSize(new Dimension(700, 400));

        JScrollPane scrollPane = new JScrollPane(table);
        logrosPanel.add(scrollPane, BorderLayout.CENTER);

        // Agregar botón Salir
        JButton salirButton = new JButton("Salir");
        salirButton.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente más grande para el botón
        salirButton.setBackground(Color.RED); // Fondo rojo para el botón
        salirButton.setForeground(Color.WHITE); // Texto blanco para el botón
        salirButton.addActionListener(e -> {
            layeredPane.remove(overlayPanel);
            inicio.mostrarPanelInicio();
        });
        logrosPanel.add(salirButton, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        overlayPanel.add(logrosPanel, gbc);

        layeredPane.removeAll();
        layeredPane.add(fxPanel, Integer.valueOf(1));
        layeredPane.add(overlayPanel, Integer.valueOf(2));
        layeredPane.repaint();
        layeredPane.revalidate();
    }
}


