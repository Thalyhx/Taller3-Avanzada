/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Control.ControlDohyo;

/**
 *
 * @author nath
 */
public class ControlPrincipalServidor {

    private ControlDohyo controlDohyo;
    
    public void inicio() {
        controlDohyo = new ControlDohyo();
        // Espera conexiones de clientes y crea combates
    }
}
    
