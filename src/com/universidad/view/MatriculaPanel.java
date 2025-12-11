package com.universidad.view;

import com.universidad.model.*;
import com.universidad.service.UniversidadService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
//Panel para gestionar matrículas
public class MatriculaPanel extends JPanel {
    
    private UniversidadService service;
    // Componentes
    private JComboBox<String> cmbAlumno;
    private JComboBox<String> cmbCarrera;
    private JComboBox<String> cmbCurso;
    private JButton btnMatricular;
    
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    
    public MatriculaPanel(UniversidadService service) {
        this.service = service;
        initComponents();
        actualizarDatos();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Título
        JLabel titulo = new JLabel("Gestión de Matrículas");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);
        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Matricular Alumno en Curso"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        // Selección de alumno
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Alumno:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbAlumno = new JComboBox<>();
        cmbAlumno.setPreferredSize(new Dimension(300, 30));
        formPanel.add(cmbAlumno, gbc);
        // Selección de carrera
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Carrera:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbCarrera = new JComboBox<>();
        cmbCarrera.setPreferredSize(new Dimension(300, 30));
        cmbCarrera.addActionListener(e -> cargarCursos());
        formPanel.add(cmbCarrera, gbc);
        // Selección de curso
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Curso:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbCurso = new JComboBox<>();
        cmbCurso.setPreferredSize(new Dimension(300, 30));
        formPanel.add(cmbCurso, gbc);
        // Botón matricular
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnMatricular = new JButton("✓ Matricular");
        btnMatricular.setFont(new Font("Arial", Font.BOLD, 14));
        btnMatricular.setBackground(COLOR_PRIMARY);
        btnMatricular.setForeground(Color.WHITE);
        btnMatricular.setFocusPainted(false);
        btnMatricular.setPreferredSize(new Dimension(200, 40));
        btnMatricular.addActionListener(e -> matricularAlumno());
        formPanel.add(btnMatricular, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        // Panel de información
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(255, 252, 231));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 193, 7), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JTextArea infoText = new JTextArea();
        infoText.setEditable(false);
        infoText.setBackground(new Color(255, 252, 231));
        infoText.setFont(new Font("Arial", Font.PLAIN, 12));
        infoText.setText(
            "Información Importante:\n\n" +
            "• El sistema validará automáticamente los prerequisitos del curso\n" +
            "• El límite máximo es de 22 créditos por ciclo\n" +
            "• El alumno debe estar inscrito en la carrera antes de matricularse\n" +
            "• No se puede matricular dos veces en el mismo curso"
        );
        infoPanel.add(infoText);
        
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    public void actualizarDatos() {
        // Cargar alumnos
        cmbAlumno.removeAllItems();
        cmbAlumno.addItem("Seleccione un alumno...");
        
        for (Alumno alumno : service.listarAlumnos()) {
            String item = alumno.obtenerCui() + " - " + alumno.getNombreCompleto();
            cmbAlumno.addItem(item);
        }
        // Cargar carreras
        cmbCarrera.removeAllItems();
        cmbCarrera.addItem("Seleccione una carrera...");
        
        for (Carrera carrera : service.listarCarreras()) {
            String item = carrera.getCodigo() + " - " + carrera.getNombre();
            cmbCarrera.addItem(item);
        }
    }
    
    private void cargarCursos() {
        cmbCurso.removeAllItems();
        cmbCurso.addItem("Seleccione un curso...");
        
        int indexCarrera = cmbCarrera.getSelectedIndex();
        if (indexCarrera <= 0) return;
        // Obtener código de carrera
        String itemCarrera = (String) cmbCarrera.getSelectedItem();
        String codigoCarrera = itemCarrera.split(" - ")[0];
        // Cargar cursos de la carrera
        Carrera carrera = service.buscarCarreraPorCodigo(codigoCarrera);
        if (carrera != null) {
            for (CursoCarrera curso : carrera.getPlanDeEstudio()) {
                String item = curso.getCodigo() + " - " + curso.getNombre() + 
                             " (" + curso.getCreditos() + " créditos)";
                cmbCurso.addItem(item);
            }
        }
    }
    
    private void matricularAlumno() {
        // Validar selecciones
        if (cmbAlumno.getSelectedIndex() <= 0) {
            mostrarError("Por favor, seleccione un alumno");
            return;
        }
        
        if (cmbCarrera.getSelectedIndex() <= 0) {
            mostrarError("Por favor, seleccione una carrera");
            return;
        }
        
        if (cmbCurso.getSelectedIndex() <= 0) {
            mostrarError("Por favor, seleccione un curso");
            return;
        }
        
        try {
            // Obtener CUI del alumno
            String itemAlumno = (String) cmbAlumno.getSelectedItem();
            String cui = itemAlumno.split(" - ")[0];
            // Obtener código de carrera
            String itemCarrera = (String) cmbCarrera.getSelectedItem();
            String codigoCarrera = itemCarrera.split(" - ")[0];
            // Obtener código de curso
            String itemCurso = (String) cmbCurso.getSelectedItem();
            String codigoCurso = itemCurso.split(" - ")[0];
            // Buscar objetos
            Alumno alumno = service.buscarAlumnoPorCUI(cui);
            Carrera carrera = service.buscarCarreraPorCodigo(codigoCarrera);
            
            if (alumno == null || carrera == null) {
                mostrarError("Error al obtener datos del alumno o carrera");
                return;
            }
            // Buscar curso en el plan
            CursoCarrera curso = null;
            for (CursoCarrera c : carrera.getPlanDeEstudio()) {
                if (c.getCodigo().equals(codigoCurso)) {
                    curso = c;
                    break;
                }
            }
            
            if (curso == null) {
                mostrarError("Curso no encontrado en el plan de estudios");
                return;
            }
            // Matricular
            service.matricularAlumno(alumno, carrera, curso);
            
            JOptionPane.showMessageDialog(
                this,
                "Alumno matriculado exitosamente en " + curso.getNombre(),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
            // Limpiar selecciones
            cmbAlumno.setSelectedIndex(0);
            cmbCarrera.setSelectedIndex(0);
            cmbCurso.removeAllItems();
            cmbCurso.addItem("Seleccione un curso...");
            
        } catch (Exception e) {
            mostrarError("Error al matricular: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
