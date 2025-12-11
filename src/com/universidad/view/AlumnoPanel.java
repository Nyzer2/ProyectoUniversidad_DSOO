package com.universidad.view;

import com.universidad.model.Alumno;
import com.universidad.service.UniversidadService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// Panel para gestionar alumnos
public class AlumnoPanel extends JPanel {
    
    private UniversidadService service;
    
    // Componentes
    private JTable tablaAlumnos;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JButton btnNuevo, btnEditar, btnEliminar, btnDetalle, btnBuscar;
    
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SUCCESS = new Color(25, 135, 84);
    private final Color COLOR_DANGER = new Color(220, 53, 69);
    
    public AlumnoPanel(UniversidadService service) {
        this.service = service;
        initComponents();
        actualizarTabla();
    }
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel superior con título y búsqueda
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        // Tabla de alumnos
        String[] columnas = {"CUI", "Nombre Completo", "DNI", "Edad", "Carreras", "Promedio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaAlumnos = new JTable(modeloTabla);
        tablaAlumnos.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaAlumnos.setRowHeight(30);
        tablaAlumnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAlumnos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaAlumnos.getTableHeader().setBackground(COLOR_PRIMARY);
        tablaAlumnos.getTableHeader().setForeground(Color.WHITE);
        
        // Evento de doble clic para ver detalle
        tablaAlumnos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    verDetalle();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaAlumnos);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel inferior con botones de acción
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }
    // Crea el panel superior con título y búsqueda
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        
        // Título
        JLabel titulo = new JLabel("Gestión de Alumnos");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.WEST);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBusqueda.setBackground(Color.WHITE);
        
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Arial", Font.PLAIN, 13));
        
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 13));
        txtBuscar.addActionListener(e -> buscarAlumno());
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnBuscar.setBackground(COLOR_PRIMARY);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(e -> buscarAlumno());
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        
        panel.add(panelBusqueda, BorderLayout.EAST);
        
        return panel;
    }
    // Crea el panel inferior con botones de acción
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(Color.WHITE);
        
        // Botón Nuevo
        btnNuevo = new JButton("Nuevo Alumno");
        btnNuevo.setFont(new Font("Arial", Font.BOLD, 13));
        btnNuevo.setBackground(COLOR_SUCCESS);
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFocusPainted(false);
        btnNuevo.addActionListener(e -> nuevoAlumno());
        
        // Botón Editar
        btnEditar = new JButton("Editar");
        btnEditar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnEditar.setBackground(COLOR_PRIMARY);
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.addActionListener(e -> editarAlumno());
        
        // Botón Ver Detalle
        btnDetalle = new JButton("Ver Detalle");
        btnDetalle.setFont(new Font("Arial", Font.PLAIN, 13));
        btnDetalle.setBackground(new Color(108, 117, 125));
        btnDetalle.setForeground(Color.WHITE);
        btnDetalle.setFocusPainted(false);
        btnDetalle.addActionListener(e -> verDetalle());
        
        // Botón Eliminar
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnEliminar.setBackground(COLOR_DANGER);
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> eliminarAlumno());
        
        panel.add(btnNuevo);
        panel.add(btnEditar);
        panel.add(btnDetalle);
        panel.add(btnEliminar);
        
        return panel;
    }
    // Actualiza la tabla de alumnos
    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        
        List<Alumno> alumnos = service.listarAlumnos();
        
        for (Alumno alumno : alumnos) {
            Object[] fila = new Object[6];
            fila[0] = alumno.obtenerCui();
            fila[1] = alumno.getNombreCompleto();
            fila[2] = alumno.getDNI();
            fila[3] = alumno.calcularEdad() + " años";
            fila[4] = alumno.getCarrerasInscripto().size();
            fila[5] = String.format("%.2f", alumno.obtenerPromedioGeneral());
            
            modeloTabla.addRow(fila);
        }
        
        // Actualizar contador en el título
        JLabel titulo = (JLabel) ((JPanel) getComponent(0)).getComponent(0);
        titulo.setText(String.format("Gestión de Alumnos (%d registros)", alumnos.size()));
    }
    //Abre el diálogo para crear un nuevo alumno
    private void nuevoAlumno() {
        AlumnoFormDialog dialog = new AlumnoFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            service,
            null
        );
        dialog.setVisible(true);
        
        if (dialog.isGuardado()) {
            actualizarTabla();
            JOptionPane.showMessageDialog(
                this,
                "Alumno registrado exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    //Abre el diálogo para editar un alumno seleccionado
    private void editarAlumno() {
        int filaSeleccionada = tablaAlumnos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, seleccione un alumno de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        String cui = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        Alumno alumno = service.buscarAlumnoPorCUI(cui);
        
        if (alumno != null) {
            AlumnoFormDialog dialog = new AlumnoFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                service,
                alumno
            );
            dialog.setVisible(true);
            
            if (dialog.isGuardado()) {
                actualizarTabla();
                JOptionPane.showMessageDialog(
                    this,
                    "Alumno actualizado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
    //Muestra el detalle de un alumno seleccionado
    private void verDetalle() {
        int filaSeleccionada = tablaAlumnos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, seleccione un alumno de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        String cui = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        Alumno alumno = service.buscarAlumnoPorCUI(cui);
        
        if (alumno != null) {
            AlumnoDetalleDialog dialog = new AlumnoDetalleDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                alumno
            );
            dialog.setVisible(true);
        }
    }
    //Elimina un alumno seleccionado
    private void eliminarAlumno() {
        int filaSeleccionada = tablaAlumnos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, seleccione un alumno de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        String cui = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreCompleto = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea eliminar al alumno:\n" + nombreCompleto + " [" + cui + "]?\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = service.eliminarAlumno(cui);
            
            if (eliminado) {
                actualizarTabla();
                JOptionPane.showMessageDialog(
                    this,
                    "Alumno eliminado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar el alumno. Puede que tenga matrículas activas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    //Busca alumnos por CUI o nombre
    private void buscarAlumno() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
        
        if (textoBusqueda.isEmpty()) {
            actualizarTabla();
            return;
        }
        
        modeloTabla.setRowCount(0);
        
        List<Alumno> alumnos = service.listarAlumnos();
        int encontrados = 0;
        
        for (Alumno alumno : alumnos) {
            boolean coincide = alumno.obtenerCui().toLowerCase().contains(textoBusqueda) ||
                             alumno.getNombreCompleto().toLowerCase().contains(textoBusqueda) ||
                             alumno.getDNI().contains(textoBusqueda);
            
            if (coincide) {
                Object[] fila = new Object[6];
                fila[0] = alumno.obtenerCui();
                fila[1] = alumno.getNombreCompleto();
                fila[2] = alumno.getDNI();
                fila[3] = alumno.calcularEdad() + " años";
                fila[4] = alumno.getCarrerasInscripto().size();
                fila[5] = String.format("%.2f", alumno.obtenerPromedioGeneral());
                
                modeloTabla.addRow(fila);
                encontrados++;
            }
        }
        // Actualizar contador
        JLabel titulo = (JLabel) ((JPanel) getComponent(0)).getComponent(0);
        titulo.setText(String.format("👥 Gestión de Alumnos (%d encontrados)", encontrados));
        
        if (encontrados == 0) {
            JOptionPane.showMessageDialog(
                this,
                "No se encontraron alumnos con el criterio de búsqueda: " + textoBusqueda,
                "Sin resultados",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
