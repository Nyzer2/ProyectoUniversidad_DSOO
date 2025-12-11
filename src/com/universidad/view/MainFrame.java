package com.universidad.view;

import com.universidad.service.UniversidadService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// Ventana principal de la aplicación
public class MainFrame extends JFrame {
    
    private UniversidadService service;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Paneles
    private DashboardPanel dashboardPanel;
    private AlumnoPanel alumnoPanel;
    private CarreraPanel carreraPanel;
    private MatriculaPanel matriculaPanel;
    
    // Colores del tema
    private final Color COLOR_PRIMARY = new Color(13, 110, 253);
    private final Color COLOR_SIDEBAR = new Color(44, 62, 80);
    private final Color COLOR_HOVER = new Color(52, 73, 94);
    
    public MainFrame() {
        this.service = UniversidadService.getInstance();
        initComponents();
        setLocationRelativeTo(null); // Centrar en pantalla
    }
    
    private void initComponents() {
        // Configuración de la ventana
        setTitle("Sistema Universidad - Gestión Académica");
        setSize(1200, 700);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        // Crear componentes
        JPanel sidebarPanel = crearSidebar();
        contentPanel = crearContentPanel();
        // Agregar componentes
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        // Mostrar dashboard por defecto
        mostrarPanel("dashboard");
    }
    // Crea el sidebar lateral
    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        // Header del sidebar
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_SIDEBAR);
        headerPanel.setMaximumSize(new Dimension(250, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel titleLabel = new JLabel("🎓 Universidad");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        sidebar.add(headerPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        // Botones del menú
        sidebar.add(crearBotonMenu("Dashboard", "dashboard"));
        sidebar.add(crearBotonMenu("Alumnos", "alumnos"));
        sidebar.add(crearBotonMenu("Carreras", "carreras"));
        sidebar.add(crearBotonMenu("Matrículas", "matriculas"));
        
        sidebar.add(Box.createVerticalGlue()); // Espaciador
        // Botón de salir
        JButton btnSalir = crearBotonMenu("🚪 Salir", null);
        btnSalir.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea salir?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION
            );
            if (opcion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        sidebar.add(btnSalir);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        return sidebar;
    }
    //Crea un botón del menú lateral
    private JButton crearBotonMenu(String texto, String panelName) {
        JButton button = new JButton(texto);
        button.setMaximumSize(new Dimension(250, 50));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(COLOR_SIDEBAR);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLOR_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_SIDEBAR);
            }
        });
        // Acción al hacer clic
        if (panelName != null) {
            button.addActionListener(e -> mostrarPanel(panelName));
        }
        
        return button;
    }
    //Crea el panel de contenido con CardLayout

    private JPanel crearContentPanel() {
        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        
        // Crear e inicializar paneles
        dashboardPanel = new DashboardPanel(service);
        alumnoPanel = new AlumnoPanel(service);
        carreraPanel = new CarreraPanel(service);
        matriculaPanel = new MatriculaPanel(service);
        
        // Agregar paneles al CardLayout
        panel.add(dashboardPanel, "dashboard");
        panel.add(alumnoPanel, "alumnos");
        panel.add(carreraPanel, "carreras");
        panel.add(matriculaPanel, "matriculas");
        
        return panel;
    }
    //Muestra un panel específico
    private void mostrarPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        
        // Actualizar datos del panel
        switch (panelName) {
            case "dashboard":
                dashboardPanel.actualizarDatos();
                break;
            case "alumnos":
                alumnoPanel.actualizarTabla();
                break;
            case "carreras":
                carreraPanel.actualizarTabla();
                break;
            case "matriculas":
                matriculaPanel.actualizarDatos();
                break;
        }
    }
    //Método main para ejecutar la aplicación
    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Ejecutar en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
