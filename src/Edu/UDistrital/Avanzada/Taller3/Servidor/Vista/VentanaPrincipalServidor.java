/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Edu.UDistrital.Avanzada.Taller3.Servidor.Vista;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Control.ControlVistaServidor;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Luchador;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Vista Principal del Servidor Sumo
 * @author nath
 */
public class VentanaPrincipalServidor extends JFrame {
    
    private ControlVistaServidor controlVista;
    private JButton btnIniciar;
    private JTextArea txtResultados;
    private JLabel lblEstado;
    
    /**
     * Constructor único - Recibe ControlVistaServidor
     */
    public VentanaPrincipalServidor(ControlVistaServidor controlVista) {
        this.controlVista = controlVista;
        
        setTitle("Servidor Sumo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnIniciar = new JButton("Iniciar Servidor");
        btnIniciar.addActionListener(e -> iniciarServidor());
        panelBotones.add(btnIniciar);
        
        // Panel de resultados
        txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(txtResultados);
        
        // Panel de estado
        JPanel panelEstado = new JPanel();
        lblEstado = new JLabel("Estado: Listo");
        panelEstado.add(lblEstado);
        
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelEstado, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    /**
     * Inicia el servidor
     */
    private void iniciarServidor() {
        new Thread(() -> {
            try {
                // Seleccionar archivo properties
                File archivoConfig = seleccionarArchivo();
                
                if (archivoConfig == null) {
                    txtResultados.setText("No se seleccionó archivo. Cancelado.");
                    return;
                }
                
                // Inicializar servidor
                if (!controlVista.inicializarServidor(archivoConfig)) {
                    txtResultados.setText("Error al inicializar el servidor");
                    return;
                }
                
                // Mostrar información inicial
                txtResultados.setText("");
                txtResultados.append("╔════════════════════════════════════════╗\n");
                txtResultados.append("║       SERVIDOR SUMO                    ║\n");
                txtResultados.append("╚════════════════════════════════════════╝\n\n");
                txtResultados.append("Archivo: " + controlVista.getControlPrincipal().getNombreArchivoConfig() + "\n");
                txtResultados.append("Puerto: " + controlVista.getControlPrincipal().getPuertoServidor() + "\n");
                txtResultados.append("Dirección: " + controlVista.getControlPrincipal().getDireccionServidor() + "\n\n");
                
                // Iniciar servidor
                controlVista.getControlPrincipal().iniciarServidor();
                txtResultados.append("✓ Servidor iniciado\n");
                txtResultados.append("Esperando 2 luchadores...\n\n");
                lblEstado.setText("Estado: Esperando conexiones...");
                
                // Aceptar primer luchador
                Socket cliente1 = controlVista.getControlPrincipal().aceptarCliente();
                txtResultados.append("✓ Cliente 1 conectado\n");
                Luchador luchador1 = controlVista.getControlPrincipal().recibirLuchador(cliente1);
                txtResultados.append("  Registrado: " + luchador1.getNombre() + 
                                   " (" + luchador1.getPeso() + " kg)\n\n");
                
                // Aceptar segundo luchador
                Socket cliente2 = controlVista.getControlPrincipal().aceptarCliente();
                txtResultados.append("✓ Cliente 2 conectado\n");
                Luchador luchador2 = controlVista.getControlPrincipal().recibirLuchador(cliente2);
                txtResultados.append("  Registrado: " + luchador2.getNombre() + 
                                   " (" + luchador2.getPeso() + " kg)\n\n");
                
                txtResultados.append("=== INICIANDO COMBATE ===\n");
                txtResultados.append(luchador1.getNombre() + " vs " + 
                                   luchador2.getNombre() + "\n\n");
                lblEstado.setText("Estado: Combate en curso...");
                
                // Iniciar combate
                controlVista.getControlPrincipal().iniciarCombate(luchador1, luchador2);
                
                // Esperar resultado
                Luchador ganador = controlVista.getControlPrincipal().esperarResultado();
                
                // Mostrar resultados
                mostrarResultados(ganador);
                
                // Cerrar sockets
                cliente1.close();
                cliente2.close();
                
                // Cerrar servidor
                controlVista.getControlPrincipal().detenerServidor();
                lblEstado.setText("Estado: Combate finalizado");
                btnIniciar.setEnabled(true);
                
            } catch (Exception e) {
                txtResultados.append("\n❌ Error: " + e.getMessage() + "\n");
                e.printStackTrace();
                lblEstado.setText("Estado: Error");
                btnIniciar.setEnabled(true);
            }
        }).start();
        
        btnIniciar.setEnabled(false);
    }
    
    /**
     * Muestra los resultados del combate
     */
    private void mostrarResultados(Luchador ganador) {
        List<Map<String, Object>> turnos = controlVista.getControlPrincipal().obtenerHistorialTurnos();
        
        txtResultados.append("\n╔════════════════════════════════════════╗\n");
        txtResultados.append("║        HISTORIAL DE TURNOS             ║\n");
        txtResultados.append("╚════════════════════════════════════════╝\n\n");
        
        if (turnos != null && !turnos.isEmpty()) {
            for (Map<String, Object> turno : turnos) {
                int numero = (Integer) turno.get("numero");
                String luchador = (String) turno.get("luchador");
                String kimarite = (String) turno.get("kimarite");
                double probabilidad = (Double) turno.get("probabilidad");
                boolean exito = (Boolean) turno.get("exito");
                
                String resultado = exito ? "✓ EXITO" : "✗ Falló";
                
                txtResultados.append(String.format("Turno %d: %s usa %s (%.0f%% prob) → %s\n",
                    numero, luchador, kimarite, probabilidad * 100, resultado));
            }
        }
        
        // Mostrar ganador
        if (ganador != null) {
            txtResultados.append("\n╔════════════════════════════════════════╗\n");
            txtResultados.append("║      COMBATE FINALIZADO                ║\n");
            txtResultados.append("╚════════════════════════════════════════╝\n\n");
            txtResultados.append("🥇 GANADOR: " + ganador.getNombre() + "\n");
            txtResultados.append("   Victorias: " + ganador.getVictorias() + "\n");
        }
    }
    
    /**
     * Abre JFileChooser para seleccionar archivo properties
     */
    private File seleccionarArchivo() {
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
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
}