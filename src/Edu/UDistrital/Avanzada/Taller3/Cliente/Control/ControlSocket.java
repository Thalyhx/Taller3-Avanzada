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

    public void conectar() throws IOException {
        socket = new java.net.Socket(config.getIp(), config.getPuerto());
        entrada = new DataInputStream(socket.getInputStream());
        salida = new DataOutputStream(socket.getOutputStream());
    }

    public void enviarLuchador(String nombre, double peso, String[] tecnicas) throws IOException {

        salida.writeUTF(nombre);
        salida.writeDouble(peso);
        salida.writeInt(tecnicas.length);

        for (String tecnica : tecnicas) {
            salida.writeUTF(tecnica);
        }
    }

    public DataInputStream getEntrada() {
        return entrada;
    }
}