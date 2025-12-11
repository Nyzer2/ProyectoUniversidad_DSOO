package com.universidad.view;

import com.universidad.model.Carrera;
import com.universidad.model.CursoCarrera;
import com.universidad.service.UniversidadService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

//Panel para gestionar carreras
public class CarreraPanel extends JPanel {
    
    private UniversidadService service;
    
    // Componentes
    private JTable tablaCarreras;
    private DefaultTableModel modeloTabla;
    private JButton btnVerPlan;
    
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    
    public CarreraPanel(UniversidadService service) {
        this.service = service;
        initComponents();
        actualizarTabla();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titulo = new JLabel("Gestión de Carreras");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);
        
        // Tabla de carreras
        String[] columnas = {"Código", "Nombre", "Duración (años)", "Total Cursos", "Total Créditos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaCarreras = new JTable(modeloTabla);
        tablaCarreras.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaCarreras.setRowHeight(30);
        tablaCarreras.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaCarreras.getTableHeader().setBackground(COLOR_PRIMARY);
        tablaCarreras.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablaCarreras);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        add(scrollPane, BorderLayout.CENTER);
        
        // Botón ver plan
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBackground(Color.WHITE);
        
        btnVerPlan = new JButton("Ver Plan de Estudios");
        btnVerPlan.setBackground(COLOR_PRIMARY);
        btnVerPlan.setForeground(Color.WHITE);
        btnVerPlan.setFont(new Font("Arial", Font.BOLD, 13));
        btnVerPlan.setFocusPainted(false);
        btnVerPlan.addActionListener(e -> verPlanEstudios());
        
        panelBoton.add(btnVerPlan);
        add(panelBoton, BorderLayout.SOUTH);
    }
    
    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        
        List<Carrera> carreras = service.listarCarreras();
        
        for (Carrera carrera : carreras) {
            Object[] fila = new Object[5];
            fila[0] = carrera.getCodigo();
            fila[1] = carrera.getNombre();
            fila[2] = carrera.getDuracionAnios();
            fila[3] = carrera.getPlanDeEstudio().size();
            fila[4] = carrera.obtenerTotalCreditos();
            
            modeloTabla.addRow(fila);
        }
    }
    
    private void verPlanEstudios() {
        int filaSeleccionada = tablaCarreras.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, seleccione una carrera",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        String codigo = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        Carrera carrera = service.buscarCarreraPorCodigo(codigo);
        
        if (carrera != null) {
            PlanEstudiosDialog dialog = new PlanEstudiosDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                carrera
            );
            dialog.setVisible(true);
        }
    }
}
