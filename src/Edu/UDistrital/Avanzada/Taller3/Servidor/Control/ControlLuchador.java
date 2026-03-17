/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Dohyo;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Luchador;
import java.util.Random;

/**
 *
 * @author nath
 */

public class ControlLuchador implements Runnable {
    private Luchador luchador;
    private Dohyo dohyo;
    private int id;
    private Random random;
    
    public ControlLuchador(Luchador luchador, Dohyo dohyo, int id) {
        this.luchador = luchador;
        this.dohyo = dohyo;
        this.id = id;
        this.random = new Random();
    }
    
    @Override
    public void run() {
        while (dohyo.isCombateActivo()) {
            // Espera aleatoria (
            
            try {
                Thread.sleep(random.nextInt(501));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Atacar
            dohyo.ejecutarTurno(luchador, id);
        }
    }
}
