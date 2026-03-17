/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author nath
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Vista;

import edu.UDistrital.Avanzada.Taller3.Servidor.Control.ControlVistaServidor;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Luchador;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Kimarite;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana Principal del Servidor Sumo
 * @author nath
 */
public class VentanaPrincipalServidor extends JFrame {
    private ControlVistaServidor controlVista;
    private JButton btnCargarKimarites;
    private JButton btnIniciarCombate;
    private JTextArea txtResultados;
    private JLabel lblEstado;
    
    public VentanaPrincipalServidor() {
        setTitle("Servidor Sumo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        this.controlVista = new ControlVistaServidor();
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnCargarKimarites = new JButton("Cargar Kimarites");
        btnCargarKimarites.addActionListener(e -> seleccionarArchivoKimarites());
        panelBotones.add(btnCargarKimarites);
        
        btnIniciarCombate = new JButton("Iniciar Combate");
        btnIniciarCombate.addActionListener(e -> iniciarCombateDemo());
        panelBotones.add(btnIniciarCombate);
        
        // Panel de resultados
        txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtResultados);
        
        // Panel de estado
        JPanel panelEstado = new JPanel();
        lblEstado = new JLabel("Estado: Listo");
        panelEstado.add(lblEstado);
        
        // Agregar componentes
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelEstado, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    /**
     * Abre JFileChooser para seleccionar el archivo kimarites.properties
     * ← AQUÍ está el JFileChooser
     */
    private void seleccionarArchivoKimarites() {
        JFileChooser fileChooser = new JFileChooser();
        
        // Configurar el filtro de archivos
        javax.swing.filechooser.FileNameExtensionFilter filter = 
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos Properties (*.properties)", "properties"
            );
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Seleccionar archivo kimarites.properties");
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Mostrar el diálogo
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            
            // Pasar al control para cargar
            boolean cargado = controlVista.cargarKimarites(rutaArchivo);

            if (cargado) {
                JOptionPane.showMessageDialog(
                    this,
                    "Kimarites cargados exitosamente desde:\n" + rutaArchivo,
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el archivo. Verifica que sea un archivo properties valido.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }
    
    /**
     * Inicia un combate de demostración
     */
    private void iniciarCombateDemo() {
        // Verificar que los kimarites estén cargados
        if (!controlVista.getControlKimarite().isKimaritesCargado()) {
            JOptionPane.showMessageDialog(
                this,
                "Debes cargar un archivo kimarites.properties primero",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Crear luchadores de demostración
        Kimarite[] tecnicas1 = {
            new Kimarite("Yorikiri", "Empuje por cintura", 0.35),
            new Kimarite("Tsuppari", "Empuje con palmas", 0.25)
        };
        
        Kimarite[] tecnicas2 = {
            new Kimarite("Shitatenage", "Tirada baja", 0.40),
            new Kimarite("Oshidashi", "Empuje pecho", 0.30)
        };
        
        Luchador luchador1 = new Luchador("Yokozuna", 130, tecnicas1);
        Luchador luchador2 = new Luchador("Ozeki", 125, tecnicas2);
        
        // Ejecutar combate en un thread separado
        new Thread(() -> ejecutarCombate(luchador1, luchador2)).start();
    }
    
    /**
     * Ejecuta el combate y muestra los resultados
     */
    private void ejecutarCombate(Luchador l1, Luchador l2) {
        lblEstado.setText("Estado: Combate en curso...");
        txtResultados.setText("");
        txtResultados.append("=== COMBATE DE SUMO ===\n");
        txtResultados.append("Luchador 1: " + l1.getNombre() + " (Peso: " + l1.getPeso() + " kg)\n");
        txtResultados.append("Luchador 2: " + l2.getNombre() + " (Peso: " + l2.getPeso() + " kg)\n");
        txtResultados.append("Victorias L1: " + l1.getVictorias() + "\n");
        txtResultados.append("Victorias L2: " + l2.getVictorias() + "\n");
        txtResultados.append("\n--- Iniciando combate ---\n\n");
        
        // Iniciar combate
        controlVista.getControlDohyo().iniciarCombate(l1, l2);
        
        // Esperar resultado
        Luchador ganador = controlVista.getControlDohyo().esperarResultado();
        
        // Mostrar resultados
        mostrarResultados(ganador, l1, l2);
    }
    
    /**
     * Muestra los datos del ganador y sus nuevas victorias
     */
    private void mostrarResultados(Luchador ganador, Luchador l1, Luchador l2) {
        StringBuilder resultado = new StringBuilder();
        resultado.append("\n=== COMBATE FINALIZADO ===\n\n");
        
        if (ganador != null) {
            resultado.append("GANADOR: ").append(ganador.getNombre()).append("\n");
            resultado.append("Peso: ").append(ganador.getPeso()).append(" kg\n");
            resultado.append("Total de victorias: ").append(ganador.getVictorias()).append("\n");
            
            resultado.append("\n--- Estadísticas Finales ---\n");
            resultado.append(l1.getNombre()).append(": ").append(l1.getVictorias()).append(" victorias\n");
            resultado.append(l2.getNombre()).append(": ").append(l2.getVictorias()).append(" victorias\n");
            
            lblEstado.setText("Estado: Combate finalizado. Ganador: " + ganador.getNombre());
        } else {
            resultado.append("ERROR: No hay ganador\n");
            lblEstado.setText("Estado: Error en el combate");
        }
        
        txtResultados.append(resultado.toString());
        
        // Mostrar diálogo de victoria
        JOptionPane.showMessageDialog(
            this,
            "¡" + ganador.getNombre() + " gana el combate!\n\n" +
            "Total de victorias: " + ganador.getVictorias(),
            "Victoria",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipalServidor ventana = new VentanaPrincipalServidor();
            ventana.setVisible(true);
        });
    }
    
    public void mostrarMensaje(String mensaje, String titulo, int tipoMensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipoMensaje);
    }

    public void mostrarError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error en el sistema", JOptionPane.ERROR_MESSAGE);
    }
}