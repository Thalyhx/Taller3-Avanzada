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

/**
 * Ventana Principal del Cliente Sumo
 * @author nath
 */
public class VentanaCliente extends JFrame {
    
    private ControlSocket controlSocket;
    private JTextField txtNombre;
    private JTextField txtPeso;
    private JTextArea txtTecnicas;
    private JButton btnSeleccionarArchivo;
    private JButton btnConectar;
    private JTextArea txtMensajes;
    private JLabel lblEstado;
    private File archivoTecnicas;
    private KimariteLoader.Kimarite[] kimarites;
    
    public VentanaCliente() {
        setTitle("Cliente Sumo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de datos del luchador
        JPanel panelDatos = new JPanel(new GridLayout(5, 1, 5, 5));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Luchador"));
        
        // Nombre
        JPanel panelNombre = new JPanel(new BorderLayout());
        panelNombre.add(new JLabel("Nombre:"), BorderLayout.WEST);
        txtNombre = new JTextField(20);
        panelNombre.add(txtNombre, BorderLayout.CENTER);
        panelDatos.add(panelNombre);
        
        // Peso
        JPanel panelPeso = new JPanel(new BorderLayout());
        panelPeso.add(new JLabel("Peso (kg):"), BorderLayout.WEST);
        txtPeso = new JTextField(20);
        panelPeso.add(txtPeso, BorderLayout.CENTER);
        panelDatos.add(panelPeso);
        
        // Seleccionar técnicas
        JPanel panelSeleccionar = new JPanel(new BorderLayout());
        btnSeleccionarArchivo = new JButton("Seleccionar archivo kimarites.properties");
        btnSeleccionarArchivo.addActionListener(e -> seleccionarArchivo());
        panelSeleccionar.add(btnSeleccionarArchivo, BorderLayout.CENTER);
        panelDatos.add(panelSeleccionar);
        
        // Técnicas cargadas
        JPanel panelTecnicas = new JPanel(new BorderLayout());
        panelTecnicas.add(new JLabel("Técnicas cargadas:"), BorderLayout.NORTH);
        txtTecnicas = new JTextArea(3, 40);
        txtTecnicas.setEditable(false);
        txtTecnicas.setFont(new Font("Monospaced", Font.PLAIN, 10));
        JScrollPane scrollTecnicas = new JScrollPane(txtTecnicas);
        panelTecnicas.add(scrollTecnicas, BorderLayout.CENTER);
        panelDatos.add(panelTecnicas);
        
        // Botón conectar
        JPanel panelBoton = new JPanel();
        btnConectar = new JButton("Conectar al Servidor");
        btnConectar.addActionListener(e -> conectarAlServidor());
        btnConectar.setEnabled(false);
        panelBoton.add(btnConectar);
        panelDatos.add(panelBoton);
        
        // Panel de mensajes
        JPanel panelMensajes = new JPanel(new BorderLayout());
        panelMensajes.setBorder(BorderFactory.createTitledBorder("Mensajes"));
        txtMensajes = new JTextArea(10, 40);
        txtMensajes.setEditable(false);
        txtMensajes.setFont(new Font("Monospaced", Font.PLAIN, 10));
        JScrollPane scrollMensajes = new JScrollPane(txtMensajes);
        panelMensajes.add(scrollMensajes, BorderLayout.CENTER);
        
        // Panel de estado
        JPanel panelEstado = new JPanel();
        lblEstado = new JLabel("Estado: Listo");
        panelEstado.add(lblEstado);
        
        panelPrincipal.add(panelDatos, BorderLayout.NORTH);
        panelPrincipal.add(panelMensajes, BorderLayout.CENTER);
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
            
            // Cargar técnicas como objetos Kimarite
            kimarites = KimariteLoader.cargarTecnicas(archivoTecnicas);
            
            if (kimarites.length > 0) {
                txtTecnicas.setText("");
                for (KimariteLoader.Kimarite kimarite : kimarites) {
                    txtTecnicas.append(kimarite.toString() + "\n");
                }
                btnConectar.setEnabled(true);
                mostrarMensaje("✓ " + kimarites.length + " técnicas cargadas");
            } else {
                mostrarMensaje("❌ No se cargaron técnicas");
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
            
            if (kimarites == null || kimarites.length == 0) {
                mostrarMensaje("❌ Selecciona archivo de técnicas");
                return;
            }
            
            String nombre = txtNombre.getText();
            double peso = Double.parseDouble(txtPeso.getText());
            
            // Crear configuración (localhost puerto 5000)
            ConfigConexion config = new ConfigConexion("localhost", 5000);
            
            // Crear control socket
            controlSocket = new ControlSocket(config);
            
            mostrarMensaje("Conectando con servidor...");
            lblEstado.setText("Estado: Conectando...");
            btnConectar.setEnabled(false);
            
            // Conectar
            controlSocket.conectar();
            mostrarMensaje("✓ Conectado al servidor");
            
            // Enviar datos del luchador con kimarites
            controlSocket.enviarLuchador(nombre, peso, kimarites);
            mostrarMensaje("✓ Datos enviados al servidor");
            
            // Iniciar thread para escuchar respuestas
            ThreadCliente threadCliente = new ThreadCliente(controlSocket.getEntrada(), this);
            threadCliente.start();
            
            lblEstado.setText("Estado: Conectado - Esperando respuesta del servidor");
            
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