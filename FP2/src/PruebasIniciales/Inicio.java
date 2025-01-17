package PruebasIniciales;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;

public class Inicio extends JPanel {

    private static final String VIDEO_PATH = "../video/fondo.mp4"; // Ruta al archivo de video
    private static final String SOUND_PATH = "../sonidos/inicio.wav"; // Ruta al archivo de sonido
    private static final String PREGUNTAS_SOUND_PATH = "../sonidos/panel.wav"; // Ruta al archivo de sonido de preguntas
    private final String CREDITOS_IMAGE_PATH = "../imagenes/img1_fondo.jpg";
    private MediaPlayer mediaPlayer;

    private String nombreUsuario;
    private String titulo = "PluzTrivERu";
    private String subtitulo = "Aprende el que pregunta Edición Perú";
    private JLayeredPane layeredPane;
    private JPanel overlayPanel;
    private JFXPanel fxPanel;
    private Clip currentClip;

    public Inicio() {
        setLayout(new BorderLayout());

        // Crear el JLayeredPane
        layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        // Crear un JPanel con BorderLayout para el fondo de video
        JPanel videoPanel = new JPanel(new BorderLayout());
        videoPanel.setBounds(0, 0, getWidth(), getHeight());

        // Crear el JFXPanel para el video de fondo
        fxPanel = new JFXPanel();
        videoPanel.add(fxPanel, BorderLayout.CENTER);
        layeredPane.add(videoPanel, Integer.valueOf(1));

        // Iniciar el entorno de JavaFX
        Platform.runLater(() -> initFX(fxPanel));

        // Reproducir sonido de inicio
        currentClip = SoundUtils.playSound(SOUND_PATH);

        // Crear y configurar el panel de botones y título
        mostrarPanelInicio();

        // Escuchar cambios de tamaño en el JFrame
        layeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                Dimension size = layeredPane.getSize();
                videoPanel.setSize(size);
                fxPanel.setSize(size);
                if (overlayPanel != null) {
                    overlayPanel.setSize(size);
                }
            }
        });
    }

    void mostrarPanelInicio() {
        overlayPanel = new JPanel(new GridBagLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, getWidth(), getHeight());
        GridBagConstraints gbc = new GridBagConstraints();

        // Título
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(Color.YELLOW);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        overlayPanel.add(titleLabel, gbc);

        // Subtítulo
        JLabel subtitleLabel = new JLabel(subtitulo);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 30));
        subtitleLabel.setForeground(Color.YELLOW);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 20, 0);
        overlayPanel.add(subtitleLabel, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.setOpaque(false);

        JButton iniciarJuegoButton = new JButton("Iniciar Juego");
        iniciarJuegoButton.setFont(new Font("Arial", Font.BOLD, 30));
        iniciarJuegoButton.setBackground(Color.RED);
        iniciarJuegoButton.setForeground(Color.WHITE);
        addHoverEffect(iniciarJuegoButton);
        buttonPanel.add(iniciarJuegoButton);

        JButton logrosButton = new JButton("Logros");
        logrosButton.setFont(new Font("Arial", Font.BOLD, 30));
        logrosButton.setBackground(Color.RED);
        logrosButton.setForeground(Color.WHITE);
        addHoverEffect(logrosButton);
        buttonPanel.add(logrosButton);

        JButton creditosButton = new JButton("Créditos");
        creditosButton.setFont(new Font("Arial", Font.BOLD, 30));
        creditosButton.setBackground(Color.RED);
        creditosButton.setForeground(Color.WHITE);
        addHoverEffect(creditosButton);
        buttonPanel.add(creditosButton);

        JButton salirButton = new JButton("Salir");
        salirButton.setFont(new Font("Arial", Font.BOLD, 30));
        salirButton.setBackground(Color.RED);
        salirButton.setForeground(Color.WHITE);
        addHoverEffect(salirButton);
        buttonPanel.add(salirButton);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        overlayPanel.add(buttonPanel, gbc);

        // Añadir el overlayPanel al layeredPane
        layeredPane.add(overlayPanel, Integer.valueOf(2));

        // Añadir ActionListeners a los botones
        iniciarJuegoButton.addActionListener(e -> mostrarPanelNombre());
        logrosButton.addActionListener(e -> {
            Logros logros = new Logros(this);
            logros.mostrarPanelLogros(layeredPane, fxPanel);
        });
        creditosButton.addActionListener(e -> {
            Creditos creditos = new Creditos(this);
            creditos.mostrarPanelCreditos(layeredPane, fxPanel);
        });
        salirButton.addActionListener(e -> System.exit(0));
    }

    private void addHoverEffect(JButton button) {
        Font originalFont = button.getFont();
        Font hoverFont = originalFont.deriveFont(originalFont.getSize() * 1.2f);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setFont(hoverFont);
            }

            public void mouseExited(MouseEvent e) {
                button.setFont(originalFont);
            }
        });
    }

    private void initFX(JFXPanel fxPanel) {
        // Crear el objeto Media
        Media media = new Media(getClass().getResource(VIDEO_PATH).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop infinito
        mediaPlayer.play();

        // Crear el MediaView
        MediaView mediaView = new MediaView(mediaPlayer);
        Pane pane = new Pane();
        pane.getChildren().add(mediaView);

        // Ajustar el tamaño del video al tamaño del panel
        mediaView.fitWidthProperty().bind(pane.widthProperty());
        mediaView.fitHeightProperty().bind(pane.heightProperty());
        mediaView.setPreserveRatio(false);

        // Crear el Scene y agregar el MediaView
        Scene scene = new Scene(pane);
        fxPanel.setScene(scene);
    }

    private void mostrarImagenFondo() {
        overlayPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon(getClass().getResource(CREDITOS_IMAGE_PATH));
                Image background = backgroundIcon.getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, getWidth(), getHeight());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Configurar y añadir el panel de título y botones
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.gridwidth = 2;
        mainGbc.insets = new Insets(20, 0, 20, 0);

        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(Color.YELLOW);
        mainPanel.add(titleLabel, mainGbc);

        mainGbc.gridy = 1;
        mainGbc.insets = new Insets(5, 0, 20, 0);

        JLabel subtitleLabel = new JLabel(subtitulo);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 30));
        subtitleLabel.setForeground(Color.YELLOW);
        mainPanel.add(subtitleLabel, mainGbc);

        mainGbc.gridy = 2;
        mainGbc.insets = new Insets(10, 10, 10, 10);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setOpaque(false);

        JButton iniciarJuegoButton = new JButton("Iniciar Juego");
        iniciarJuegoButton.setFont(new Font("Arial", Font.BOLD, 30));
        iniciarJuegoButton.setBackground(Color.RED);
        iniciarJuegoButton.setForeground(Color.WHITE);
        addHoverEffect(iniciarJuegoButton);
        buttonPanel.add(iniciarJuegoButton);

        JButton logrosButton = new JButton("Logros");
        logrosButton.setFont(new Font("Arial", Font.BOLD, 30));
        logrosButton.setBackground(Color.RED);
        logrosButton.setForeground(Color.WHITE);
        addHoverEffect(logrosButton);
        buttonPanel.add(logrosButton);

        JButton creditosButton = new JButton("Créditos");
        creditosButton.setFont(new Font("Arial", Font.BOLD, 30));
        creditosButton.setBackground(Color.RED);
        creditosButton.setForeground(Color.WHITE);
        addHoverEffect(creditosButton);
        buttonPanel.add(creditosButton);

        JButton salirButton = new JButton("Salir");
        salirButton.setFont(new Font("Arial", Font.BOLD, 30));
        salirButton.setBackground(Color.RED);
        salirButton.setForeground(Color.WHITE);
        addHoverEffect(salirButton);
        buttonPanel.add(salirButton);

        mainPanel.add(buttonPanel, mainGbc);
        overlayPanel.add(mainPanel, gbc);

        iniciarJuegoButton.addActionListener(e -> mostrarPanelNombre());
        logrosButton.addActionListener(e -> {
            Logros logros = new Logros(this);
            logros.mostrarPanelLogros(layeredPane, fxPanel);
        });
        creditosButton.addActionListener(e -> {
            Creditos creditos = new Creditos(this);
            creditos.mostrarPanelCreditos(layeredPane, fxPanel);
        });
        salirButton.addActionListener(e -> System.exit(0));

        layeredPane.removeAll();
        layeredPane.add(overlayPanel, Integer.valueOf(2)); // Añadir el panel de la imagen en la capa superior
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
        repaint();
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
        repaint();
    }

    private void mostrarPanelNombre() {
        JTextField nombreField = new JTextField(20);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Ingrese su nombre:"));
        panel.add(nombreField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Nombre de Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            nombreUsuario = nombreField.getText();
            guardarUsuarioEnBD();
            mostrarPanelCategorias();
            // Añadir reproducción de sonido después de mostrar las categorías
            stopCurrentClip();
            currentClip = SoundUtils.playSound(PREGUNTAS_SOUND_PATH);
        }
    }

    private void guardarUsuarioEnBD() {
        try (Connection conn = Conexion.getConnection()) {
            String query = "INSERT INTO usuarios (nombre, puntaje) VALUES (?, 0)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombreUsuario);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void mostrarPanelCategorias() {
        overlayPanel = new JPanel(new GridLayout(1, 4));
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, getWidth(), getHeight());

        // Cargar las imágenes
        ImageIcon historiaIcon = new ImageIcon(getClass().getResource("../imagenes/historia.png"));
        ImageIcon geografiaIcon = new ImageIcon(getClass().getResource("../imagenes/geografia.png"));
        ImageIcon gastronomiaIcon = new ImageIcon(getClass().getResource("../imagenes/gastronomia.png"));
        ImageIcon mitologiaIcon = new ImageIcon(getClass().getResource("../imagenes/mitologia.png"));

        // Crear los botones con las imágenes de fondo
        JButton historiaButton = new ImageButton("Historia", historiaIcon);
        JButton geografiaButton = new ImageButton("Geografía", geografiaIcon);
        JButton gastronomiaButton = new ImageButton("Gastronomía", gastronomiaIcon);
        JButton mitologiaButton = new ImageButton("Mitologia", mitologiaIcon);

        addHoverEffect(historiaButton);
        addHoverEffect(geografiaButton);
        addHoverEffect(gastronomiaButton);
        addHoverEffect(mitologiaButton);

        historiaButton.addActionListener(e -> {
            mostrarPreguntasHistoria();
        });

        geografiaButton.addActionListener(e -> {
            mostrarPreguntasGeografia();
        });

        gastronomiaButton.addActionListener(e -> {
           mostrarPreguntasGastronomia(); 
        });

        mitologiaButton.addActionListener(e -> {
            mostrarPreguntasMitologia();
        });

        overlayPanel.add(historiaButton);
        overlayPanel.add(geografiaButton);
        overlayPanel.add(gastronomiaButton);
        overlayPanel.add(mitologiaButton);

        layeredPane.removeAll();
        layeredPane.add(fxPanel, Integer.valueOf(1)); // Asegurarse de que el video esté detrás
        layeredPane.add(overlayPanel, Integer.valueOf(2)); // Añadir el panel de botones en la capa superior
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    private void mostrarPreguntasHistoria() {
        overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, getWidth(), getHeight());

        overlayPanel.add(new PreguntaHistoria(nombreUsuario, this), BorderLayout.CENTER);

        layeredPane.removeAll();
        layeredPane.add(fxPanel, Integer.valueOf(1)); // Asegurarse de que el video esté detrás
        layeredPane.add(overlayPanel, Integer.valueOf(2)); // Añadir el panel de botones en la capa superior
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    private void mostrarPreguntasGeografia() {
        overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, getWidth(), getHeight());

        overlayPanel.add(new PreguntaGeografia(nombreUsuario, this), BorderLayout.CENTER);

        layeredPane.removeAll();
        layeredPane.add(fxPanel, Integer.valueOf(1)); // Asegurarse de que el video esté detrás
        layeredPane.add(overlayPanel, Integer.valueOf(2)); // Añadir el panel de botones en la capa superior
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    private void mostrarPreguntasGastronomia() {
        overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, getWidth(), getHeight());

        overlayPanel.add(new PreguntaGastronomia(nombreUsuario, this), BorderLayout.CENTER);

        layeredPane.removeAll();
        layeredPane.add(fxPanel, Integer.valueOf(1)); // Asegurarse de que el video esté detrás
        layeredPane.add(overlayPanel, Integer.valueOf(2)); // Añadir el panel de botones en la capa superior
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    private void mostrarPreguntasMitologia() {
        overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, getWidth(), getHeight());

        overlayPanel.add(new PreguntaMitologia(nombreUsuario, this), BorderLayout.CENTER);

        layeredPane.removeAll();
        layeredPane.add(fxPanel, Integer.valueOf(1)); // Asegurarse de que el video esté detrás
        layeredPane.add(overlayPanel, Integer.valueOf(2)); // Añadir el panel de botones en la capa superior
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    public void mostrarPantallaInicial() {
        System.out.println("Reconfigurando pantalla inicial...");
        stopCurrentClip();
        currentClip = SoundUtils.playSound(SOUND_PATH); // Reproducir sonido de inicio al volver a la pantalla principal
        Platform.runLater(this::mostrarImagenFondo);
    }

    private void mostrarPanelCreditos() {
        Creditos creditos = new Creditos(this);
        creditos.mostrarPanelCreditos(layeredPane, fxPanel);
    }

    private void mostrarPanelLogros() {
        Logros logros = new Logros(this);
        logros.mostrarPanelLogros(layeredPane, fxPanel);
    }

    private void stopCurrentClip() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("PluzTrivERu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.add(new Inicio());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Iniciar en pantalla completa
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Inicio::createAndShowGUI);
    }
}
