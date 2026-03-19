/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Vista.VentanaPrincipalServidor;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Servidor;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Luchador;
import javax.swing.SwingUtilities;
import java.io.File;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Control Principal del Servidor Sumo
 * Gestiona el servidor, combate y procesa el historial de turnos
 * @author nath
 */
public class ControlPrincipalServidor {

    private Servidor servidor;
    private ControlDohyo controlDohyo;
    private VentanaPrincipalServidor vista;
    private ControlVistaServidor controlVista;
    
    /**
     * Constructor - Instancia ControlVistaServidor que instancia la Vista
     */
    public ControlPrincipalServidor() {
        // Instanciar en EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            this.controlVista = new ControlVistaServidor(this);
            this.vista = controlVista.getVista();
            this.vista.setVisible(true);
        });
    }
    
    /**
     * Inicializa el servidor con un archivo de configuración
     * @param archivoConfig archivo properties del servidor
     * @return true si se inicializó correctamente
     */
    public boolean inicializarServidor(File archivoConfig) {
        try {
            // Crear servidor 
            this.servidor = new Servidor(archivoConfig);

            // Crear ControlKimarite para evaluar kimarites
            ControlKimarite controlKimarite = new ControlKimarite();

            // Crear ControlDohyo con servidor y control de kimarites
            this.controlDohyo = new ControlDohyo(servidor, controlKimarite);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Inicia el servidor en el puerto configurado
     */
    public void iniciarServidor() throws Exception {
        if (servidor == null) {
            throw new Exception("Servidor no inicializado");
        }
        servidor.iniciar();
    }
    
    /**
     * Acepta un cliente (bloquea hasta que uno se conecte)
     */
    public Socket aceptarCliente() throws Exception {
        if (servidor == null) {
            throw new Exception("Servidor no inicializado");
        }
        return servidor.aceptarCliente();
    }
    
    /**
     * Recibe los datos de un luchador desde el socket
     */
    public Luchador recibirLuchador(Socket cliente) throws Exception {
        if (controlDohyo == null) {
            throw new Exception("ControlDohyo no inicializado");
        }
        return controlDohyo.recibirLuchador(cliente);
    }
    
    /**
     * Inicia un combate entre dos luchadores
     */
    public void iniciarCombate(Luchador l1, Luchador l2) {
        if (controlDohyo == null) {
            throw new RuntimeException("ControlDohyo no inicializado");
        }
        controlDohyo.iniciarCombate(l1, l2);
    }
    
    /**
     * Espera a que termine el combate y retorna el ganador
     */
    public Luchador esperarResultado() {
        if (controlDohyo == null) {
            throw new RuntimeException("ControlDohyo no inicializado");
        }
        return controlDohyo.esperarResultado();
    }
    
    /**
     * Detiene el servidor
     */
    public void detenerServidor() throws Exception {
        if (servidor != null) {
            servidor.detener();
        }
    }
    
    /**
     * Obtiene el historial de turnos del último combate
     * @return List de Map con datos de cada turno
     */
    public List<Map<String, Object>> obtenerHistorialTurnos() {
        if (controlDohyo != null) {
            return controlDohyo.getHistorialTurnos();
        }
        return null;
    }
    
    /**
     * Obtiene el ganador del último combate
     * @return Luchador ganador
     */
    public Luchador obtenerGanador() {
        if (controlDohyo != null) {
            return controlDohyo.getGanador();
        }
        return null;
    }
    
    /**
     * Obtiene el puerto del servidor
     */
    public int getPuertoServidor() {
        if (servidor != null) {
            return servidor.getPuerto();
        }
        return -1;
    }
    
    /**
     * Obtiene la dirección del servidor
     */
    public String getDireccionServidor() {
        if (servidor != null) {
            return servidor.getDireccion();
        }
        return null;
    }
    
    /**
     * Obtiene el nombre del archivo de configuración
     */
    public String getNombreArchivoConfig() {
        if (servidor != null && servidor.getArchivoConfig() != null) {
            return servidor.getArchivoConfig().getName();
        }
        return null;
    }
    
    /**
     * Obtiene la vista
     */
    public VentanaPrincipalServidor getVista() {
        return vista;
    }
    
    public ControlDohyo getControlDohyo() {
        return controlDohyo;
    }
}   
   