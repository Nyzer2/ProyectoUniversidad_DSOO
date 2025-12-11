package com.universidad.view;

import com.universidad.service.UniversidadService;
import javax.swing.*;
import java.awt.*;

// Panel del dashboard con estadísticas y bienvenida
public class DashboardPanel extends JPanel {
    
    private UniversidadService service;
    
    // Componentes
    private JLabel lblTotalAlumnos;
    private JLabel lblTotalCarreras;
    private JLabel lblTotalMatriculas;
    
    public DashboardPanel(UniversidadService service) {
        this.service = service;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Título
        JLabel titulo = new JLabel("Dashboard");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);
        // Panel de estadísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(Color.WHITE);
        
        statsPanel.add(crearTarjetaEstadistica("Alumnos", "0", new Color(13, 110, 253)));
        statsPanel.add(crearTarjetaEstadistica("Carreras", "0", new Color(25, 135, 84)));
        statsPanel.add(crearTarjetaEstadistica("Matrículas", "0", new Color(255, 193, 7)));
        
        add(statsPanel, BorderLayout.CENTER);
        // Panel de información
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(13, 110, 253), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JTextArea infoText = new JTextArea();
        infoText.setEditable(false);
        infoText.setBackground(new Color(240, 248, 255));
        infoText.setFont(new Font("Arial", Font.PLAIN, 14));
        infoText.setText(
            "Bienvenido al Sistema de Gestión Universitaria\n\n" +
            "Funcionalidades disponibles:\n" +
            "• Gestión de Alumnos: Registrar, editar y consultar información de estudiantes\n" +
            "• Gestión de Carreras: Administrar carreras y planes de estudio\n" +
            "• Gestión de Matrículas: Matricular alumnos en cursos y asignar notas\n\n" +
            "Use el menú lateral para navegar entre las diferentes secciones."
        );
        
        infoPanel.add(infoText);
        add(infoPanel, BorderLayout.SOUTH);
        
        actualizarDatos();
    }
    //Crea una tarjeta de estadística
    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(30, 20, 30, 20)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 48));
        lblValor.setForeground(Color.WHITE);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Guardar referencia según el título
        if (titulo.contains("Alumnos")) {
            lblTotalAlumnos = lblValor;
        } else if (titulo.contains("Carreras")) {
            lblTotalCarreras = lblValor;
        } else if (titulo.contains("Matrículas")) {
            lblTotalMatriculas = lblValor;
        }
        
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblValor);
        
        return card;
    }
    //Actualiza las estadísticas del dashboard
    public void actualizarDatos() {
        int totalAlumnos = service.listarAlumnos().size();
        int totalCarreras = service.listarCarreras().size();
        
        if (lblTotalAlumnos != null) {
            lblTotalAlumnos.setText(String.valueOf(totalAlumnos));
        }
        if (lblTotalCarreras != null) {
            lblTotalCarreras.setText(String.valueOf(totalCarreras));
        }
        if (lblTotalMatriculas != null) {
            // Calcular total de matrículas
            int totalMatriculas = 0;
            for (var alumno : service.listarAlumnos()) {
                totalMatriculas += alumno.getMatriculas().size();
            }
            lblTotalMatriculas.setText(String.valueOf(totalMatriculas));
        }
    }
}
