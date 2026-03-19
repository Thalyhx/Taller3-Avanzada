/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;


import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Servidor;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Luchador;
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
    
    /**
     * Constructor vacío
     */
    public ControlPrincipalServidor() {
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
     * Obtiene el ganador del último combate
     * @return Luchador ganador
     */
    public Luchador obtenerGanador() {
        if (controlDohyo != null) {
            return controlDohyo.getGanador();
        }
        return null;
    }
    
    public ControlDohyo getControlDohyo() {
        return controlDohyo;
    }
    
    public void setControlDohyo(ControlDohyo controlDohyo) {
        this.controlDohyo = controlDohyo;
    }
}
   