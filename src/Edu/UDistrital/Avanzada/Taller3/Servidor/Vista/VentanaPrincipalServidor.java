/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Edu.UDistrital.Avanzada.Taller3.Servidor.Vista;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Control.ControlVistaServidor;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Control.ControlPrincipalServidor;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Luchador;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Kimarite;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Ventana Principal del Servidor Sumo
 * @author nath
 */
public class VentanaPrincipalServidor extends JFrame {
    private ControlVistaServidor controlVista;
    private ControlPrincipalServidor controlPrincipal;
    private JButton btnCargarKimarites;
    private JButton btnIniciarCombate;
    private JTextArea txtResultados;
    private JLabel lblEstado;
    
    private Luchador luchador1;
    private Luchador luchador2;
    
    public VentanaPrincipalServidor() {
        setTitle("Servidor Sumo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        this.controlVista = new ControlVistaServidor();
        this.controlPrincipal = new ControlPrincipalServidor();
        this.controlPrincipal.setControlDohyo(controlVista.getControlDohyo());
        
        inicializarLuchadores();
        initComponents();
    }
    
    private void inicializarLuchadores() {
        Kimarite[] tecnicas1 = {
            new Kimarite("Yorikiri", "Empuje por cintura", 0.35),
            new Kimarite("Tsuppari", "Empuje con palmas", 0.25)
        };
        
        Kimarite[] tecnicas2 = {
            new Kimarite("Shitatenage", "Tirada baja", 0.40),
            new Kimarite("Oshidashi", "Empuje pecho", 0.30)
        };
        
        this.luchador1 = new Luchador("Yokozuna", 130, tecnicas1);
        this.luchador2 = new Luchador("Ozeki", 125, tecnicas2);
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnCargarKimarites = new JButton("Cargar Kimarites");
        btnCargarKimarites.addActionListener(e -> seleccionarArchivoKimarites());
        panelBotones.add(btnCargarKimarites);
        
        btnIniciarCombate = new JButton("Iniciar Combate");
        btnIniciarCombate.addActionListener(e -> iniciarCombate());
        panelBotones.add(btnIniciarCombate);
        
        txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(txtResultados);
        
        JPanel panelEstado = new JPanel();
        lblEstado = new JLabel("Estado: Listo");
        panelEstado.add(lblEstado);
        
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelEstado, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private void seleccionarArchivoKimarites() {
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
            String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            boolean cargado = controlVista.cargarKimarites(rutaArchivo);

            if (cargado) {
                JOptionPane.showMessageDialog(this,
                    "Kimarites cargados exitosamente desde:\n" + rutaArchivo,
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al cargar el archivo. Verifica que sea un archivo properties valido.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void iniciarCombate() {
        if (!controlVista.getControlKimarite().isKimaritesCargado()) {
            JOptionPane.showMessageDialog(this,
                "Debes cargar un archivo kimarites.properties primero",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        new Thread(() -> ejecutarCombate()).start();
    }
    
    private void ejecutarCombate() {
        lblEstado.setText("Estado: Combate en curso...");
        txtResultados.setText("");
        
        mostrarInicioCombate();
        
        controlVista.getControlDohyo().iniciarCombate(luchador1, luchador2);
        
        Luchador ganador = controlVista.getControlDohyo().esperarResultado();
        
        mostrarResultados(ganador);
    }
    
    private void mostrarInicioCombate() {
        txtResultados.append("╔════════════════════════════════════════╗\n");
        txtResultados.append("║       COMBATE DE SUMO                  ║\n");
        txtResultados.append("╚════════════════════════════════════════╝\n\n");
        
        txtResultados.append("Luchador 1: " + luchador1.getNombre() + " (Peso: " + luchador1.getPeso() + " kg)\n");
        txtResultados.append("Luchador 2: " + luchador2.getNombre() + " (Peso: " + luchador2.getPeso() + " kg)\n");
        txtResultados.append("\nVictorias Previas:\n");
        txtResultados.append("  " + luchador1.getNombre() + ": " + luchador1.getVictorias() + " victorias\n");
        txtResultados.append("  " + luchador2.getNombre() + ": " + luchador2.getVictorias() + " victorias\n");
        txtResultados.append("\n─────────────────────────────────────────\n");
        txtResultados.append("             INICIANDO COMBATE\n");
        txtResultados.append("─────────────────────────────────────────\n\n");
    }
    
    private void mostrarResultados(Luchador ganador) {
        List<Map<String, Object>> turnos = controlPrincipal.obtenerHistorialTurnos();
        
        mostrarHistorialTurnos(turnos);
        mostrarResultadoFinal(ganador);
        
        if (ganador != null) {
            lblEstado.setText("Estado: Combate finalizado. Ganador: " + ganador.getNombre());
            
            JOptionPane.showMessageDialog(this,
                "¡" + ganador.getNombre() + " gana el combate!\n\n" +
                "Total de victorias: " + ganador.getVictorias(),
                "Victoria",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            lblEstado.setText("Estado: Error en el combate");
        }
    }
    
    private void mostrarHistorialTurnos(List<Map<String, Object>> turnos) {
        txtResultados.append("╔════════════════════════════════════════╗\n");
        txtResultados.append("║        HISTORIAL DE TURNOS             ║\n");
        txtResultados.append("╚════════════════════════════════════════╝\n\n");
        
        if (turnos != null) {
            for (Map<String, Object> turno : turnos) {
                int numero = (Integer) turno.get("numero");
                String luchador = (String) turno.get("luchador");
                String kimarite = (String) turno.get("kimarite");
                double probabilidad = (Double) turno.get("probabilidad");
                boolean exito = (Boolean) turno.get("exito");
                
                String resultado = exito ? "✓ EXITO" : "✗ Falló";
                
                txtResultados.append(String.format("Turno %d: %s usa %s (%.0f%% prob) → %s\n",
                    numero,
                    luchador,
                    kimarite,
                    probabilidad * 100,
                    resultado
                ));
            }
        }
    }
    
    private void mostrarResultadoFinal(Luchador ganador) {
        txtResultados.append("\n╔════════════════════════════════════���═══╗\n");
        txtResultados.append("║      COMBATE FINALIZADO                ║\n");
        txtResultados.append("╚════════════════════════════════════════╝\n\n");
        
        if (ganador != null) {
            txtResultados.append("🥇 GANADOR: " + ganador.getNombre() + "\n");
            txtResultados.append("   Peso: " + ganador.getPeso() + " kg\n");
            txtResultados.append("   Victorias Actuales: " + ganador.getVictorias() + "\n");
            
            txtResultados.append("\n─────────────────────────────────────────\n");
            txtResultados.append("       ESTADÍSTICAS FINALES\n");
            txtResultados.append("─────────────────────────────────────────\n\n");
            txtResultados.append(luchador1.getNombre() + ": " + luchador1.getVictorias() + " victorias\n");
            txtResultados.append(luchador2.getNombre() + ": " + luchador2.getVictorias() + " victorias\n");
        } else {
            txtResultados.append("ERROR: No hay ganador\n");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipalServidor ventana = new VentanaPrincipalServidor();
            ventana.setVisible(true);
        });
    }
}