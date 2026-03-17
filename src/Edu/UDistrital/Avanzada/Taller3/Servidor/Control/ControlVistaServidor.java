/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.UDistrital.Avanzada.Taller3.Servidor.Control;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Control.ControlDohyo;
import Edu.UDistrital.Avanzada.Taller3.Servidor.Control.ControlKimarite;


/**
 *
 * @author nath
 */

public class ControlVistaServidor {
    
    private ControlKimarite controlKimarite;
    private ControlDohyo controlDohyo;
    
    public ControlVistaServidor() {
        this.controlKimarite = new ControlKimarite();
        
        // Inicializar ControlDohyo con ControlKimarite
        this.controlDohyo = new ControlDohyo(controlKimarite);
    }
    
    /**
     * Carga el archivo de kimarites y muestra mensaje de resultado
     * @param rutaArchivo ru    ta del archivo seleccionado
     */
    
    /**
     * Carga el archivo de kimarites y muestra mensaje de resultado
     * @param rutaArchivo ruta del archivo seleccionado
     * @return si se cargo el properties
     */
    public boolean cargarKimarites(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isEmpty()) {
            return false;
        }
        
        java.io.File archivo = new java.io.File(rutaArchivo);
        boolean cargado = controlKimarite.cargarKimaritesDesdeArchivo(archivo);
        
        return cargado;
    }
    
    public ControlKimarite getControlKimarite() {
        return controlKimarite;
    }
    
    public ControlDohyo getControlDohyo() {
        return controlDohyo;
    }

}