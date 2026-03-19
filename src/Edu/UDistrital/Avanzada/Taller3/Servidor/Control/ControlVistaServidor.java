/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Vista.VentanaPrincipalServidor;
import java.io.File;

/**
 * Control de la Vista del Servidor
 * Intermediario entre Vista y ControlPrincipalServidor
 * @author nath
 */
public class ControlVistaServidor {
    
    private VentanaPrincipalServidor vista;
    private ControlPrincipalServidor controlPrincipal;
    
    /**
     * Constructor que recibe ControlPrincipalServidor e instancia la Vista
     */
    public ControlVistaServidor(ControlPrincipalServidor controlPrincipal) {
        this.controlPrincipal = controlPrincipal;
        // Crear vista pasando this (ControlVistaServidor)
        this.vista = new VentanaPrincipalServidor(this);
    }
    
    /**
     * Obtiene la vista
     */
    public VentanaPrincipalServidor getVista() {
        return vista;
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