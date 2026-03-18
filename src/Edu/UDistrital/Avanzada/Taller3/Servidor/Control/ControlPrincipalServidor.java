/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;


import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Luchador;
import java.util.List;
import java.util.Map;

/**
 * Control Principal del Servidor Sumo
 * Gestiona el combate y procesa el historial de turnos
 * @author nath
 */
public class ControlPrincipalServidor {

    private ControlDohyo controlDohyo;
    
    public void inicio() {
        controlDohyo = new ControlDohyo();
        // Espera conexiones de clientes y crea combates
    }
    
    /**
     * Obtiene el historial de turnos del último combate
     * @return List de Map con datos de cada turno
     */
    public List<Map<String, Object>> obtenerHistorialTurnos() {
        if (controlDohyo != null) {
            return controlDohyo.getHistorialTurnos();
        }
        return null;
    }
    
    /**
     * Obtiene el ganador del último combate
     * @return Luchador ganador
     */
    public Luchador obtenerGanador() {
        if (controlDohyo != null) {
            return controlDohyo.getGanador();
        }
        return null;
    }
    
    public ControlDohyo getControlDohyo() {
        return controlDohyo;
    }
    
    public void setControlDohyo(ControlDohyo controlDohyo) {
        this.controlDohyo = controlDohyo;
    }
}
   