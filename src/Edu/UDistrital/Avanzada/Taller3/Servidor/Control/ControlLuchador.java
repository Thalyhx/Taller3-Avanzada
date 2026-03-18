/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Luchador;
import java.util.Random;

/**
 * Control de un luchador en el combate
 * Ejecuta turnos sincronizados con ControlDohyo
 * @author nath
 */

public class ControlLuchador implements Runnable {
    private Luchador luchador;
    private ControlDohyo controlDohyo;
    private int id;
    private Random random;
    
    public ControlLuchador(Luchador luchador, ControlDohyo controlDohyo, int id) {
        this.luchador = luchador;
        this.controlDohyo = controlDohyo;
        this.id = id;
        this.random = new Random();
    }
    
    @Override
    public void run() {
        while (controlDohyo.isCombateActivo()) {
            // Espera aleatoria 
            
            try {
                Thread.sleep(random.nextInt(501));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Atacar
            controlDohyo.ejecutarTurno(luchador, id);
        }
    }
}
