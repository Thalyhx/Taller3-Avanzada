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
    private ControlKimarite controlKimarite;
    
    //Contructor Vacio

    public ControlDohyo() {
    }
    
      /**
     * Constructor que recibe el control de kimarites
     */
    public ControlDohyo(ControlKimarite controlKimarite) {
        this.controlKimarite = controlKimarite;
    }
    
    /**
     * Inicia un combate entre dos luchadores
     */
    public void iniciarCombate(Luchador l1, Luchador l2) {
        // Crear el dohyō con el control de kimarites
        this.dohyo = new Dohyo(controlKimarite);
//        this.dohyo.setLuchadores(l1, l2);
        
        // Crear executor con 2 hilos
        this.executor = Executors.newFixedThreadPool(2);
        
        // Crear controles para cada luchador
        ControlLuchador ctrl1 = new ControlLuchador(l1, dohyo, 1);
        ControlLuchador ctrl2 = new ControlLuchador(l2, dohyo, 2);
        
        // Sincroniza luchadores con el dohyō
        executor.execute(ctrl1);
        executor.execute(ctrl2);
    }
    
    /**
     * Espera a que termine el combate y retorna el ganador
     * Incrementa las victorias del ganador
     */
    public Luchador esperarResultado() {
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
         // Obtener el ganador
        Luchador ganador = dohyo.getGanador();
        
        // Incrementar victorias del ganador
        if (ganador != null) {
            ganador.incrementarVictorias();
        }
        
        return ganador;
    }
}

