/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Cliente.Control;

import java.io.DataInputStream;
import java.io.IOException;
import Edu.UDistrital.Avanzada.Taller3.Cliente.Vista.VentanaCliente;

/**
 * Hilo encargado de escuchar la respuesta del servidor.
 */
public class ThreadCliente extends Thread {

    private DataInputStream entrada;
    private VentanaCliente vista;

    public ThreadCliente(DataInputStream entrada, VentanaCliente vista) {
        this.entrada = entrada;
        this.vista = vista;
    }

    @Override
    public void run() {

        try {
            while (true) {

                int opcion = entrada.readInt();

                switch (opcion) {

                    case 1:
                        String resultado = entrada.readUTF();
                        vista.mostrarMensaje(resultado);
                        break;
                }
            }

        } catch (IOException e) {
            vista.mostrarMensaje("Conexion finalizada");
        }
    }
}