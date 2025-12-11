package com.universidad;

import com.universidad.dao.ConexionDB;
import com.universidad.view.MainFrame;
import javax.swing.*;
//Clase principal del sistema
public class Main {
    
    public static void Main(String[] args) {
        // Verificar conexión a base de datos antes de iniciar
        System.out.println("==============================================");
        System.out.println("  SISTEMA UNIVERSIDAD - VERSIÓN DESKTOP");
        System.out.println("==============================================\n");
        
        System.out.println("Verificando conexión a base de datos...");
        
        if (!ConexionDB.verificarConexion()) {
            System.err.println("\nERROR: No se pudo conectar a la base de datos");
            System.err.println("Verifique que:");
            System.err.println("  1. MySQL está ejecutándose");
            System.err.println("  2. La base de datos 'universidad_db' existe");
            System.err.println("  3. El usuario y contraseña son correctos");
            System.err.println("  4. El archivo config/database.properties está configurado");
            System.err.println("\n" + ConexionDB.obtenerInfoConexion());
            // Mostrar diálogo de error
            JOptionPane.showMessageDialog(
                null,
                "No se pudo conectar a la base de datos.\n" +
                "Verifique la configuración e intente nuevamente.",
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE
            );
            
            System.exit(1);
        }
        
        System.out.println("Conexión exitosa a la base de datos\n");
        System.out.println("Iniciando interfaz gráfica...\n");
        // Configurar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer Look and Feel: " + e.getMessage());
        }
        // Iniciar la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                System.out.println("Aplicación iniciada correctamente");
            } catch (Exception e) {
                System.err.println("Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(
                    null,
                    "Error al iniciar la aplicación:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}