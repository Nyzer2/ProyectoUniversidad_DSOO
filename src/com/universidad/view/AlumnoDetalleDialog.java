package com.universidad.view;

import com.universidad.model.Alumno;
import com.universidad.model.Matricula;
import javax.swing.*;
import java.awt.*;

// Diálogo para mostrar el detalle de un alumno
public class AlumnoDetalleDialog extends JDialog {
    
    public AlumnoDetalleDialog(JFrame parent, Alumno alumno) {
        super(parent, "Detalle del Alumno", true);
        initComponents(alumno);
    }
    
    private void initComponents(Alumno alumno) {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        // Panel de información personal
        JPanel panelInfo = new JPanel(new GridLayout(6, 2, 10, 10));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información Personal"));
        panelInfo.setBackground(Color.WHITE);
        
        panelInfo.add(crearLabel("CUI:"));
        panelInfo.add(crearLabel(alumno.obtenerCui()));
        
        panelInfo.add(crearLabel("Nombre:"));
        panelInfo.add(crearLabel(alumno.getNombre()));
        
        panelInfo.add(crearLabel("Apellido:"));
        panelInfo.add(crearLabel(alumno.getApellido()));
        
        panelInfo.add(crearLabel("DNI:"));
        panelInfo.add(crearLabel(alumno.getDNI()));
        
        panelInfo.add(crearLabel("Fecha de Nacimiento:"));
        panelInfo.add(crearLabel(alumno.getFechaNacimiento().toString()));
        
        panelInfo.add(crearLabel("Promedio General:"));
        panelInfo.add(crearLabel(String.format("%.2f", alumno.obtenerPromedioGeneral())));
        
        // Panel de carreras
        JPanel panelCarreras = new JPanel(new BorderLayout());
        panelCarreras.setBorder(BorderFactory.createTitledBorder("Carreras Inscritas"));
        panelCarreras.setBackground(Color.WHITE);
        
        JTextArea areaCarreras = new JTextArea();
        areaCarreras.setEditable(false);
        areaCarreras.setBackground(Color.WHITE);
        
        if (alumno.getCarrerasInscripto().isEmpty()) {
            areaCarreras.setText("No está inscrito en ninguna carrera");
        } else {
            StringBuilder sb = new StringBuilder();
            alumno.getCarrerasInscripto().forEach(c -> 
                sb.append("• ").append(c.getNombre()).append(" [").append(c.getCodigo()).append("]\n")
            );
            areaCarreras.setText(sb.toString());
        }
        
        panelCarreras.add(new JScrollPane(areaCarreras));
        
        // Panel de matrículas
        JPanel panelMatriculas = new JPanel(new BorderLayout());
        panelMatriculas.setBorder(BorderFactory.createTitledBorder("Historial Académico"));
        panelMatriculas.setBackground(Color.WHITE);
        
        JTextArea areaMatriculas = new JTextArea();
        areaMatriculas.setEditable(false);
        areaMatriculas.setBackground(Color.WHITE);
        
        if (alumno.getMatriculas().isEmpty()) {
            areaMatriculas.setText("No tiene matrículas registradas");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Matricula m : alumno.getMatriculas()) {
                sb.append("• ").append(m.getCurso().getNombre());
                if (m.getNotaAsignada() != null) {
                    sb.append(" - Nota: ").append(m.getNotaAsignada().getValor());
                    sb.append(m.estaAprobado() ? " (APROBADO)" : " (DESAPROBADO)");
                }
                sb.append("\n");
            }
            areaMatriculas.setText(sb.toString());
        }
        
        panelMatriculas.add(new JScrollPane(areaMatriculas));
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        
        mainPanel.add(panelInfo, BorderLayout.NORTH);
        mainPanel.add(panelCarreras, BorderLayout.CENTER);
        mainPanel.add(panelMatriculas, BorderLayout.SOUTH);
        
        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(btnCerrar);
        
        add(mainPanel, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }
    
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        return label;
    }
}
