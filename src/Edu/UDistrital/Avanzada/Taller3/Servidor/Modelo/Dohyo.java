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
    
    private Luchador luchador1;
    private Luchador luchador2;
    private int turnoActual = 1; // Comienza luchador 1
    private boolean combateActivo = true;
    private Luchador ganador;
    private ControlKimarite controlKimarite;
    
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
            ganador.incrementarVictorias();
        } else {
            // 4. Pasar el turno
            turnoActual = (idLuchador == 1) ? 2 : 1;
        }

        // Despertar al otro luchador
        notifyAll();
    }
    
    public boolean isCombateActivo() {
        return combateActivo;
    }
    
    public Luchador getGanador() {
        return ganador;
    }
}
