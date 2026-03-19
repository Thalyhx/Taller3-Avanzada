/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Cliente.Vista;

import Edu.UDistrital.Avanzada.Taller3.Cliente.Modelo.ConfigConexion;
import Edu.UDistrital.Avanzada.Taller3.Cliente.Control.KimariteLoader;
import Edu.UDistrital.Avanzada.Taller3.Cliente.Control.ControlSocket;
import Edu.UDistrital.Avanzada.Taller3.Cliente.Control.ThreadCliente;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Ventana Principal del Cliente Sumo
 * @author nath
 */
public class VentanaCliente extends JFrame {
    
    private ControlSocket controlSocket;
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
    private File archivoTecnicas;
    private KimariteLoader.Kimarite[] todasLasTecnicas;
    private ArrayList<KimariteLoader.Kimarite> tecnicasSeleccionadas;
    
    public VentanaCliente() {
        setTitle("Cliente Sumo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        
        tecnicasSeleccionadas = new ArrayList<>();
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
        btnSeleccionarArchivo.addActionListener(e -> seleccionarArchivo());
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
        btnAgregarTecnica.addActionListener(e -> agregarTecnica());
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
        btnEliminarTecnica.addActionListener(e -> eliminarTecnica());
        panelSeleccionadas.add(btnEliminarTecnica, BorderLayout.SOUTH);
        panelTecnicas.add(panelSeleccionadas, gbcTec);
        gbcTec.gridwidth = 1;
        
        // ========== PANEL ACCIÓN ==========
        JPanel panelAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnConectar = new JButton("Conectar al Servidor");
        btnConectar.setFont(new Font("Arial", Font.BOLD, 12));
        btnConectar.addActionListener(e -> conectarAlServidor());
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
    
    /**
     * Abre JFileChooser para seleccionar archivo de técnicas
     */
    private void seleccionarArchivo() {
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
            archivoTecnicas = fileChooser.getSelectedFile();
            
            // Cargar todas las técnicas
            todasLasTecnicas = KimariteLoader.cargarTecnicas(archivoTecnicas);
            
            if (todasLasTecnicas.length > 0) {
                // Limpiar combo box
                cmbTecnicas.removeAllItems();
                
                // Agregar técnicas al combo box
                for (KimariteLoader.Kimarite kimarite : todasLasTecnicas) {
                    cmbTecnicas.addItem(kimarite.toString());
                }
                
                cmbTecnicas.setEnabled(true);
                btnAgregarTecnica.setEnabled(true);
                mostrarMensaje("✓ " + todasLasTecnicas.length + " técnicas cargadas");
            } else {
                mostrarMensaje("❌ No se cargaron técnicas");
            }
        }
    }
    
    /**
     * Agrega la técnica seleccionada a la lista
     */
    private void agregarTecnica() {
        int indice = cmbTecnicas.getSelectedIndex();
        
        if (indice >= 0) {
            KimariteLoader.Kimarite kimarite = todasLasTecnicas[indice];
            
            // Verificar que no esté duplicada
            for (KimariteLoader.Kimarite k : tecnicasSeleccionadas) {
                if (k.getNombre().equals(kimarite.getNombre())) {
                    mostrarMensaje("⚠ Ya está agregada: " + kimarite.getNombre());
                    return;
                }
            }
            
            // Agregar a la lista de seleccionadas
            tecnicasSeleccionadas.add(kimarite);
            modeloLista.addElement(kimarite.toString());
            
            // Habilitar botón conectar si hay al menos una técnica
            if (tecnicasSeleccionadas.size() > 0) {
                btnConectar.setEnabled(true);
            }
            
            mostrarMensaje("✓ Agregada: " + kimarite.getNombre());
        }
    }
    
    /**
     * Elimina la técnica seleccionada de la lista
     */
    private void eliminarTecnica() {
        int indice = lstTecnicasSeleccionadas.getSelectedIndex();
        
        if (indice >= 0) {
            KimariteLoader.Kimarite eliminada = tecnicasSeleccionadas.get(indice);
            tecnicasSeleccionadas.remove(indice);
            modeloLista.remove(indice);
            
            mostrarMensaje("✓ Eliminada: " + eliminada.getNombre());
            
            if (tecnicasSeleccionadas.isEmpty()) {
                btnConectar.setEnabled(false);
            }
        }
    }
    
    /**
     * Conecta con el servidor y envía los datos del luchador
     */
    private void conectarAlServidor() {
        try {
            // Validar datos
            if (txtNombre.getText().isEmpty()) {
                mostrarMensaje("❌ Ingresa el nombre del luchador");
                return;
            }
            
            if (txtPeso.getText().isEmpty()) {
                mostrarMensaje("❌ Ingresa el peso del luchador");
                return;
            }
            
            if (tecnicasSeleccionadas.isEmpty()) {
                mostrarMensaje("❌ Selecciona al menos una técnica");
                return;
            }
            
            String nombre = txtNombre.getText();
            double peso = Double.parseDouble(txtPeso.getText());
            
            // Convertir a array
            KimariteLoader.Kimarite[] kimarites = tecnicasSeleccionadas.toArray(
                new KimariteLoader.Kimarite[0]
            );
            
            // Crear configuración (localhost puerto 5000)
            ConfigConexion config = new ConfigConexion("localhost", 5000);
            
            // Crear control socket
            controlSocket = new ControlSocket(config);
            
            mostrarMensaje("═══════════════════════════════════════");
            mostrarMensaje("Conectando con servidor...");
            lblEstado.setText("Estado: Conectando...");
            btnConectar.setEnabled(false);
            
            // Conectar
            controlSocket.conectar();
            mostrarMensaje("✓ Conectado al servidor");
            
            // Enviar datos del luchador
            controlSocket.enviarLuchador(nombre, peso, kimarites);
            mostrarMensaje("✓ Datos enviados:");
            mostrarMensaje("  • Luchador: " + nombre);
            mostrarMensaje("  • Peso: " + peso + " kg");
            mostrarMensaje("  • Técnicas: " + kimarites.length);
            
            for (KimariteLoader.Kimarite k : kimarites) {
                mostrarMensaje("    - " + k.toString());
            }
            
            // Iniciar thread para escuchar respuestas
            ThreadCliente threadCliente = new ThreadCliente(controlSocket.getEntrada(), this);
            threadCliente.start();
            
            lblEstado.setText("Estado: Conectado - Esperando combate...");
            mostrarMensaje("═══════════════════════════════════════");
            
        } catch (NumberFormatException e) {
            mostrarMensaje("❌ El peso debe ser un número");
            btnConectar.setEnabled(true);
        } catch (Exception e) {
            mostrarMensaje("❌ Error de conexión: " + e.getMessage());
            btnConectar.setEnabled(true);
            lblEstado.setText("Estado: Error");
        }
    }
    
    /**
     * Muestra mensaje en el área de mensajes
     */
    public void mostrarMensaje(String mensaje) {
        txtMensajes.append(mensaje + "\n");
        txtMensajes.setCaretPosition(txtMensajes.getDocument().getLength());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaCliente ventana = new VentanaCliente();
            ventana.setVisible(true);
        });
    }
}