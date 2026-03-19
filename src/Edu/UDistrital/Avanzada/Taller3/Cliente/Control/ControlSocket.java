/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Cliente.Control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import Edu.UDistrital.Avanzada.Taller3.Cliente.Modelo.ConfigConexion;

/**
 * Controlador encargado de la comunicación con el servidor.
 */
public class ControlSocket {

    private ConfigConexion config;
    private java.net.Socket socket;
    private DataInputStream entrada;
    private DataOutputStream salida;

    public ControlSocket(ConfigConexion config) {
        this.config = config;
    }
    
     /**
     * Conecta con el servidor
     */
    public void conectar() throws IOException {
        socket = new java.net.Socket(config.getIp(), config.getPuerto());
        entrada = new DataInputStream(socket.getInputStream());
        salida = new DataOutputStream(socket.getOutputStream());
    }
    
    /**
     * Envía los datos del luchador con sus técnicas
     * @param nombre nombre del luchador
     * @param peso peso del luchador
     * @param kimarites array de Kimarite del luchador
     */
    public void enviarLuchador(String nombre, double peso, KimariteLoader.Kimarite[] kimarites) throws IOException {
        salida.writeUTF(nombre);
        salida.writeDouble(peso);
        salida.writeInt(kimarites.length);

        for (KimariteLoader.Kimarite kimarite : kimarites) {
            salida.writeUTF(kimarite.getNombre());
        }
        
        salida.flush();
    }

    /**
     * Obtiene el stream de entrada
     */
    public DataInputStream getEntrada() {
        return entrada;
    }
    
    /**
     * Obtiene el stream de salida
     */
    public DataOutputStream getSalida() {
        return salida;
    }
    
}