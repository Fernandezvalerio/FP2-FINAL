package PruebasIniciales;

import java.sql.*;

public class Conexion {
    // Constantes de configuración de la base de datos
    private static final String HOST = "localhost"; // Dirección del host donde está la base de datos
    private static final String PORT = "3307"; // Puerto de la base de datos, cambiar a 3306 si es necesario
    private static final String DB_NAME = "triviadb"; // Nombre de la base de datos
    private static final String USERNAME = "root"; // Nombre de usuario para la conexión
    private static final String PASSWORD = "Plateado"; // Contraseña para la conexión

    // Método para obtener la conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        // Construcción de la URL de conexión usando las constantes
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        // Retorna una conexión a la base de datos usando DriverManager
        return DriverManager.getConnection(url, USERNAME, PASSWORD);
    }
}


