/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;

import java.io.File;

/**
 * Control de la Vista del Servidor
 * @author nath
 */
public class ControlVistaServidor {
    
    private ControlPrincipalServidor controlPrincipal;
    
    /**
     * Constructor
     */
    public ControlVistaServidor() {
        this.controlPrincipal = new ControlPrincipalServidor();
    }
    
    /**
     * Inicializa el servidor
     */
    public boolean inicializarServidor(File archivoConfig) {
        return controlPrincipal.inicializarServidor(archivoConfig);
    }
    
    /**
     * Obtiene el control principal
     */
    public ControlPrincipalServidor getControlPrincipal() {
        return controlPrincipal;
    }
}