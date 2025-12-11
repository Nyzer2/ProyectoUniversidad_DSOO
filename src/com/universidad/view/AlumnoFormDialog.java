package com.universidad.view;

import com.universidad.model.Alumno;
import com.universidad.service.UniversidadService;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;

public class AlumnoFormDialog extends JDialog {
    
    private UniversidadService service;
    private Alumno alumno; // null si es nuevo, con datos si es edición
    private boolean guardado = false;
    
    // Componentes del formulario
    private JTextField txtCUI;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDNI;
    private JTextField txtFechaNac; // Formato: YYYY-MM-DD
    
    private JButton btnGuardar, btnCancelar;
    
    public AlumnoFormDialog(JFrame parent, UniversidadService service, Alumno alumno) {
        super(parent, alumno == null ? "Nuevo Alumno" : "Editar Alumno", true);
        this.service = service;
        this.alumno = alumno;
        initComponents();
    }
    private void initComponents() {
        setSize(500, 450);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // CUI
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("CUI: *"), gbc);
        
        gbc.gridx = 1;
        txtCUI = new JTextField(20);
        txtCUI.setEnabled(alumno == null); // Solo editable si es nuevo
        formPanel.add(txtCUI, gbc);
        
        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nombre: *"), gbc);
        
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        formPanel.add(txtNombre, gbc);
        
        // Apellido
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Apellido: *"), gbc);
        
        gbc.gridx = 1;
        txtApellido = new JTextField(20);
        formPanel.add(txtApellido, gbc);
        
        // DNI
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("DNI: *"), gbc);
        
        gbc.gridx = 1;
        txtDNI = new JTextField(20);
        formPanel.add(txtDNI, gbc);
        
        // Fecha de Nacimiento
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Fecha Nac. (YYYY-MM-DD): *"), gbc);
        
        gbc.gridx = 1;
        txtFechaNac = new JTextField(20);
        formPanel.add(txtFechaNac, gbc);
        
        // Información
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JLabel lblInfo = new JLabel("<html><i>* Campos obligatorios<br>" +
                                    "CUI: Código único (ej: A2024001)<br>" +
                                    "DNI: 8 dígitos numéricos</i></html>");
        lblInfo.setForeground(Color.GRAY);
        formPanel.add(lblInfo, gbc);
        
        // Si es edición, llenar campos
        if (alumno != null) {
            txtCUI.setText(alumno.obtenerCui());
            txtNombre.setText(alumno.getNombre());
            txtApellido.setText(alumno.getApellido());
            txtDNI.setText(alumno.getDNI());
            txtFechaNac.setText(alumno.getFechaNacimiento().toString());
        }
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        
        btnGuardar = new JButton(alumno == null ? "Guardar" : "Actualizar");
        btnGuardar.setBackground(new Color(25, 135, 84));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarAlumno());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        
        // Agregar componentes al diálogo
        add(formPanel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    // Guarda o actualiza el alumno
    private void guardarAlumno() {
        // Validar campos
        String cui = txtCUI.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDNI.getText().trim();
        String fechaNacStr = txtFechaNac.getText().trim();
        
        // Validaciones
        if (cui.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || 
            dni.isEmpty() || fechaNacStr.isEmpty()) {
            mostrarError("Todos los campos son obligatorios");
            return;
        }
        
        if (cui.length() < 7 || cui.length() > 10) {
            mostrarError("El CUI debe tener entre 7 y 10 caracteres");
            txtCUI.requestFocus();
            return;
        }
        
        if (dni.length() != 8 || !dni.matches("\\d+")) {
            mostrarError("El DNI debe tener exactamente 8 dígitos numéricos");
            txtDNI.requestFocus();
            return;
        }
        
        // Validar fecha
        LocalDate fechaNac;
        try {
            fechaNac = LocalDate.parse(fechaNacStr, DateTimeFormatter.ISO_LOCAL_DATE);
            
            // Validar edad mínima (15 años)
            if (fechaNac.plusYears(15).isAfter(LocalDate.now())) {
                mostrarError("El alumno debe tener al menos 15 años");
                txtFechaNac.requestFocus();
                return;
            }
            
        } catch (DateTimeParseException e) {
            mostrarError("Formato de fecha inválido. Use YYYY-MM-DD (ej: 2000-03-15)");
            txtFechaNac.requestFocus();
            return;
        }
        
        try {
            if (alumno == null) {
                // Crear nuevo alumno
                Alumno nuevoAlumno = new Alumno(nombre, apellido, dni, fechaNac, cui);
                boolean exito = service.registrarAlumno(nuevoAlumno);
                
                if (exito) {
                    guardado = true;
                    dispose();
                } else {
                    mostrarError("Error al guardar el alumno. Verifique que el CUI no esté duplicado.");
                }
            } else {
                // Actualizar alumno existente
                alumno.setNombre(nombre);
                alumno.setApellido(apellido);
                alumno.setDNI(dni);
                alumno.setFechaNacimiento(fechaNac);
                
                boolean exito = service.actualizarAlumno(alumno);
                
                if (exito) {
                    guardado = true;
                    dispose();
                } else {
                    mostrarError("Error al actualizar el alumno");
                }
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Muestra un mensaje de error
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Error de Validación",
            JOptionPane.ERROR_MESSAGE
        );
    }
    // Indica si se guardó o actualizó el alumno
    public boolean isGuardado() {
        return guardado;
    }
}
