package com.universidad.view;

import com.universidad.model.Carrera;
import com.universidad.model.CursoCarrera;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
//Diálogo para mostrar el plan de estudios de una carrera
public class PlanEstudiosDialog extends JDialog {
    
    public PlanEstudiosDialog(JFrame parent, Carrera carrera) {
        super(parent, "Plan de Estudios - " + carrera.getNombre(), true);
        initComponents(carrera);
    }
    
    private void initComponents(Carrera carrera) {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        // Información de la carrera
        JPanel panelInfo = new JPanel(new GridLayout(3, 2, 10, 5));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInfo.setBackground(Color.WHITE);
        
        panelInfo.add(new JLabel("Carrera:"));
        panelInfo.add(new JLabel(carrera.getNombre()));
        
        panelInfo.add(new JLabel("Código:"));
        panelInfo.add(new JLabel(carrera.getCodigo()));
        
        panelInfo.add(new JLabel("Duración:"));
        panelInfo.add(new JLabel(carrera.getDuracionAnios() + " años (" + 
                                  (carrera.getDuracionAnios() * 2) + " semestres)"));
        
        add(panelInfo, BorderLayout.NORTH);
        // Panel con pestañas por semestre
        JTabbedPane tabbedPane = new JTabbedPane();
        
        int totalSemestres = carrera.getDuracionAnios() * 2;
        
        for (int sem = 1; sem <= totalSemestres; sem++) {
            List<CursoCarrera> cursos = carrera.obtenerCursosPorSemestre(sem);
            JPanel panelSemestre = crearPanelSemestre(cursos);
            tabbedPane.addTab("Semestre " + sem, panelSemestre);
        }
        
        add(tabbedPane, BorderLayout.CENTER);
        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(btnCerrar);
        
        add(panelBoton, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelSemestre(List<CursoCarrera> cursos) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        if (cursos.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay cursos en este semestre", SwingConstants.CENTER);
            panel.add(lblVacio);
            return panel;
        }
        // Tabla de cursos
        String[] columnas = {"Código", "Nombre", "Créditos", "Área Temática"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        int totalCreditos = 0;
        for (CursoCarrera curso : cursos) {
            Object[] fila = {
                curso.getCodigo(),
                curso.getNombre(),
                curso.getCreditos(),
                curso.getAreaTematica()
            };
            modelo.addRow(fila);
            totalCreditos += curso.getCreditos();
        }
        
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        // Total de créditos
        JLabel lblTotal = new JLabel("Total de créditos: " + totalCreditos, SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        panel.add(lblTotal, BorderLayout.SOUTH);
        
        return panel;
    }
}