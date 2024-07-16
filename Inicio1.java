package PruebasIniciales;
// Importamos las librerías necesarias para la aplicación
import javax.swing.*; // Para crear componentes de interfaz gráfica (JPanel, JButton, JFrame, etc.)
import java.awt.*; // Para el diseño gráfico (Color, Font, Graphics, etc.)
import java.awt.image.BufferedImage; // Para manejar imágenes en memoria
import java.io.IOException; // Para manejar excepciones de entrada/salida
import java.sql.*; // Para interactuar con la base de datos
import javax.imageio.ImageIO; // Para leer imágenes desde archivos
import javax.swing.table.DefaultTableModel; // Para manejar modelos de tabla en JTable
import java.awt.event.ActionListener; // Para manejar eventos de acción en botones

// Definimos la clase Inicio que extiende JPanel para crear un panel personalizado
public class Inicio extends JPanel {

    // Declaramos una variable para almacenar la imagen de fondo
    private BufferedImage imagenFondo;
    // Variable para almacenar el nombre del usuario
    private String nombreUsuario;

    // Constructor de la clase Inicio
    public Inicio() {
        try {
            // Intentamos cargar la imagen de fondo desde los recursos
            imagenFondo = ImageIO.read(getClass().getResource("/imagenes/img1_fondo.jpg"));
            if (imagenFondo == null) {
                // Si la imagen no se carga correctamente, mostramos un mensaje en la consola
                System.out.println("No se pudo cargar la imagen.");
            }
        } catch (IOException | IllegalArgumentException ex) {
            // Capturamos y mostramos cualquier excepción que ocurra al cargar la imagen
            ex.printStackTrace();
            System.out.println("Excepción al intentar cargar la imagen: " + ex.getMessage());
        }

        // Establecemos el diseño del panel como null para posicionar los componentes manualmente
        setLayout(null);

        // Creamos y configuramos el botón "Iniciar Juego"
        JButton iniciarJuegoButton = new JButton("Iniciar Juego");
        iniciarJuegoButton.setBounds(610, 400, 300, 70); // Posición y tamaño
        iniciarJuegoButton.setFont(new Font("Arial", Font.BOLD, 40)); // Fuente
        iniciarJuegoButton.setBackground(Color.RED); // Color de fondo
        iniciarJuegoButton.setForeground(Color.WHITE); // Color del texto

        // Creamos y configuramos el botón "Salir"
        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(610, 550, 300, 70); // Posición y tamaño
        salirButton.setFont(new Font("Arial", Font.BOLD, 40)); // Fuente
        salirButton.setBackground(Color.RED); // Color de fondo
        salirButton.setForeground(Color.WHITE); // Color del texto

        // Creamos y configuramos el botón "Logros"
        JButton logrosButton = new JButton("Logros");
        logrosButton.setBounds(80, 690, 200, 50); // Posición y tamaño
        logrosButton.setFont(new Font("Arial", Font.BOLD, 20)); // Fuente
        logrosButton.setBackground(Color.RED); // Color de fondo
        logrosButton.setForeground(Color.WHITE); // Color del texto

        // Creamos y configuramos el botón "Créditos"
        JButton CreditosButton = new JButton("Creditos");
        CreditosButton.setBounds(1250, 690, 200, 50); // Posición y tamaño
        CreditosButton.setFont(new Font("Arial", Font.BOLD, 20)); // Fuente
        CreditosButton.setBackground(Color.RED); // Color de fondo
        CreditosButton.setForeground(Color.WHITE); // Color del texto

        // Agregamos los botones al panel
        add(CreditosButton);
        add(iniciarJuegoButton);
        add(logrosButton);
        add(salirButton);

        // Agregamos ActionListener a los botones para manejar los eventos de clic
        iniciarJuegoButton.addActionListener(e -> mostrarPanelNombre()); // Muestra el panel para ingresar el nombre
        salirButton.addActionListener(e -> System.exit(0)); // Sale de la aplicación
        logrosButton.addActionListener(e -> mostrarPanelLogros()); // Muestra el panel de logros
        CreditosButton.addActionListener(e -> mostrarPanelCreditos()); // Muestra el panel de créditos
    }

    // Sobrescribimos el método paintComponent para personalizar la forma en que se pinta el panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Llamamos al método de la superclase para asegurarnos de que el panel se pinta correctamente
        if (imagenFondo != null) {
            // Si la imagen de fondo se ha cargado correctamente, la dibujamos ajustada al tamaño del panel
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Si no se pudo cargar la imagen, mostramos un mensaje de error
            g.setColor(Color.RED);
            g.drawString("No se pudo cargar la imagen.", 20, 30);
        }
        // Dibujamos el título y subtítulo
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 100));
        g.drawString("PluzTriviaPeru", 425, 200);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Aprende el que pregunta Edición Perú", 500, 300);
    }

    //----------------------------------------------PRIMER PANEL--------------------------------------------------------------//

    // Método para mostrar el panel de ingreso del nombre
    private void mostrarPanelNombre() {
        // Creamos un campo de texto para ingresar el nombre
        JTextField nombreField = new JTextField(20);
        // Creamos un panel y le agregamos una etiqueta y el campo de texto
        JPanel panel = new JPanel();
        panel.add(new JLabel("Ingrese su nombre:"));
        panel.add(nombreField);

        // Mostramos un cuadro de diálogo para que el usuario ingrese su nombre
        int result = JOptionPane.showConfirmDialog(null, panel, "Nombre de Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Si el usuario presiona OK, guardamos el nombre ingresado
            nombreUsuario = nombreField.getText();
            // Guardamos el nombre de usuario en la base de datos
            guardarUsuarioEnBD();
            // Mostramos el panel de categorías
            mostrarPanelCategorias();
        }
    }

    // Método para guardar el usuario en la base de datos
    private void guardarUsuarioEnBD() {
        try (Connection conn = Conexion.getConnection()) {
            // Creamos una consulta SQL para insertar el nombre de usuario y un puntaje inicial de 0
            String query = "INSERT INTO usuarios (nombre, puntaje) VALUES (?, 0)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombreUsuario); // Establecemos el nombre de usuario en la consulta
            stmt.executeUpdate(); // Ejecutamos la consulta
        } catch (SQLException ex) {
            // Si ocurre un error, lo mostramos en la consola
            ex.printStackTrace();
        }
    }

    // Método para mostrar el panel de categorías
    private void mostrarPanelCategorias() {
        // Creamos un nuevo marco para las categorías
        JFrame frame = new JFrame("Categorías");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerramos el marco al cerrar
        frame.setSize(400, 300); // Establecemos el tamaño del marco
        frame.setLayout(new GridLayout(4, 1)); // Establecemos un diseño de cuadrícula de 4 filas y 1 columna

        // Creamos los botones para cada categoría
        JButton historiaButton = new JButton("Historia");
        JButton geografiaButton = new JButton("Geografía");
        JButton gastronomiaButton = new JButton("Gastronomía");
        JButton cienciaButton = new JButton("Ciencia");

        // Agregamos un ActionListener al botón de geografía para mostrar las preguntas de geografía
        geografiaButton.addActionListener(e -> mostrarPreguntasGeografia());

        // Agregamos los botones al marco
        frame.add(historiaButton);
        frame.add(geografiaButton);
        frame.add(gastronomiaButton);
        frame.add(cienciaButton);

        // Hacemos visible el marco
        frame.setVisible(true);
    }

    // Método para mostrar las preguntas de Geografía
    private void mostrarPreguntasGeografia() {
        // Creamos un nuevo marco para las preguntas de geografía
        JFrame frame = new JFrame("Geografía");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerramos el marco al cerrar
        frame.setSize(800, 600); // Establecemos el tamaño del marco
        // Agregamos un nuevo componente PreguntaGeografia al marco, pasando el nombre del usuario
        frame.add(new PreguntaGeografia(nombreUsuario));
        // Hacemos visible el marco
        frame.setVisible(true);
    }

    // Método para mostrar el panel de Créditos
    private void mostrarPanelCreditos() {
        // Creamos un nuevo marco para los créditos
        JFrame frame = new JFrame("Créditos");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerramos el marco al cerrar
        frame.setSize(400, 300); // Establecemos el tamaño del marco
        frame.setLayout(new GridLayout(5, 1)); // Establecemos un diseño de cuadrícula de 5 filas y 1 columna

        // Creamos los botones para cada crédito
        JButton valerioButton = new JButton("Valerio");
        JButton valerio2Button = new JButton("Valerio2");
        JButton valio3Button = new JButton("Valio3");
        JButton valerio4Button = new JButton("Valerio4");
        JButton salirButton = new JButton("Salir");

        // Agregamos un ActionListener al botón de salir para cerrar el marco
        salirButton.addActionListener(e -> frame.dispose());

        // Agregamos los botones al marco
        frame.add(valerioButton);
        frame.add(valerio2Button);
        frame.add(valio3Button);
        frame.add(valerio4Button);
        frame.add(salirButton);

        // Hacemos visible el marco
        frame.setVisible(true);
    }

    // Método para mostrar el panel de Logros
    private void mostrarPanelLogros() {
        // Creamos un nuevo marco para los logros
        JFrame frame = new JFrame("Logros");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerramos el marco al cerrar
        frame.setSize(600, 400); // Establecemos el tamaño del marco

        // Definimos los nombres de las columnas de la tabla
        String[] columnNames = {"Nombre", "Puntaje"};
        // Creamos un modelo de tabla con las columnas definidas
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Intentamos obtener los datos de los usuarios desde la base de datos
        try (Connection conn = Conexion.getConnection()) {
            // Creamos una consulta SQL para obtener los nombres y puntajes de los usuarios, ordenados por puntaje descendente
            String query = "SELECT nombre, puntaje FROM usuarios ORDER BY puntaje DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Iteramos sobre los resultados de la consulta y los agregamos al modelo de la tabla
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int puntaje = rs.getInt("puntaje");
                model.addRow(new Object[]{nombre, puntaje});
            }
        } catch (SQLException ex) {
            // Si ocurre un error, lo mostramos en la consola
            ex.printStackTrace();
        }

        // Creamos una tabla con el modelo de tabla
        JTable table = new JTable(model);
        // Creamos un panel de desplazamiento y le agregamos la tabla
        JScrollPane scrollPane = new JScrollPane(table);
        // Agregamos el panel de desplazamiento al marco
        frame.add(scrollPane, BorderLayout.CENTER);
        // Hacemos visible el marco
        frame.setVisible(true);
    }

    // Método para crear y mostrar la interfaz gráfica de usuario (GUI)
    private static void createAndShowGUI() {
        // Creamos un nuevo marco para la aplicación principal
        JFrame frame = new JFrame("PluzTriviaPeru");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerramos la aplicación al cerrar el marco
        frame.setSize(1550, 820); // Establecemos el tamaño del marco
        // Agregamos una instancia de la clase Inicio al marco
        frame.add(new Inicio());
        // Hacemos visible el marco
        frame.setVisible(true);
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        // Utilizamos SwingUtilities para asegurarnos de que la creación y actualización de la GUI se realicen en el hilo de despacho de eventos
        SwingUtilities.invokeLater(Inicio::createAndShowGUI);
    }
}





