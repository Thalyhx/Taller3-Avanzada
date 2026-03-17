/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Edu.UDistrital.Avanzada.Taller3.Servidor.Control;

import Edu.UDistrital.Avanzada.Taller3.Servidor.Modelo.Kimarite;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author nath
 */

public class ControlKimarite {
    private Properties kimariteProperties;
    private Random random;
    private File archivoKimarites;
    
    public ControlKimarite() {
        this.random = new Random();
        this.kimariteProperties = new Properties();
    }
    
    /**
     * Carga las propiedades de kimarites desde un archivo seleccionado por el usuario
     * mediante JFileChooser
     * @param archivo el archivo a cargar
     * @return true si se cargó exitosamente, false en caso contrario
     */
    public boolean cargarKimaritesDesdeArchivo(File archivo) {
        if (archivo == null || !archivo.exists()) {
            
            return false;

        }
        
        try (InputStream input = new FileInputStream(archivo)) {
            kimariteProperties.load(input);
            this.archivoKimarites = archivo;
            
            return true;
            
        } catch (IOException e) {
            
            return false;
            
        }
    }
    
    /**
     * Verifica si cargó el properties
     */
    public boolean isKimaritesCargado() {
        return !kimariteProperties.isEmpty();
    }
    
    /**
     * Obtiene el archivo actual de kimarites
     */
    public File getArchivoKimarites() {
        return archivoKimarites;
    }
    
    /**
     * Obtiene la probabilidad de éxito de un Kimarite
     * 
     * @param kimarite el técnica a evaluar
     * @return probabilidad de éxito (0.0 a 1.0)
     */
    public double obtenerProbabilidadExito(Kimarite kimarite) {
        String nombreKimarite = kimarite.getNombre();
        
        // Obtener la probabilidad del archivo properties
        String probabilidadStr = kimariteProperties.getProperty(
            nombreKimarite + ".probabilidad", "0.3"
        );
        
        try {
            return Double.parseDouble(probabilidadStr);
        } catch (NumberFormatException e) {

            return 0.3; 
        }
    }
    
    /**
     * Evalúa si un Kimarite tiene éxito basado en su probabilidad
     * 
     * @param kimarite a evaluar
     * @return true si la técnica saca al oponente, false en caso contrario
     */
    public boolean evaluarExito(Kimarite kimarite) {
        double probabilidadExito = obtenerProbabilidadExito(kimarite);
        double valorAleatorio = random.nextDouble();
        
        return valorAleatorio < probabilidadExito;
    }
    
    /**
     * Obtiene información adicional del Kimarite desde properties
     */
    public String obtenerDescripcion(Kimarite kimarite) {
        return kimariteProperties.getProperty(
            kimarite.getNombre() + ".descripcion", 
            "Técnica sin descripción"
        );
    }
}
