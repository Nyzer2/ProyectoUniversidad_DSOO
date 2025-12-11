package com.universidad.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
//Clase para gestionar conexiones a MySQL
//Sin pool de conexiones externo - usa conexiones directas
public class ConexionDB {
    
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    static {
        cargarConfiguracion();
    }
    // Carga la configuración desde archivo properties
    private static void cargarConfiguracion() {
        Properties props = new Properties();
        try {
            // Intentar cargar desde archivo
            FileInputStream fis = new FileInputStream("config/database.properties");
            props.load(fis);
            fis.close();
            
            URL = props.getProperty("db.url");
            USERNAME = props.getProperty("db.username");
            PASSWORD = props.getProperty("db.password");
            DRIVER = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            
        } catch (IOException e) {
            // Si no existe el archivo, usar valores por defecto
            System.out.println("Usando configuración por defecto");
            URL = "jdbc:mysql://localhost:3306/universidad_db?useSSL=false&serverTimezone=UTC";
            USERNAME = "app_usuario";
            PASSWORD = "app_password_123";
        }
    }
    //Obtiene una nueva conexión a la base de datos
    public static Connection obtenerConexion() throws SQLException {
        try {
            // Cargar el driver JDBC de MySQL
            Class.forName(DRIVER);
            // Crear y retornar la conexión
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            // Configurar la conexión
            conn.setAutoCommit(true);
            
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Driver MySQL no encontrado");
            System.err.println("Asegúrese de tener mysql-connector-java-8.0.33.jar en la carpeta lib/");
            throw new SQLException("Driver no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("ERROR: No se pudo conectar a la base de datos");
            System.err.println("URL: " + URL);
            System.err.println("Usuario: " + USERNAME);
            throw new SQLException("Error de conexión: " + e.getMessage());
        }
    }
    // Cierra una conexión de forma segura
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
    // Verifica si la conexión a la base de datos es exitosa
    public static boolean verificarConexion() {
        Connection conn = null;
        try {
            conn = obtenerConexion();
            return true;
        } catch (SQLException e) {
            System.err.println("No se pudo conectar a la base de datos: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion(conn);
        }
    }
    // Obtiene información de la configuración de la base de datos
    public static String obtenerInfoConexion() {
        return String.format(
            "Configuración de Base de Datos:\n" +
            "URL: %s\n" +
            "Usuario: %s\n" +
            "Driver: %s",
            URL, USERNAME, DRIVER
        );
    }
}
