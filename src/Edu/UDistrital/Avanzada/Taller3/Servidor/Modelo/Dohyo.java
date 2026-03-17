/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Control.ControlKimarite;


/**
 *
 * @author nath
 */

public class Dohyo {
    
//    private Luchador luchador1;
//    private Luchador luchador2;
    private int turnoActual = 1; // Comienza luchador 1
    private boolean combateActivo = true;
    private Luchador ganador;
    private ControlKimarite controlKimarite;
    
     /**
     * Constructor que recibe el control de kimarites
     */
    public Dohyo(ControlKimarite controlKimarite) {
        this.controlKimarite = controlKimarite;
    }
    
//    /**
//     * Establece los luchadores del combate
//     */
//    public void setLuchadores(Luchador l1, Luchador l2) {
//        this.luchador1 = l1;
//        this.luchador2 = l2;
//    }
    
    /**
     * Ejecuta un turno del combate
     * Sincroniza para que solo un luchador ataque por turno
     */
    public synchronized void ejecutarTurno(Luchador atacante, int idLuchador) {
        
        // Esperar turno
        while (idLuchador != turnoActual && combateActivo) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Si el combate ya acabó, se sale
        if (!combateActivo) return;

        // Ejecutar el ataque
        Kimarite tecnica = atacante.obtenerKimariteAleatorio();
        
        // Evalua Probabilidad de éxito
        boolean sacoAlOponente = controlKimarite.evaluarExito(tecnica);

        if (sacoAlOponente) {
            combateActivo = false;
            ganador = atacante;
            
        } else {
            // 4. Pasar el turno
            turnoActual = (idLuchador == 1) ? 2 : 1;
        }

        // Despertar al otro luchador
        notifyAll();
    }
    
     /**
     * Verifica si el combate está activo
     */
    public boolean isCombateActivo() {
        return combateActivo;
    }
    
    /**
     * Obtiene el ganador del combate
     */
    public Luchador getGanador() {
        return ganador;
    }
    
    /**
     * Obtiene el luchador 1
     */
//    public Luchador getLuchador1() {
//        return luchador1;
//    }
//    
//    /**
//     * Obtiene el luchador 2
//     */
//    public Luchador getLuchador2() {
//        return luchador2;
//    }
}
