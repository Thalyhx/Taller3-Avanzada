/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Cliente.Vista;

import Edu.UDistrital.Avanzada.Taller3.Cliente.Control.ControlVista;
import Edu.UDistrital.Avanzada.Taller3.Cliente.Control.KimariteLoader;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Ventana Principal del Cliente Sumo
 * @author nath
 */
public class VentanaCliente extends JFrame {
    
    private ControlVista controlVista;
    private JTextField txtNombre;
    private JTextField txtPeso;
    private JComboBox<String> cmbTecnicas;
    private JList<String> lstTecnicasSeleccionadas;
    private DefaultListModel<String> modeloLista;
    private JButton btnAgregarTecnica;
    private JButton btnEliminarTecnica;
    private JButton btnSeleccionarArchivo;
    private JButton btnConectar;
    private JTextArea txtMensajes;
    private JLabel lblEstado;
    
    /**
     * Constructor único 
     */
    public VentanaCliente(ControlVista controlVista) {
        this.controlVista = controlVista;
        
        setTitle("Cliente Sumo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(5, 5));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ========== PANEL SUPERIOR: Datos del Luchador ==========
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Luchador"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panelDatos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtNombre = new JTextField(15);
        panelDatos.add(txtNombre, gbc);
        
        // Peso
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panelDatos.add(new JLabel("Peso (kg):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtPeso = new JTextField(15);
        panelDatos.add(txtPeso, gbc);
        
        // Botón seleccionar archivo
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        btnSeleccionarArchivo = new JButton("Seleccionar archivo kimarites.properties");
        btnSeleccionarArchivo.addActionListener(e -> onSeleccionarArchivo());
        panelDatos.add(btnSeleccionarArchivo, gbc);
        gbc.gridwidth = 1;
        
        // ========== PANEL TÉCNICAS ==========
        JPanel panelTecnicas = new JPanel(new GridBagLayout());
        panelTecnicas.setBorder(BorderFactory.createTitledBorder("Seleccionar Técnicas"));
        GridBagConstraints gbcTec = new GridBagConstraints();
        gbcTec.insets = new Insets(3, 5, 3, 5);
        gbcTec.anchor = GridBagConstraints.WEST;
        
        // Disponibles
        gbcTec.gridx = 0;
        gbcTec.gridy = 0;
        gbcTec.weightx = 0;
        panelTecnicas.add(new JLabel("Disponibles:"), gbcTec);
        gbcTec.gridx = 1;
        gbcTec.weightx = 1;
        gbcTec.fill = GridBagConstraints.HORIZONTAL;
        cmbTecnicas = new JComboBox<>();
        cmbTecnicas.setEnabled(false);
        panelTecnicas.add(cmbTecnicas, gbcTec);
        gbcTec.gridx = 2;
        gbcTec.weightx = 0;
        gbcTec.fill = GridBagConstraints.NONE;
        btnAgregarTecnica = new JButton("Agregar");
        btnAgregarTecnica.setEnabled(false);
        btnAgregarTecnica.addActionListener(e -> onAgregarTecnica());
        panelTecnicas.add(btnAgregarTecnica, gbcTec);
        
        // Seleccionadas
        gbcTec.gridx = 0;
        gbcTec.gridy = 1;
        gbcTec.gridwidth = 3;
        gbcTec.weightx = 1;
        gbcTec.weighty = 1;
        gbcTec.fill = GridBagConstraints.BOTH;
        JPanel panelSeleccionadas = new JPanel(new BorderLayout(5, 5));
        panelSeleccionadas.add(new JLabel("Seleccionadas:"), BorderLayout.NORTH);
        modeloLista = new DefaultListModel<>();
        lstTecnicasSeleccionadas = new JList<>(modeloLista);
        lstTecnicasSeleccionadas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstTecnicasSeleccionadas.setPreferredSize(new Dimension(400, 80));
        JScrollPane scrollTecnicas = new JScrollPane(lstTecnicasSeleccionadas);
        panelSeleccionadas.add(scrollTecnicas, BorderLayout.CENTER);
        btnEliminarTecnica = new JButton("Eliminar");
        btnEliminarTecnica.addActionListener(e -> onEliminarTecnica());
        panelSeleccionadas.add(btnEliminarTecnica, BorderLayout.SOUTH);
        panelTecnicas.add(panelSeleccionadas, gbcTec);
        gbcTec.gridwidth = 1;
        
        // ========== PANEL ACCIÓN ==========
        JPanel panelAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnConectar = new JButton("Conectar al Servidor");
        btnConectar.setFont(new Font("Arial", Font.BOLD, 12));
        btnConectar.addActionListener(e -> onConectarAlServidor());
        btnConectar.setEnabled(false);
        panelAccion.add(btnConectar);
        
        // ========== PANEL MENSAJES ==========
        JPanel panelMensajes = new JPanel(new BorderLayout(5, 5));
        panelMensajes.setBorder(BorderFactory.createTitledBorder("Mensajes del Servidor"));
        txtMensajes = new JTextArea(10, 60);
        txtMensajes.setEditable(false);
        txtMensajes.setFont(new Font("Monospaced", Font.PLAIN, 10));
        txtMensajes.setLineWrap(true);
        txtMensajes.setWrapStyleWord(true);
        JScrollPane scrollMensajes = new JScrollPane(txtMensajes);
        panelMensajes.add(scrollMensajes, BorderLayout.CENTER);
        
        // ========== PANEL ESTADO ==========
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.setBorder(BorderFactory.createEtchedBorder());
        lblEstado = new JLabel("Estado: Listo");
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 10));
        panelEstado.add(lblEstado);
        
        // ========== ENSAMBLAR TODO ==========
        panelPrincipal.add(panelDatos, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.add(panelTecnicas, BorderLayout.NORTH);
        panelCentral.add(panelAccion, BorderLayout.CENTER);
        panelCentral.add(panelMensajes, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelEstado, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    // ========== EVENT HANDLERS ==========
    
    private void onSeleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        javax.swing.filechooser.FileNameExtensionFilter filter = 
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos Properties (*.properties)", "properties"
            );
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Seleccionar archivo kimarites.properties");
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            controlVista.onSeleccionarArchivo(fileChooser.getSelectedFile());
        }
    }
    
    private void onAgregarTecnica() {
        int indice = cmbTecnicas.getSelectedIndex();
        controlVista.onAgregarTecnica(indice);
    }
    
    private void onEliminarTecnica() {
        int indice = lstTecnicasSeleccionadas.getSelectedIndex();
        controlVista.onEliminarTecnica(indice);
    }
    
    private void onConectarAlServidor() {
        String nombre = txtNombre.getText();
        String peso = txtPeso.getText();
        controlVista.onConectarAlServidor(nombre, peso);
    }
    
    // ========== MÉTODOS DE ACTUALIZACIÓN (llamados por Control) ==========
    
    public void habilitarComboBoxTecnicas() {
        cmbTecnicas.setEnabled(true);
        btnAgregarTecnica.setEnabled(true);
    }
    
    public void actualizarComboBoxTecnicas(KimariteLoader.Kimarite[] tecnicas) {
        cmbTecnicas.removeAllItems();
        for (KimariteLoader.Kimarite kimarite : tecnicas) {
            cmbTecnicas.addItem(kimarite.toString());
        }
    }
    
    public void agregarTecnicaALista(String tecnica) {
        modeloLista.addElement(tecnica);
    }
    
    public void eliminarTecnicaDeLista(int indice) {
        modeloLista.remove(indice);
    }
    
    public void habilitarBotonConectar() {
        btnConectar.setEnabled(true);
    }
    
    public void deshabilitarBotonConectar() {
        btnConectar.setEnabled(false);
    }
    
    public void mostrarMensaje(String mensaje) {
        txtMensajes.append(mensaje + "\n");
        txtMensajes.setCaretPosition(txtMensajes.getDocument().getLength());
    }
    
    public void actualizarEstado(String estado) {
        lblEstado.setText(estado);
    }
}