/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Dohyo;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Luchador;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author nath
 */

public class ControlDohyo {
    private Dohyo dohyo;
    private ExecutorService executor;
    
    //Contructor Vacio

    public ControlDohyo() {
    }
    
    
    public void iniciarCombate(Luchador l1, Luchador l2) {
        this.dohyo = new Dohyo();
        this.executor = Executors.newFixedThreadPool(2);
        
        // Crear controles para cada luchador
        ControlLuchador ctrl1 = new ControlLuchador(l1, dohyo, 1);
        ControlLuchador ctrl2 = new ControlLuchador(l2, dohyo, 2);
        
        // Sincroniza luchadores con el dohyō
        executor.execute(ctrl1);
        executor.execute(ctrl2);
    }
    
    public Luchador esperarResultado() {
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return dohyo.getGanador();
    }
}

